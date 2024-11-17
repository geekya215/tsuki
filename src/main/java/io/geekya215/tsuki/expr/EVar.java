package io.geekya215.tsuki.expr;

import org.jetbrains.annotations.NotNull;

public record EVar(@NotNull String x) implements Expr {
}
