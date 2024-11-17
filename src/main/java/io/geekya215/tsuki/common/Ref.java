package io.geekya215.tsuki.common;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class Ref<T> {
    private @NotNull T value;

    private Ref(@NotNull T value) {
        this.value = value;
    }

    public static <T> Ref<T> of(@NotNull T value) {
        return new Ref<>(value);
    }

    public T unwrap() {
        return this.value;
    }

    public void update(@NotNull T value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ref<?> ref = (Ref<?>) o;
        return Objects.equals(value, ref.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
