package io.geekya215.tsuki;

import io.geekya215.tsuki.type.Type;

import java.util.List;

public record Scheme(List<Integer> tvars, Type t) {
}
