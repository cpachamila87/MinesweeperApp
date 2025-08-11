package com.gic.minesweeper.helper;

import java.util.Random;

// Helper class to check random behaviour for testing
public class RandomHelper extends Random {
    private int[] values;
    private int index = 0;

    public void setValues(int[] values) {
        this.values = values;
        this.index = 0;
    }

    @Override
    public int nextInt(int bound) {
        if (values == null || index >= values.length) {
            return super.nextInt(bound);
        }
        return values[index++];
    }
}
