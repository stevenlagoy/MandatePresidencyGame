package com.stevenlagoy.presidency.util;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * Thrown to indicate that a manager matching method failed to return a required match.
 */
public class MatchingException extends RuntimeException {
    public final Callable<Object> matchingMethod;

    public MatchingException(String message, Callable<Object> matchingMethod) {
        super(message);
        this.matchingMethod = matchingMethod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatchingException that = (MatchingException) o;
        return this.getMessage().equals(that.getMessage()) && Objects.equals(matchingMethod, that.matchingMethod);
    }
}
