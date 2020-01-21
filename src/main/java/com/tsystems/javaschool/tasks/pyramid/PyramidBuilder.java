package com.tsystems.javaschool.tasks.pyramid;

import java.util.Comparator;
import java.util.List;

public class PyramidBuilder {

    /**
     * Builds a pyramid with sorted values (with minumum value at the top line and maximum at the bottom,
     * from left to right). All vacant positions in the array are zeros.
     *
     * @param inputNumbers to be used in the pyramid
     * @return 2d array with pyramid inside
     * @throws {@link CannotBuildPyramidException} if the pyramid cannot be build with given input
     */
    public int[][] processBuild(List<Integer> inputNumbers) {
        if (inputNumbers.contains(null) || inputNumbers.isEmpty()) {
            throw new CannotBuildPyramidException();
        }
        int pyramidHeight = findPyramidHeight(inputNumbers.size());
        int pyramidLength = pyramidHeight * 2 - 1;
        inputNumbers.sort(Comparator.reverseOrder());
        return processBuild(inputNumbers, pyramidHeight, pyramidLength);
    }

    private int findPyramidHeight(int numbersCount) {
        double d = 1 + 4 * 2 * numbersCount;
        double n1 = (-1 - Math.sqrt(d)) / 2;
        double n2 = (-1 + Math.sqrt(d)) / 2;
        if (n1 > 0 && n1 % 1 == 0) {
            return (int) n1;
        } else if (n2 > 0 && n2 % 1 == 0) {
            return (int) n2;
        } else {
            throw new CannotBuildPyramidException();
        }
    }

    private int[][] processBuild(List<Integer> inputNumbers, int pyramidHeight, int pyramidLength) {
        int[][] answer = new int[pyramidHeight][pyramidLength];
        int index = 0;
        int currentPyramidLength = pyramidLength;
        for (int i = pyramidHeight - 1; i >= 0 ; i--) {
            int edgeZerosCount = (pyramidLength - currentPyramidLength) / 2;
            addZerosToEdges(answer[i], edgeZerosCount);
            for (int j = 0; j < currentPyramidLength; j++) {
                answer[i][pyramidLength - 1 - j - edgeZerosCount] = j % 2 == 0
                        ? inputNumbers.get(index++)
                        : 0;
            }
            currentPyramidLength -= 2;
        }
        return answer;
    }

    private void addZerosToEdges(int[] array, int edgeZerosCount) {
        for (int j = 0; j < edgeZerosCount; j++) {
            array[j] = 0;
            array[array.length - 1 - j] = 0;
        }
    }
}
