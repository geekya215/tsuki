package io.geekya215.tsuki.type;

import io.geekya215.tsuki.Ref;
import io.geekya215.tsuki.typevar.TypeVar;

public record TVar(Ref<TypeVar> typeVarRef) implements Type {
}
