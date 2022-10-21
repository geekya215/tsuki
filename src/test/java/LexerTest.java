import io.geekya215.tsuki.Lexer;
import io.geekya215.tsuki.token.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class LexerTest {
    @Test
    void getVar() {
        var expectedTokens = new Lexer("x").getTokens();
        var actualTokens = List.of(new Identifier("x"));
        Assertions.assertEquals(expectedTokens, actualTokens);
    }

    @Test
    void getLambda() {
        var expectedTokens = new Lexer("\\x.x").getTokens();
        var actualTokens = List.of(new Backslash(), new Identifier("x"), new Dot(), new Identifier("x"));
        Assertions.assertEquals(expectedTokens, actualTokens);
    }

    @Test
    void getApplication() {
        var expectedTokens = new Lexer("e1 e2").getTokens();
        var actualTokens = List.of(new Identifier("e1"), new Identifier("e2"));
        Assertions.assertEquals(expectedTokens, actualTokens);
    }

    @Test
    void getApplicationWithParenthsis() {
        var expectedTokens = new Lexer("e1 (e2 e3)").getTokens();
        var actualTokens = List.of(new Identifier("e1"), new LParen(), new Identifier("e2"), new Identifier("e3"), new RParen());
        Assertions.assertEquals(expectedTokens, actualTokens);
    }

    @Test
    void getCurryLambda() {
        var expectedTokens = new Lexer("\\x . \\y . x y").getTokens();
        var actualTokens = List.of(new Backslash(), new Identifier("x"), new Dot(), new Backslash(), new Identifier("y"), new Dot(), new Identifier("x"), new Identifier("y"));
        Assertions.assertEquals(expectedTokens, actualTokens);
    }
}
