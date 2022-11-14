package io.geekya215.tsuki;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;

public class Repl {
    private final BufferedReader reader;
    private final BufferedWriter writer;
    private final String prompt;

    public Repl(BufferedReader reader, BufferedWriter writer, String prompt) {
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

    public void write(String text) throws IOException {
        writer.write(text);
        writer.flush();
    }

    public void writeLine(String text) throws IOException {
        write(text + "\n");
    }
}
