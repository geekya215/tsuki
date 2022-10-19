package io.geekya215.tsuki.expr;

public record ELet(String x, Expr e1, Expr e2) implements Expr {
}
