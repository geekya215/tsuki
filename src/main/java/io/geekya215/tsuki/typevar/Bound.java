package io.geekya215.tsuki.typevar;

import io.geekya215.tsuki.type.Type;
import org.jetbrains.annotations.NotNull;

public record Bound(@NotNull Type t) implements TypeVar {
}
