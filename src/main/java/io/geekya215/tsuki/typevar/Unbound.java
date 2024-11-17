package io.geekya215.tsuki.typevar;

import org.jetbrains.annotations.NotNull;

public record Unbound(@NotNull Integer id,
                      @NotNull Integer level)
        implements TypeVar {
}
