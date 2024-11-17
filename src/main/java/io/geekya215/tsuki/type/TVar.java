package io.geekya215.tsuki.type;

import io.geekya215.tsuki.common.Ref;
import io.geekya215.tsuki.typevar.TypeVar;
import org.jetbrains.annotations.NotNull;

public record TVar(@NotNull Ref<TypeVar> typeVarRef) implements Type {
}
