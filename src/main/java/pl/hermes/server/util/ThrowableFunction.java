package pl.hermes.server.util;

public interface ThrowableFunction<T, R> {

    R apply(T arg) throws Exception;
}
