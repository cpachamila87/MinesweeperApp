package com.gic.minesweeper.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MinesweeperGridCellTest {

    private MinesweeperGridCell minesweeperGridCell;
    
    @Before
    public void setUp() {
        minesweeperGridCell = new MinesweeperGridCell();
    }
    
    @Test
    public void testInitialState() {
        Assert.assertFalse(minesweeperGridCell.isMine());
        Assert.assertFalse(minesweeperGridCell.isRevealed());
        Assert.assertEquals(0, minesweeperGridCell.getAdjacentMines());
    }
    
    @Test
    public void testSetMine() {
        minesweeperGridCell.setMine(true);
        Assert.assertTrue(minesweeperGridCell.isMine());
        
        minesweeperGridCell.setMine(false);
        Assert.assertFalse(minesweeperGridCell.isMine());
    }
    
    @Test
    public void testReveal() {
        minesweeperGridCell.reveal();
        Assert.assertTrue(minesweeperGridCell.isRevealed());
    }
    
    @Test
    public void testSetAdjacentMines() {
        minesweeperGridCell.setAdjacentMines(5);
        Assert.assertEquals(5, minesweeperGridCell.getAdjacentMines());
    }
    
    @Test
    public void testIncrementAdjacentMines() {
        Assert.assertEquals(0, minesweeperGridCell.getAdjacentMines());
        
        minesweeperGridCell.incrementAdjacentMines();
        Assert.assertEquals(1, minesweeperGridCell.getAdjacentMines());
        
        minesweeperGridCell.incrementAdjacentMines();
        Assert.assertEquals(2, minesweeperGridCell.getAdjacentMines());
    }
    
    @Test
    public void testGetDisplayValueUnrevealed() {
        Assert.assertEquals("_", minesweeperGridCell.getDisplayValue());
    }
    
    @Test
    public void testGetDisplayValueEmpty() {
        minesweeperGridCell.reveal();
        Assert.assertEquals("0", minesweeperGridCell.getDisplayValue());
    }
    
    @Test
    public void testGetDisplayValueWithAdjacentMines() {
        minesweeperGridCell.setAdjacentMines(3);
        minesweeperGridCell.reveal();
        Assert.assertEquals("3", minesweeperGridCell.getDisplayValue());
    }
}