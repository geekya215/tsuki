package io.geekya215.tsuki;

import io.geekya215.tsuki.exception.ParserException;
import io.geekya215.tsuki.expr.EAbs;
import io.geekya215.tsuki.expr.EApp;
import io.geekya215.tsuki.expr.EVar;
import io.geekya215.tsuki.expr.Expr;
import io.geekya215.tsuki.token.*;

// term ::= application
//       | LAMBDA IDENT DOT term
//
// application ::= atom application'
//
// application' ::= atom application'
//               | ε  /* empty */
//
// atom ::= LPAREN term RPAREN
//       | IDENT
public final class Parser {
    private Lexer lexer;

    public Parser(String input) {
        this.lexer = new Lexer(input);
    }

    public Expr parse() throws ParserException {
        return parseTerm();
    }

    private Expr parseTerm() throws ParserException {
        return switch (lexer.peek()) {
            case Backslash backslash -> {
                lexer.next();
                var ident = (Identifier) match(Identifier.class);
                skip(Dot.class);
                var e = parseTerm();
                yield new EAbs(ident.value(), e);
            }
            default -> parseApplication();
        };
    }

    private Expr parseApplication() throws ParserException {
        var lhs = parseAtom();
        while (true) {
            try {
                var rhs = parseAtom();
                lhs = new EApp(lhs, rhs);
            } catch (Exception e) {
                return lhs;
            }
        }
    }

    private Expr parseAtom() throws ParserException {
        return switch (lexer.peek()) {
            case Identifier identifier -> {
                var x = (Identifier) lexer.next();
                yield new EVar(x.value());
            }
            case LParen lParen -> {
                lexer.next();
                var term = parseTerm();
                skip(RParen.class);
                yield term;
            }
            default -> throw new ParserException("unexpected token");
        };
    }

    private Token match(Class<? extends Token> token) throws ParserException {
        if (lexer.peek().getClass() == token) {
            return lexer.next();
        } else {
            throw new ParserException(String.format("expected %s, but got %s", token, lexer.peek().getClass()));
        }
    }

    private void skip(Class<? extends Token> token) throws ParserException {
        if (lexer.peek().getClass() == token) {
            lexer.next();
        } else {
            throw new ParserException(String.format("expected %s, but got %s", token, lexer.peek().getClass()));
        }
    }
}
