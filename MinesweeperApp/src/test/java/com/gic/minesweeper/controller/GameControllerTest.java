package com.gic.minesweeper.controller;

import com.gic.minesweeper.model.MinesweeperGridCell;
import com.gic.minesweeper.service.MinesweeperGridService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

public class GameControllerTest {

    private GameController game;
    
    @Before
    public void setUp() {
        // Create a game with a 4x4 grid and 3 mines
        game = new GameController(4, 3);
    }
    
    @Test
    public void testGameInitialization() {
        Assert.assertNotNull(game.getGrid());
        Assert.assertEquals(4, game.getGrid().getSize());
        Assert.assertFalse(game.isGameOver());
        Assert.assertFalse(game.isGameWon());
    }
    
    @Test
    public void testDisplayGrid() {
        String display = game.gridView();
        Assert.assertNotNull(display);
        Assert.assertTrue(display.contains("1 2 3 4"));
        Assert.assertTrue(display.contains("A "));
        Assert.assertTrue(display.contains("B "));
        Assert.assertTrue(display.contains("C "));
        Assert.assertTrue(display.contains("D "));
    }
    
    @Test
    public void testSelectSquareValid() {

        try {

            Field gridField = GameController.class.getDeclaredField("minesweeperGrid");
            gridField.setAccessible(true);
            MinesweeperGridService minesweeperGrid = (MinesweeperGridService) gridField.get(game);

            MinesweeperGridCell minesweeperGridCell = minesweeperGrid.getCell(0, 0);
            setPrivateField(minesweeperGridCell, "mine", false);
            setPrivateField(minesweeperGridCell, "adjacentMines", 2);

            String result = game.selectSquare("A1");
            Assert.assertTrue(result.contains("2 adjacent mines"));
            Assert.assertFalse(game.isGameOver());
            Assert.assertTrue(minesweeperGridCell.isRevealed());
            
        } catch (Exception e) {
            Assert.fail("Test failed due to exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testSelectSquareInvalidFormat() {
        String result = game.selectSquare("A");
        Assert.assertTrue(result.contains("Square format is invalid"));
    }
    
    @Test
    public void testSelectSquareInvalidPosition() {
        String result = game.selectSquare("Z9");
        Assert.assertTrue(result.contains("Position is invalid"));
    }
    
    @Test
    public void testSelectSquareAlreadyRevealed() {
        try {

            Field gridField = GameController.class.getDeclaredField("minesweeperGrid");
            gridField.setAccessible(true);
            MinesweeperGridService minesweeperGrid = (MinesweeperGridService) gridField.get(game);

            MinesweeperGridCell minesweeperGridCell = minesweeperGrid.getCell(0, 0);
            setPrivateField(minesweeperGridCell, "mine", false);
            setPrivateField(minesweeperGridCell, "revealed", true);

            String result = game.selectSquare("A1");
            Assert.assertTrue(result.contains("already revealed"));
            
        } catch (Exception e) {
            Assert.fail("Test failed due to exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testSelectSquareMine() {
        try {

            Field gridField = GameController.class.getDeclaredField("minesweeperGrid");
            gridField.setAccessible(true);
            MinesweeperGridService minesweeperGrid = (MinesweeperGridService) gridField.get(game);

            MinesweeperGridCell minesweeperGridCell = minesweeperGrid.getCell(0, 0);
            setPrivateField(minesweeperGridCell, "mine", true);

            String result = game.selectSquare("A1");
            Assert.assertTrue(result.contains("detonated a mine"));
            Assert.assertTrue(game.isGameOver());
            Assert.assertFalse(game.isGameWon());
            Assert.assertTrue(minesweeperGridCell.isRevealed());
            
        } catch (Exception e) {
            Assert.fail("Test failed due to exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testSelectSquareWin() {
        try {

            Field gridField = GameController.class.getDeclaredField("minesweeperGrid");
            gridField.setAccessible(true);
            MinesweeperGridService minesweeperGrid = (MinesweeperGridService) gridField.get(game);

            for (int row = 0; row < 4; row++) {
                for (int col = 0; col < 4; col++) {
                    MinesweeperGridCell minesweeperGridCell = minesweeperGrid.getCell(row, col);
                    if (row == 0 && col == 0) {
                        setPrivateField(minesweeperGridCell, "mine", false);
                        setPrivateField(minesweeperGridCell, "revealed", false);
                    } else {
                        if (!minesweeperGridCell.isMine()) {
                            setPrivateField(minesweeperGridCell, "revealed", true);
                        }
                    }
                }
            }

            String result = game.selectSquare("A1");
            Assert.assertTrue(result.contains("Congratulations"));
            Assert.assertTrue(game.isGameOver());
            Assert.assertTrue(game.isGameWon());
            
        } catch (Exception e) {
            Assert.fail("Test failed due to exception: " + e.getMessage());
        }
    }

    private void setPrivateField(Object object, String fieldName, Object value) throws Exception {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, value);
    }
}