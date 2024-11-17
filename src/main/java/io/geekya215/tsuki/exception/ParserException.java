package io.geekya215.tsuki.exception;

import org.jetbrains.annotations.NotNull;

public final class ParserException extends RuntimeException {
    public ParserException(@NotNull final String message) {
        super(message);
    }
}
