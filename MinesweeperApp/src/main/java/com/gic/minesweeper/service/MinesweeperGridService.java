package com.gic.minesweeper.service;

import com.gic.minesweeper.model.MinesweeperGridCell;

import java.util.Random;


public class MinesweeperGridService {

    private final int size;
    private final MinesweeperGridCell[][] minesweeperGridCells;
    private final int totalMines;
    private boolean gameOver;
    private boolean gameWon;
    private final Random random;

    public MinesweeperGridService(int size, int mines) {
        this(size, mines, new Random());
    }

    public MinesweeperGridService(int size, int mines, Random random) {

        if (size < 2) {
            throw new IllegalArgumentException("Minimum Grid size should be 2x2");
        }

        int maxMines = (int)(size * size * 0.35);
        if (mines <= 0 || mines > maxMines) {
            throw new IllegalArgumentException("Mines count should be between 1 and " + maxMines);
        }

        this.size = size;
        this.totalMines = mines;
        this.minesweeperGridCells = new MinesweeperGridCell[size][size];
        this.gameOver = false;
        this.gameWon = false;
        this.random = random;

        initializeCells();
        placeMines();
        calculateAdjacentMines();
    }

    public int getSize() {
        return size;
    }

    public boolean isGameWon() {
        return gameWon;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    private void initializeCells() {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                minesweeperGridCells[row][col] = new MinesweeperGridCell();
            }
        }
    }

    private void placeMines() {
        int minesPlaced = 0;
        while (minesPlaced < totalMines) {
            int row = random.nextInt(size);
            int col = random.nextInt(size);
            
            if (!minesweeperGridCells[row][col].isMine()) {
                minesweeperGridCells[row][col].setMine(true);
                minesPlaced++;
            }
        }
    }

    private void calculateAdjacentMines() {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (minesweeperGridCells[row][col].isMine()) {
                    incrementAdjacentCells(row, col);
                }
            }
        }
    }

    private void incrementAdjacentCells(int row, int col) {
        for (int r = Math.max(0, row - 1); r <= Math.min(size - 1, row + 1); r++) {
            for (int c = Math.max(0, col - 1); c <= Math.min(size - 1, col + 1); c++) {
                if (r == row && c == col) {
                    continue;
                }

                if (!minesweeperGridCells[r][c].isMine()) {
                    minesweeperGridCells[r][c].incrementAdjacentMines();
                }
            }
        }
    }

    public boolean revealCell(int row, int col) {
        if (gameOver || gameWon || !isValidPosition(row, col)) {
            return false;
        }

        MinesweeperGridCell minesweeperGridCell = minesweeperGridCells[row][col];
        if (minesweeperGridCell.isRevealed()) {
            return true;
        }

        minesweeperGridCell.reveal();
        if (minesweeperGridCell.isMine()) {
            gameOver = true;
            return true;
        }

        if (minesweeperGridCell.getAdjacentMines() == 0) {
            revealAdjacentCells(row, col);
        }

        checkWinningCondition();
        
        return true;
    }

    private void revealAdjacentCells(int row, int col) {
        for (int r = Math.max(0, row - 1); r <= Math.min(size - 1, row + 1); r++) {
            for (int c = Math.max(0, col - 1); c <= Math.min(size - 1, col + 1); c++) {

                if (r == row && c == col) {
                    continue;
                }

                MinesweeperGridCell adjacentMinesweeperGridCell = minesweeperGridCells[r][c];
                if (adjacentMinesweeperGridCell.isRevealed() || adjacentMinesweeperGridCell.isMine()) {
                    continue;
                }

                adjacentMinesweeperGridCell.reveal();
                if (adjacentMinesweeperGridCell.getAdjacentMines() == 0) {
                    revealAdjacentCells(r, c);
                }
            }
        }
    }

    private void checkWinningCondition() {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                MinesweeperGridCell minesweeperGridCell = minesweeperGridCells[row][col];
                if (!minesweeperGridCell.isMine() && !minesweeperGridCell.isRevealed()) {
                    return;
                }
            }
        }

        gameWon = true;
    }

    public boolean isValidPosition(int row, int col) {
        return row >= 0 && row < size && col >= 0 && col < size;
    }

    public MinesweeperGridCell getCell(int row, int col) {
        if (!isValidPosition(row, col)) {
            throw new IllegalArgumentException("Cell position is invalid: " + row + "," + col);
        }
        return minesweeperGridCells[row][col];
    }

    public static int[] parseSquare(String square) {
        if (square == null || square.length() < 2) {
            throw new IllegalArgumentException("Square format is invalid: " + square);
        }

        char rowChar = Character.toUpperCase(square.charAt(0));
        int row = rowChar - 'A';
        
        try {
            int col = Integer.parseInt(square.substring(1)) - 1;
            return new int[]{row, col};
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Square format is invalid: " + square);
        }
    }
}