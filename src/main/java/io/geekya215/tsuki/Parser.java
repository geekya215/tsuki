package io.geekya215.tsuki;

import io.geekya215.tsuki.common.Option;
import io.geekya215.tsuki.common.Peekable;
import io.geekya215.tsuki.exception.ParserException;
import io.geekya215.tsuki.expr.EAbs;
import io.geekya215.tsuki.expr.EApp;
import io.geekya215.tsuki.expr.EVar;
import io.geekya215.tsuki.expr.Expr;
import io.geekya215.tsuki.token.*;
import org.jetbrains.annotations.NotNull;

//
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
//
public final class Parser {
    private final @NotNull Lexer lexer;
    private final @NotNull Peekable<Token> tokens;

    public Parser(@NotNull final String input) {
        this.lexer = new Lexer(input);
        this.tokens = new Peekable<>(lexer.getTokens().listIterator());
    }

    public @NotNull Expr parse() {
        return parseTerm();
    }

    private @NotNull Expr parseTerm() {
        return skip(Backslash.class) ? parseLambda() : parseApplication();
    }

    private @NotNull Expr parseLambda() {
        Identifier ident = (Identifier) consume(Identifier.class);
        consume(Dot.class);
        var e = parseTerm();
        return new EAbs(ident.value(), e);
    }

    private @NotNull Expr parseApplication() {
        var lhs = parseAtom();
        while (true) {
            Option<Expr> rhs = parseApplicationPrime();
            if (rhs instanceof Option.Some(Expr expr)) {
                lhs = new EApp(lhs, expr);
            } else {
                break;
            }
        }
        return lhs;
    }

    private @NotNull Option<Expr> parseApplicationPrime() {
        return match(Identifier.class) || match(LParen.class) ? Option.some(parseAtom()) : Option.none();
    }

    private @NotNull Expr parseAtom() {
        if (match(LParen.class)) {
            consume(LParen.class);
            var term = parseTerm();
            consume(RParen.class);
            return term;
        } else if (match(Identifier.class)) {
            Identifier identifier = (Identifier) consume(Identifier.class);
            return new EVar(identifier.value());
        } else {
            throw new ParserException("Expected expression");
        }
    }

    private boolean match(@NotNull final Class<? extends Token> tokenType) {
        return tokens.peek() instanceof Option.Some(Token tok) && tokenType.isInstance(tok);
    }

    private boolean skip(@NotNull final Class<? extends Token> tokenType) {
        if (match(tokenType)) {
            tokens.next();
            return true;
        } else {
            return false;
        }
    }

    private @NotNull Token consume(@NotNull final Class<? extends Token> tokenType) {
        if (match(tokenType)) {
            var token = ((Option.Some<Token>) tokens.next());
            return token.value();
        } else {
            throw new ParserException("Expected token of type " + tokenType.getSimpleName());
        }
    }
}
