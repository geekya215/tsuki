package io.geekya215.tsuki.expr;

import org.jetbrains.annotations.NotNull;

public record EApp(@NotNull Expr e1, @NotNull Expr e2) implements Expr {
}
