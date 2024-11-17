package io.geekya215.tsuki.common;

import org.jetbrains.annotations.NotNull;

public sealed interface Option<T> permits Option.Some, Option.None {
    static <T> Option<T> some(@NotNull final T value) {
        return new Some<>(value);
    }

    static <T> Option<T> none() {
        return new None<>();
    }

    record Some<T>(@NotNull T value) implements Option<T> {
        @Override
        public String toString() {
            return value.toString();
        }
    }

    record None<T>() implements Option<T> {
        @Override
        public String toString() {
            return "None";
        }
    }
}