package io.geekya215.tsuki.token;

public sealed interface Token permits Backslash, Dot, Identifier, LParen, RParen, Space {
    Token BACKSLASH = new Backslash();
    Token DOT = new Dot();
    Token LPAREN = new LParen();
    Token RPAREN = new RParen();
}
