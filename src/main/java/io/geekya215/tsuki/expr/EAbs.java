package io.geekya215.tsuki.expr;

import org.jetbrains.annotations.NotNull;

public record EAbs(@NotNull String x, @NotNull Expr e) implements Expr {
}
