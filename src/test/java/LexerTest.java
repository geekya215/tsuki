import io.geekya215.tsuki.Lexer;
import io.geekya215.tsuki.token.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class LexerTest {
    @Test
    void getVar() {
        var expectedTokens =  Lexer.tokenize("x");
        var actualTokens = List.of(new Identifier("x"));
        Assertions.assertEquals(expectedTokens, actualTokens);
    }

    @Test
    void getLambda() {
        var expectedTokens =  Lexer.tokenize("\\x.x");
        var actualTokens = List.of(new Backslash(), new Identifier("x"), new Dot(), new Identifier("x"));
        Assertions.assertEquals(expectedTokens, actualTokens);
    }

    @Test
    void getApplication() {
        var expectedTokens =  Lexer.tokenize("e1 e2");
        var actualTokens = List.of(new Identifier("e1"), new Identifier("e2"));
        Assertions.assertEquals(expectedTokens, actualTokens);
    }

    @Test
    void getApplicationWithParenthsis() {
        var expectedTokens =  Lexer.tokenize("e1 (e2 e3)");
        var actualTokens = List.of(new Identifier("e1"), new LParen(), new Identifier("e2"), new Identifier("e3"), new RParen());
        Assertions.assertEquals(expectedTokens, actualTokens);
    }

    @Test
    void getCurryLambda() {
        var expectedTokens =  Lexer.tokenize("\\x . \\y . x y");
        var actualTokens = List.of(new Backslash(), new Identifier("x"), new Dot(), new Backslash(), new Identifier("y"), new Dot(), new Identifier("x"), new Identifier("y"));
        Assertions.assertEquals(expectedTokens, actualTokens);
    }
}
