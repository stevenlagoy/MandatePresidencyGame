package com.stevenlagoy.presidency.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class CollectionOperations {

    private CollectionOperations() {}

    /**
     * Generates all unique combinations of the given items.
     *
     * <p>
     * A combination is an unordered selection of zero or more distinct elements
     * drawn from the input collection. The relative order of elements inside a
     * combination is irrelevant, and no combination is repeated.
     * </p>
     *
     * <p>
     * For an input of size {@code n}, this method conceptually produces
     * {@code 2^n} combinations (including the empty combination), subject to any
     * constraints imposed by the input collection.
     * </p>
     *
     * <p>
     * The returned structure is immutable with respect to the original input;
     * modifications to the input collection after invocation do not affect the
     * result.
     * </p>
     *
     * @param <T>   Element type
     * @param items the source collection of distinct items
     * @return a collection containing every possible combination of the input items
     */
    public static <T> Set<Set<T>> combinations(Collection<T> items) {
        List<T> list = new ArrayList<>(items);
        Set<Set<T>> result = new HashSet<>();
        combosRecurse(list, 0, new HashSet<>(), result);
        return result;
    }

    private static <T> void combosRecurse(List<T> items, int index, Set<T> current, Set<Set<T>> result) {
        if (index == items.size()) {
            result.add(new HashSet<>(current));
            return;
        }

        // Exclude current item
        combosRecurse(items, index + 1, current, result);

        // Include current item
        current.add(items.get(index));
        combosRecurse(items, index + 1, current, result);
        current.remove(items.get(index));
    }
}
