package com.gic.minesweeper.controller;

import com.gic.minesweeper.model.MinesweeperGridCell;
import com.gic.minesweeper.service.MinesweeperGridService;

/**
 * This is the Minesweeper controller class.
 * This manages the game flow and status.
 */
public class GameController {

    private MinesweeperGridService minesweeperGrid;

    public GameController(int size, int mines) {
        this.minesweeperGrid = new MinesweeperGridService(size, mines);
    }


    public String selectSquare(String square) {
        try {
            int[] position = MinesweeperGridService.parseSquare(square);
            int row = position[0];
            int col = position[1];
            
            if (!minesweeperGrid.isValidPosition(row, col)) {
                return "Position is invalid. Please try again.";
            }
            
            if (minesweeperGrid.getCell(row, col).isRevealed()) {
                return "This cell is already revealed. Please try again.";
            }
            
            minesweeperGrid.revealCell(row, col);
            
            if (minesweeperGrid.isGameOver()) {
                return "Oh no, you detonated a mine! Game over.";
            }
            
            if (minesweeperGrid.isGameWon()) {
                return "Congratulations, you have won the game!";
            }
            
            return "This square contains " + minesweeperGrid.getCell(row, col).getAdjacentMines() + " adjacent mines.";
            
        } catch (IllegalArgumentException e) {
            return "Square format is invalid. Please use this format (e.g., A1).";
        }
    }

    public String gridView() {
        StringBuilder sb = new StringBuilder();
        
        // Display column headers
        sb.append("  ");
        for (int col = 1; col <= minesweeperGrid.getSize(); col++) {
            sb.append(col).append(" ");
        }
        sb.append("\n");
        
        // Display rows
        for (int row = 0; row < minesweeperGrid.getSize(); row++) {
            sb.append((char)('A' + row)).append(" ");
            // Display cells in the row
            for (int col = 0; col < minesweeperGrid.getSize(); col++) {
                MinesweeperGridCell minesweeperGridCell = minesweeperGrid.getCell(row, col);
                sb.append(minesweeperGridCell.getDisplayValue()).append(" ");
            }
            sb.append("\n");
        }
        
        return sb.toString();
    }

    public boolean isGameOver() {
        return minesweeperGrid.isGameOver() || minesweeperGrid.isGameWon();
    }

    public boolean isGameWon() {
        return minesweeperGrid.isGameWon();
    }

    public MinesweeperGridService getGrid() {
        return minesweeperGrid;
    }
}