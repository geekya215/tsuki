package io.geekya215.tsuki;

import io.geekya215.tsuki.type.Type;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record Scheme(@NotNull List<@NotNull Integer> tvars, @NotNull Type t) {
}
