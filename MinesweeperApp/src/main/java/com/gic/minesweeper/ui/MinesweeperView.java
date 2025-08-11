package com.gic.minesweeper.ui;

import com.gic.minesweeper.controller.GameController;

import java.util.Scanner;

/**
 * This is the view class for the Minesweeper App.
 * This handles the user input and the flow of app through the command line.
 */
public class MinesweeperView {

    private static final Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        boolean play = true;
        
        while (play) {
            startGame();
            System.out.println("Press any key to play again...");
            String input = scanner.nextLine().trim();
            play = !input.isEmpty();
        }

        scanner.close();
    }

    private static void startGame() {
        System.out.println("```");
        System.out.println("Welcome to Minesweeper!\n");

        int size = getGridSize();

        int maxMines = (int)(size * size * 0.35);
        int mines = getMinesCount(maxMines);

        GameController game = new GameController(size, mines);
        
        System.out.println("\nHere is your minefield:");
        System.out.println(game.gridView());

        while (!game.isGameOver()) {
            System.out.print("Select a square to reveal (e.g. A1): ");
            String square = scanner.nextLine().trim();
            
            String result = game.selectSquare(square);
            System.out.println(result);
            
            if (!game.isGameOver()) {
                System.out.println("\nHere is your updated minefield:");
                System.out.println(game.gridView());
            }
        }

    }

    private static int getGridSize() {
        int size = 0;
        boolean validInput = false;
        
        while (!validInput) {
            System.out.print("Enter the size of the grid (e.g. 4 for a 4x4 grid): ");
            try {
                size = Integer.parseInt(scanner.nextLine().trim());
                if (size >= 2) {
                    validInput = true;
                } else {
                    System.out.println("Minimum Grid size should be 2x2.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Input is invalid. Please enter a number.");
            }
        }
        
        return size;
    }

    private static int getMinesCount(int maxMines) {
        int mines = 0;
        boolean validInput = false;
        
        while (!validInput) {
            System.out.println("Enter the number of mines to place on the grid (maximum is 35% of the total squares): ");
            try {
                mines = Integer.parseInt(scanner.nextLine().trim());
                if (mines > 0 && mines <= maxMines) {
                    validInput = true;
                } else {
                    System.out.println("Mines count should be between 1 and " + maxMines + ".");
                }

            } catch (NumberFormatException e) {
                System.out.println("Input is invalid. Please enter a number.");
            }
        }
        
        return mines;
    }
}