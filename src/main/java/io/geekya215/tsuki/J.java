package io.geekya215.tsuki;

import io.geekya215.tsuki.common.Ref;
import io.geekya215.tsuki.expr.*;
import io.geekya215.tsuki.type.TFun;
import io.geekya215.tsuki.type.TVar;
import io.geekya215.tsuki.type.Type;
import io.geekya215.tsuki.typevar.Bound;
import io.geekya215.tsuki.typevar.Unbound;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class J {
    private static int currentLevel = 1;
    private static int currentTypevar = 0;

    static void enterLevel() {
        currentLevel++;
    }

    static void exitLevel() {
        currentLevel--;
    }

    static int newVar() {
        return ++currentTypevar;
    }

    static @NotNull Type newVarType() {
        return new TVar(Ref.of(new Unbound(newVar(), currentLevel)));
    }

    static @NotNull Type instantiate(@NotNull final Scheme scheme) {
        var tvars = scheme.tvars();
        var t = scheme.t();
        var subst = tvars.stream()
            .collect(Collectors.toMap(Function.identity(), e -> newVarType()));
        return apply(subst, t);
    }

    static @NotNull Type apply(@NotNull final Map<@NotNull Integer, @NotNull Type> subst,
                               @NotNull final Type t) {
        return switch (t) {
            case TVar tVar -> switch (tVar.typeVarRef().unwrap()) {
                case Bound(Type type) -> apply(subst, type);
                case Unbound(_, Integer id) -> subst.getOrDefault(id, tVar);
            };
            case TFun(Type t1, Type t2) -> new TFun(apply(subst, t1), apply(subst, t2));
        };
    }

    static boolean occurs(final int id, final int level, @NotNull final Type t) {
        return switch (t) {
            case TVar tVar -> switch (tVar.typeVarRef().unwrap()) {
                case Bound(Type type) -> occurs(id, level, type);
                case Unbound(Integer _id, Integer _level) -> {
                    var minLevel = Math.min(level, _level);
                    tVar.typeVarRef().update(new Unbound(_id, minLevel));
                    yield Objects.equals(id, _id);
                }
            };
            case TFun(Type t1, Type t2) -> occurs(id, level, t1) || occurs(id, level, t2);
        };
    }

    static void unify(@NotNull final Type t1, @NotNull final Type t2) throws Exception {
        if (t1 instanceof TVar t && t.typeVarRef().unwrap() instanceof Bound(Type type)) {
            unify(type, t2);
        } else if (t2 instanceof TVar t && t.typeVarRef().unwrap() instanceof Bound(Type type)) {
            unify(t1, type);
        } else if (t1 instanceof TVar t && t.typeVarRef().unwrap() instanceof Unbound(Integer id, Integer level)) {
            if (Objects.equals(t1, t2)) {
                // Skip
            } else {
                if (occurs(id, level, t2)) {
                    throw new Exception();
                } else {
                    t.typeVarRef().update(new Bound(t2));
                }
            }
        } else if (t2 instanceof TVar t && t.typeVarRef().unwrap() instanceof Unbound(Integer id, Integer level)) {
            if (Objects.equals(t1, t2)) {
                // Skip
            } else {
                if (occurs(id, level, t1)) {
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

    static @NotNull Scheme generalize(@NotNull final Type t) {
        return new Scheme(ftv(t).stream().distinct().collect(Collectors.toList()), t);
    }

    static @NotNull List<@NotNull Integer> ftv(@NotNull final Type t) {
        return switch (t) {
            case TVar tVar -> switch (tVar.typeVarRef().unwrap()) {
                case Bound(Type type) -> ftv(type);
                case Unbound(Integer id, Integer level) -> {
                    var res = new ArrayList<Integer>();
                    if (level > currentLevel) {
                        res.add(id);
                    }
                    yield res;
                }
            };
            case TFun(Type t1, Type t2) -> {
                var res = ftv(t1);
                res.addAll(ftv(t2));
                yield res;
            }
        };
    }

    public static @NotNull Type infer(@NotNull final Map<@NotNull String, @NotNull Scheme> env,
                                      @NotNull final Expr expr) throws Exception {
        return switch (expr) {
            case EVar(String x) -> {
                var s = env.get(x);
                if (Objects.isNull(s)) {
                    throw new Exception();
                } else {
                    yield instantiate(s);
                }
            }
            case EAbs(String x, Expr e) -> {
                var t = newVarType();
                env.put(x, new Scheme(new ArrayList<>(), t));
                var _t = infer(env, e);
                yield new TFun(t, _t);
            }
            case EApp(Expr e1, Expr e2) -> {
                var t0 = infer(env, e1);
                var t1 = infer(env, e2);
                var t = newVarType();
                unify(t0, new TFun(t1, t));
                yield t;
            }
            case ELet(String x, Expr e1, _) -> {
                enterLevel();
                var t = infer(env, e1);
                exitLevel();
                env.put(x, generalize(t));
                yield infer(env, e1);
            }
        };
    }
}
