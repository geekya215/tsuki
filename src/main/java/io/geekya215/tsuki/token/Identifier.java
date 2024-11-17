package io.geekya215.tsuki.token;

import org.jetbrains.annotations.NotNull;

public record Identifier(@NotNull String value) implements Token {
}
