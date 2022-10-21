package io.geekya215.tsuki;

import io.geekya215.tsuki.token.*;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public final class Lexer {
    private List<Token> tokens;
    private Integer cursor;
    private Integer size;

    public Lexer(String input) {
        this.tokens = tokenize(input);
        this.cursor = -1;
        this.size = tokens.size();
    }

    private List<Token> tokenize(String input) {
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
                case "(" -> new LParen();
                case ")" -> new RParen();
                case "." -> new Dot();
                case "\\" -> new Backslash();
                default -> new Identifier(lex);
            };
            tokens.add(token);
        }

        return tokens;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    // Todo
    // use optional instead of throw exception
    public Token peek() {
        return tokens.get(cursor + 1);
    }

    // same as peek
    public Token next() {
        return tokens.get(++cursor);
    }
}
