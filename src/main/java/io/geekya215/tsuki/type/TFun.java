package io.geekya215.tsuki.type;

import org.jetbrains.annotations.NotNull;

public record TFun(@NotNull Type t1, @NotNull Type t2) implements Type {
}
