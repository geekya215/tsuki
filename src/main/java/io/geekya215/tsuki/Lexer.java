package io.geekya215.tsuki;

import io.geekya215.tsuki.token.*;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public final class Lexer {
    public static List<Token> tokenize(String input) {
        var text = input
            .replaceAll("\\(", " ( ")
            .replaceAll("\\)", " ) ")
            .replaceAll("\\.", " . ")
            .replaceAll("\\\\", " \\\\ ");
        var tokens = new ArrayList<Token>();
        var tokenizer = new StringTokenizer(text);

        while (tokenizer.hasMoreTokens()) {
            var lex = tokenizer.nextToken();
            var token = switch (lex) {
                case "("  -> new LParen();
                case ")" -> new RParen();
                case "." -> new Dot();
                case "\\" -> new Backslash();
                default -> new Identifier(lex);
            };
            tokens.add(token);
        }

        return tokens;
    }
}
