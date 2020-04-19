package pl.hermes.server.util;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class LazyValue<T> implements Supplier<T> {

    private final Supplier<T> supplier;
    private T value;

    public static <T> LazyValue<T> from(Supplier<T> supplier) {
        return new LazyValue<>(supplier);
    }

    public static <T> LazyValue<T> from(T value) {
        return new LazyValue<>(() -> value);
    }

    @Override
    public T get() {
        return value != null ? value : init();
    }

    private synchronized T init() {
        if (value == null) {
            value = requireNonNull(supplier.get());
        }
        return value;
    }
}
