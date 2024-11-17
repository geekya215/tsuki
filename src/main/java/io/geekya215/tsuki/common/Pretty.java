package io.geekya215.tsuki.common;

import io.geekya215.tsuki.type.TFun;
import io.geekya215.tsuki.type.TVar;
import io.geekya215.tsuki.type.Type;
import io.geekya215.tsuki.typevar.Bound;
import io.geekya215.tsuki.typevar.Unbound;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public final class Pretty {

    static void nextLetter(@NotNull final Ref<Character> characterRef) {
        characterRef.update((char) (characterRef.unwrap() + 1));
    }

    static boolean shouldParenthesize(@NotNull final Type t) {
        if (t instanceof TVar _t && _t.typeVarRef().unwrap() instanceof Bound __t) {
            return shouldParenthesize(__t.t());
        } else return t instanceof TFun;
    }

    public static @NotNull String prettyType(@NotNull final Ref<Character> typeVarNameRef,
                                             @NotNull final Map<@NotNull Integer, @NotNull String> tbl,
                                             @NotNull final Type t) {
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
