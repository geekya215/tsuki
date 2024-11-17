package io.geekya215.tsuki.exception;

import org.jetbrains.annotations.NotNull;

public final class LexerException extends RuntimeException {
    public LexerException(@NotNull final String message) {
        super(message);
    }
}
