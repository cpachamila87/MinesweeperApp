package com.gic.minesweeper.model;

public class MinesweeperGridCell {
    private boolean mine;
    private boolean revealed;
    private int adjacentMines;

    public MinesweeperGridCell() {
        this.mine = false;
        this.revealed = false;
        this.adjacentMines = 0;
    }

    public boolean isMine() {
        return mine;
    }

    public void setMine(boolean mine) {
        this.mine = mine;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public void reveal() {
        revealed = true;
    }

    public int getAdjacentMines() {
        return adjacentMines;
    }

    public void setAdjacentMines(int adjacentMines) {
        this.adjacentMines = adjacentMines;
    }

    public void incrementAdjacentMines() {
        this.adjacentMines++;
    }

    public String getDisplayValue() {
        if (!revealed) {
            return "_";
        } else if (adjacentMines == 0) {
            return "0";
        } else {
            return String.valueOf(adjacentMines);
        }
    }
}