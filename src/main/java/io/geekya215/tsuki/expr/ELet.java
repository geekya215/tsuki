package io.geekya215.tsuki.expr;

import org.jetbrains.annotations.NotNull;

public record ELet(@NotNull String x, @NotNull Expr e1, @NotNull Expr e2) implements Expr {
}
