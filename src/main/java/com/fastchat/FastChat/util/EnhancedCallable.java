package com.fastchat.FastChat.util;

/**
 *
 * @param <R> return type
 * @param <A> arguments to be put as varargs
 */
@FunctionalInterface
public interface EnhancedCallable<R, A> {
	R call(A... args);
}
