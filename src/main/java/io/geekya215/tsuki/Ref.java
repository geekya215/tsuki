package io.geekya215.tsuki;

import java.util.Objects;

public final class Ref<T> {
    private T value;

    private Ref(T value) {
        this.value = value;
    }

    public static <T> Ref of(T value) {
        return new Ref(value);
    }

    public T unwrap() {
        return this.value;
    }

    public Ref update(T value) {
        this.value = value;
        return this;
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
        return value != null ? value.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Ref{" + value + "}";
    }
}
