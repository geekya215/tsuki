package io.geekya215.tsuki;

import io.geekya215.tsuki.common.Pretty;
import io.geekya215.tsuki.common.Ref;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;

public final class Repl {
    private final @NotNull BufferedReader reader;
    private final @NotNull BufferedWriter writer;
    private final @NotNull String prompt;

    public Repl(@NotNull final BufferedReader reader, @NotNull final BufferedWriter writer, @NotNull final String prompt) {
        this.reader = reader;
        this.writer = writer;
        this.prompt = prompt;
    }

    public void start() throws Exception {
        while (true) {
            write(prompt + "> ");
            String input = reader.readLine();
            if (input.equalsIgnoreCase("exit")) {
                write("Bye\n");
                System.exit(0);
            } else {
                var env = new HashMap<String, Scheme>();
                var parser = new Parser(input);
                var expr = parser.parse();
                var type = J.infer(env, expr);
                writeLine(Pretty.prettyType(Ref.of('a'), new HashMap<>(), type));
            }
        }
    }

    public void write(@NotNull final String text) throws IOException {
        writer.write(text);
        writer.flush();
    }

    public void writeLine(@NotNull final String text) throws IOException {
        write(text + "\n");
    }
}
