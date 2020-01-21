package com.tsystems.javaschool.tasks.subsequence;

import java.util.List;

public class Subsequence {

    /**
     * Checks if it is possible to get a sequence which is equal to the first
     * one by removing some elements from the second one.
     *
     * @param x first sequence
     * @param y second sequence
     * @return <code>true</code> if possible, otherwise <code>false</code>
     */
    @SuppressWarnings("rawtypes")
    public boolean find(List x, List y) {
        if (x == null || y == null) {
            throw new IllegalArgumentException();
        }
        if (x.isEmpty()) {
            return true;
        }
        int i = 0;
        Object currentElement = x.get(i);
        for (Object obj : y) {
            if (currentElement.equals(obj)) {
                i++;
                if (i == x.size()) {
                    return true;
                }
                currentElement = x.get(i);
            }
        }
        return false;
    }
}
