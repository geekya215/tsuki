import io.geekya215.tsuki.Lexer;
import io.geekya215.tsuki.Parser;
import io.geekya215.tsuki.expr.EAbs;
import io.geekya215.tsuki.expr.EApp;
import io.geekya215.tsuki.expr.EVar;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ParserTest {
    @Test
    void getEVar() throws Exception {
        var parser = new Parser("x");
        var expectedExpr = new EVar("x");
        var actualExpr = parser.parse();
        Assertions.assertEquals(expectedExpr, actualExpr);
    }

    @Test
    void getEAbs() throws Exception {
        var parser = new Parser("\\x . x");
        var expectedExpr = new EAbs("x", new EVar("x"));
        var actualExpr = parser.parse();
        Assertions.assertEquals(expectedExpr, actualExpr);
    }

    @Test
    void getCurry() throws Exception {
        var parser = new Parser("\\x . \\y . x y");
        var expectedExpr = new EAbs("x", new EAbs("y", new EApp(new EVar("x"), new EVar("y"))));
        var actualExpr = parser.parse();
        Assertions.assertEquals(expectedExpr, actualExpr);
    }

    @Test
    void getEAbsWithParenthesis() throws Exception {
        var parser = new Parser("\\x . \\y . x (y z)");
        var expectedExpr = new EAbs("x", new EAbs("y", new EApp(new EVar("x"), new EApp(new EVar("y"), new EVar("z")))));
        var actualExpr = parser.parse();
        Assertions.assertEquals(expectedExpr, actualExpr);
    }
}
