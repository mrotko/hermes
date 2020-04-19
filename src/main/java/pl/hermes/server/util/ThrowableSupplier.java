package pl.hermes.server.util;

public interface ThrowableSupplier<T> {

    T get() throws Exception;
}
