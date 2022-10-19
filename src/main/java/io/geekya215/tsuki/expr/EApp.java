package io.geekya215.tsuki.expr;

public record EApp(Expr e1, Expr e2) implements Expr {
}
