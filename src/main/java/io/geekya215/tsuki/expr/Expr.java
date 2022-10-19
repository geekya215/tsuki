package io.geekya215.tsuki.expr;

public sealed interface Expr permits EAbs, EApp, ELet, EVar {
}
