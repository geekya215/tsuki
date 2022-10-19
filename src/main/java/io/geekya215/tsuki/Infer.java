package io.geekya215.tsuki;

import io.geekya215.tsuki.expr.*;
import io.geekya215.tsuki.type.TFun;
import io.geekya215.tsuki.type.TVar;
import io.geekya215.tsuki.type.Type;
import io.geekya215.tsuki.typevar.Bound;
import io.geekya215.tsuki.typevar.Unbound;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Infer {
    private static Integer currentLevel = 1;
    private static Integer currentTypevar = 0;

    static void enterLevel() {
        currentLevel++;
    }

    static void exitLevel() {
        currentLevel--;
    }

    static Integer newVar() {
        return ++currentTypevar;
    }

    static Type newVarType() {
        return new TVar(Ref.of(new Unbound(newVar(), currentLevel)));
    }

    static Type instantiate(Scheme scheme) {
        var tvars = scheme.tvars();
        var t = scheme.t();
        var subst = tvars.stream()
            .collect(Collectors.toMap(Function.identity(), e -> newVarType()));
        return apply(subst, t);
    }

    static Type apply(Map<Integer, Type> subst, Type t) {
        return switch (t) {
            case TVar tVar -> switch (tVar.typeVarRef().unwrap()) {
                case Bound bound -> apply(subst, bound.t());
                case Unbound unbound -> subst.getOrDefault(unbound.id(), tVar);
            };
            case TFun tFun -> new TFun(apply(subst, tFun.t1()), apply(subst, tFun.t2()));
        };
    }

    static Boolean occurs(Integer id, Integer level, Type t) {
        return switch (t) {
            case TVar tVar -> switch (tVar.typeVarRef().unwrap()) {
                case Bound bound -> occurs(id, level, bound.t());
                case Unbound unbound -> {
                    var _id = unbound.id();
                    var _level = unbound.level();
                    var minLevel = Math.min(level, _level);
                    tVar.typeVarRef().update(new Unbound(_id, minLevel));
                    yield Objects.equals(id, _id);
                }
            };
            case TFun tFun -> occurs(id, level, tFun.t1()) || occurs(id, level, tFun.t2());
        };
    }

    static void unify(Type t1, Type t2) throws Exception {
        if (t1 instanceof TVar t && t.typeVarRef().unwrap() instanceof Bound b) {
            unify(b.t(), t2);
        } else if (t2 instanceof TVar t && t.typeVarRef().unwrap() instanceof Bound b) {
            unify(t1, b.t());
        } else if (t1 instanceof TVar t && t.typeVarRef().unwrap() instanceof Unbound ub) {
            if (Objects.equals(t1, t2)) {
                // Skip
            } else {
                if (occurs(ub.id(), ub.level(), t2)) {
                    throw new Exception();
                } else {
                    t.typeVarRef().update(new Bound(t2));
                }
            }
        } else if (t2 instanceof TVar t && t.typeVarRef().unwrap() instanceof Unbound ub) {
            if (Objects.equals(t1, t2)) {
                // Skip
            } else {
                if (occurs(ub.id(), ub.level(), t1)) {
                    throw new Exception();
                } else {
                    t.typeVarRef().update(new Bound(t1));
                }
            }
        } else if (t1 instanceof TFun _t1 && t2 instanceof TFun _t2) {
            unify(_t1.t1(), _t2.t1());
            unify(_t1.t2(), _t2.t2());
        } else {
            throw new Exception();
        }
    }

    static Scheme generalize(Type t) {
        return new Scheme(
            ftv(t).stream().distinct().collect(Collectors.toList()),
            t);
    }

    static List<Integer> ftv(Type t) {
        return switch (t) {
            case TVar tVar -> switch (tVar.typeVarRef().unwrap()) {
                case Bound bound -> ftv(bound.t());
                case Unbound unbound -> {
                    var res = new ArrayList<Integer>();
                    if (unbound.level() > currentLevel) {
                        res.add(unbound.id());
                    }
                    yield res;
                }
            };
            case TFun tFun -> {
                var res = ftv(tFun.t1());
                res.addAll(ftv(tFun.t2()));
                yield res;
            }
        };
    }

    public static Type infer(Map<String, Scheme> env, Expr expr) throws Exception {
        return switch (expr) {
            case EVar eVar -> {
                var x = eVar.x();
                var s = env.get(x);
                if (Objects.isNull(s)) {
                    throw new Exception();
                } else {
                    yield instantiate(s);
                }
            }
            case EAbs eAbs -> {
                var x = eAbs.x();
                var e = eAbs.e();
                var t = newVarType();
                env.put(x, new Scheme(new ArrayList<>(), t));
                var _t = infer(env, e);
                yield new TFun(t, _t);
            }
            case EApp eApp -> {
                var e1 = eApp.e1();
                var e2 = eApp.e2();
                var t0 = infer(env, e1);
                var t1 = infer(env, e2);
                var t = newVarType();
                unify(t0, new TFun(t1, t));
                yield t;
            }
            case ELet eLet -> {
                var x = eLet.x();
                var e1 = eLet.e1();

                enterLevel();
                var t = infer(env, e1);
                exitLevel();
                env.put(x, generalize(t));
                yield infer(env, e1);
            }
        };
    }
}
