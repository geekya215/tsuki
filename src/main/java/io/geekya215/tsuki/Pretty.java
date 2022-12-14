package io.geekya215.tsuki;

import io.geekya215.tsuki.type.TFun;
import io.geekya215.tsuki.type.TVar;
import io.geekya215.tsuki.type.Type;
import io.geekya215.tsuki.typevar.Bound;
import io.geekya215.tsuki.typevar.Unbound;

import java.util.Map;

public final class Pretty {

    static void nextLetter(Ref<Character> characterRef) {
        characterRef.update((char) (characterRef.unwrap() + 1));
    }

    static Boolean shouldParenthesize(Type t) {
        if (t instanceof TVar _t && _t.typeVarRef().unwrap() instanceof Bound __t) {
            return shouldParenthesize(__t.t());
        } else return t instanceof TFun;
    }

    static String prettyType(Ref<Character> typeVarNameRef, Map<Integer, String> tbl, Type t) {
        return switch (t) {
            case TVar tVar -> switch (tVar.typeVarRef().unwrap()) {
                case Bound bound -> prettyType(typeVarNameRef, tbl, bound.t());
                case Unbound unbound -> {
                    var n = unbound.id();
                    if (tbl.containsKey(n)) {
                        yield tbl.get(n);
                    } else {
                        var s = typeVarNameRef.unwrap().toString();
                        tbl.put(n, s);
                        nextLetter(typeVarNameRef);
                        yield s;
                    }
                }
            };
            case TFun tFun -> {
                var t1 = tFun.t1();
                var t2 = tFun.t2();
                var aStr = prettyType(typeVarNameRef, tbl, t1);
                var bStr = prettyType(typeVarNameRef, tbl, t2);
                if (shouldParenthesize(t1)) {
                    yield "(" + aStr + ") -> " + bStr;
                } else {
                    yield aStr + " -> " + bStr;
                }
            }
        };
    }
}
