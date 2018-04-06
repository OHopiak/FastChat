package com.fastchat.FastChat.util;

/**
 * @param <A> arguments to be put as varargs
 */
@FunctionalInterface
public interface EnhancedRunnable<A> {
	void run(A... args);
}

