package io.geekya215.tsuki;

import io.geekya215.tsuki.common.Option;
import io.geekya215.tsuki.common.Peekable;
import io.geekya215.tsuki.exception.LexerException;
import io.geekya215.tsuki.token.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public final class Lexer {
    private final @NotNull Peekable<Character> chars;

    public Lexer(@NotNull final String input) {
        final Iterator<Character> iter = input.chars().mapToObj(c -> (char) c).iterator();
        chars = new Peekable<>(iter);
    }

    public @NotNull List<@NotNull Token> getTokens() {
        var tokens = new ArrayList<Token>();
        while (nextToken() instanceof Option.Some<Token>(Token tok)) {
            if (!(tok instanceof Space)) {
                tokens.add(tok);
            }
        }
        return tokens;
    }

    public @NotNull Option<Token> nextToken() {
        return switch (chars.peek()) {
            case Option.Some(Character c) -> switch (c) {
                case ' ' -> consume(new Space());
                case '\\' -> consume(new Backslash());
                case '.' -> consume(new Dot());
                case '(' -> consume(new LParen());
                case ')' -> consume(new RParen());
                default -> {
                    if (Character.isAlphabetic(c)) {
                        final String ident = peekTakeWhite(ch -> Character.isAlphabetic(ch) || Character.isDigit(ch));
                        yield new Option.Some<>(new Identifier(ident));
                    } else {
                        throw new LexerException("invalid identifier [" + c + "]");
                    }
                }
            };
            case Option.None<Character> _ ->  Option.none();
        };
    }

    private @NotNull String peekTakeWhite(@NotNull final Predicate<Character> predicate) {
        final StringBuilder sb = new StringBuilder();
        while (chars.peek() instanceof Option.Some<Character>(Character c)) {
            if (predicate.test(c)) {
                chars.next();
                sb.append(c);
            } else {
                break;
            }
        }
        return sb.toString();
    }

    private @NotNull Option<Token> consume(@NotNull final Token token) {
        chars.next();
        return Option.some(token);
    }
}
