package com.gic.minesweeper.service;

import com.gic.minesweeper.helper.RandomHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MinesweeperGridTest {
    private MinesweeperGridService minesweeperGrid;
    private RandomHelper randomHelper;
    
    @Before
    public void setUp() {
        randomHelper = new RandomHelper();
        minesweeperGrid = new MinesweeperGridService(4, 3, randomHelper);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGridSizeTooSmall() {
        new MinesweeperGridService(1, 1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMinesCountTooLow() {
        new MinesweeperGridService(4, 0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMinesCountTooHigh() {
        new MinesweeperGridService(4, 7);
    }
    
    @Test
    public void testGridInitialization() {
        Assert.assertEquals(4, minesweeperGrid.getSize());
        Assert.assertFalse(minesweeperGrid.isGameOver());
        Assert.assertFalse(minesweeperGrid.isGameWon());
    }
    
    @Test
    public void testMinesPlacement() {
        randomHelper.setValues(new int[]{0, 0, 1, 1, 2, 2});
        minesweeperGrid = new MinesweeperGridService(4, 3, randomHelper);
        
        Assert.assertTrue(minesweeperGrid.getCell(0, 0).isMine());
        Assert.assertTrue(minesweeperGrid.getCell(1, 1).isMine());
        Assert.assertTrue(minesweeperGrid.getCell(2, 2).isMine());

        int mineCount = 0;
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                if (minesweeperGrid.getCell(row, col).isMine()) {
                    mineCount++;
                }
            }
        }
        Assert.assertEquals(3, mineCount);
    }
    
    @Test
    public void testAdjacentMinesCalculation() {
        // Setup mock to place mines at specific positions
        randomHelper.setValues(new int[]{0, 0, 1, 1, 2, 2});
        minesweeperGrid = new MinesweeperGridService(4, 3, randomHelper);
        
        // Check cells adjacent to mines
        Assert.assertEquals(2, minesweeperGrid.getCell(0, 1).getAdjacentMines());
        Assert.assertEquals(2, minesweeperGrid.getCell(1, 0).getAdjacentMines());
        Assert.assertEquals(2, minesweeperGrid.getCell(1, 2).getAdjacentMines());
        Assert.assertEquals(2, minesweeperGrid.getCell(2, 1).getAdjacentMines());
        Assert.assertEquals(1, minesweeperGrid.getCell(3, 3).getAdjacentMines());
    }
    
    @Test
    public void testRevealCell() {
        randomHelper.setValues(new int[]{0, 0, 1, 1, 2, 2});
        minesweeperGrid = new MinesweeperGridService(4, 3, randomHelper);

        Assert.assertTrue(minesweeperGrid.revealCell(3, 3));
        Assert.assertTrue(minesweeperGrid.getCell(3, 3).isRevealed());
        Assert.assertFalse(minesweeperGrid.isGameOver());
    }
    
    @Test
    public void testRevealMine() {
        randomHelper.setValues(new int[]{0, 0, 1, 1, 2, 2});
        minesweeperGrid = new MinesweeperGridService(4, 3, randomHelper);

        Assert.assertTrue(minesweeperGrid.revealCell(0, 0));
        Assert.assertTrue(minesweeperGrid.getCell(0, 0).isRevealed());
        Assert.assertTrue(minesweeperGrid.isGameOver());
        Assert.assertFalse(minesweeperGrid.isGameWon());
    }
    
    @Test
    public void testRevealEmptyCell() {
        randomHelper.setValues(new int[]{0, 0, 1, 1, 2, 2});
        minesweeperGrid = new MinesweeperGridService(4, 3, randomHelper);

        Assert.assertTrue(minesweeperGrid.revealCell(3, 0));
        Assert.assertTrue(minesweeperGrid.getCell(3, 0).isRevealed());
        Assert.assertTrue(minesweeperGrid.getCell(2, 0).isRevealed());
        Assert.assertTrue(minesweeperGrid.getCell(3, 1).isRevealed());
    }
    
    @Test
    public void testIsValidPosition() {
        Assert.assertTrue(minesweeperGrid.isValidPosition(0, 0));
        Assert.assertTrue(minesweeperGrid.isValidPosition(3, 3));
        Assert.assertFalse(minesweeperGrid.isValidPosition(-1, 0));
        Assert.assertFalse(minesweeperGrid.isValidPosition(0, -1));
        Assert.assertFalse(minesweeperGrid.isValidPosition(4, 0));
        Assert.assertFalse(minesweeperGrid.isValidPosition(0, 4));
    }
    
    @Test
    public void testGetCell() {
        Assert.assertNotNull(minesweeperGrid.getCell(0, 0));
        Assert.assertNotNull(minesweeperGrid.getCell(3, 3));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetCellInvalidPosition() {
        minesweeperGrid.getCell(-1, 0);
    }
    
    @Test
    public void testWinCondition() {
        randomHelper.setValues(new int[]{0, 0, 1, 1, 2, 2});
        minesweeperGrid = new MinesweeperGridService(4, 3, randomHelper);

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                if (!minesweeperGrid.getCell(row, col).isMine()) {
                    minesweeperGrid.revealCell(row, col);
                }
            }
        }
        
        Assert.assertTrue(minesweeperGrid.isGameWon());
        Assert.assertFalse(minesweeperGrid.isGameOver());
    }
    
    @Test
    public void testParseCoordinate() {
        int[] square = MinesweeperGridService.parseSquare("A1");
        Assert.assertEquals(0, square[0]);
        Assert.assertEquals(0, square[1]);

        square = MinesweeperGridService.parseSquare("B3");
        Assert.assertEquals(1, square[0]);
        Assert.assertEquals(2, square[1]);

        square = MinesweeperGridService.parseSquare("D4");
        Assert.assertEquals(3, square[0]);
        Assert.assertEquals(3, square[1]);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseCoordinateInvalidFormat() {
        MinesweeperGridService.parseSquare("A");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseCoordinateInvalidNumberFormat() {
        MinesweeperGridService.parseSquare("AX");
    }
    
    @Test
    public void testRevealAlreadyRevealed() {
        randomHelper.setValues(new int[]{0, 0, 1, 1, 2, 2});
        minesweeperGrid = new MinesweeperGridService(4, 3, randomHelper);

        minesweeperGrid.revealCell(3, 3);
        Assert.assertTrue(minesweeperGrid.getCell(3, 3).isRevealed());
        Assert.assertTrue(minesweeperGrid.revealCell(3, 3));
        Assert.assertFalse(minesweeperGrid.isGameOver());
        Assert.assertFalse(minesweeperGrid.isGameWon());
    }
    
}