package net.javasudoku.java;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


// 1. Define the interface
interface SudokuSolver {
    // Abstract method (does not have a body)
    void readArry(int[][] board, String filePath);
    boolean bruteforce(int[][] board, int gSize);
    void printBoard(int[][] board, int gSize);
    
}


// 2. Implement the interface in a concrete class
class Solver implements SudokuSolver {
    // The body of makeSound() is provided here
    @Override
    public void readArry(int[][] board, String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int row = 0;
            while ((line = br.readLine()) != null && row < board.length) {
                String[] values = line.split(",");
                for (int col = 0; col < values.length && col < board[row].length; col++) {
                    if ( col==0 && row == 0 ) {
                        values[col] = values[col].substring(1); // Remove the first character from the first value
                    }
                    if (values[col].trim().isEmpty()) {
                    board[row][col] = 0;
                    } else {
 //                       System.out.println(values[col].trim());
                        board[row][col] = Integer.parseInt(values[col].trim());
                    }
                }
                row++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }  	
    }

    // The body of sleep() is provided here
    @Override
    public boolean bruteforce(int[][] board, int gSize) {
        for (int row = 0; row < gSize; row++) {
            for (int col = 0; col < gSize; col++) {
                // Find an empty cell
                if (board[row][col] == 0) {
                    // Try numbers 1 through 9
                    for (int numberToTry = 1; numberToTry <= 9; numberToTry++) {
                        if (isValidPlacement(board, numberToTry, row, col, gSize)) {
                            board[row][col] = numberToTry;

                            // Recursively attempt to solve the rest of the board
                            if (bruteforce(board, gSize)) {
                                return true;
                            }

                            // Undo the move (backtrack) if it didn't lead to a solution
                            board[row][col] = 0;
                        }
                    }
                    // If no number from 1-9 works here, this path is dead
                    return false;
                }
            }
        }
        return true; // Entire board is filled legally
    }

    /**
     * Checks if placing a specific number in a cell breaks any Sudoku rules.
    **/
    private static boolean isValidPlacement(int[][] board, int number, int row, int col, int gSize) {
        return !isNumberInRow(board, number, row, gSize) &&
               !isNumberInColumn(board, number, col, gSize) &&
               !isNumberInBox(board, number, row, col, gSize);
    }

    private static boolean isNumberInRow(int[][] board, int number, int row, int gSize) {
        for (int i = 0; i < gSize; i++) {
            if (board[row][i] == number) {
                return true;
            }
        }
        return false;
    }

    private static boolean isNumberInColumn(int[][] board, int number, int col, int gSize) {
        for (int i = 0; i < gSize; i++) {
            if (board[i][col] == number) {
                return true;
            }
        }
        return false;
    }

    private static boolean isNumberInBox(int[][] board, int number, int row, int col, int gSize ) {
        // Calculate the top-left index of the local 3x3 box
        int localBoxRow = row - row % 3;
        int localBoxCol = col - col % 3;

        for (int i = localBoxRow; i < localBoxRow + 3; i++) {
            for (int j = localBoxCol; j < localBoxCol + 3; j++) {
                if (board[i][j] == number) {
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    public void printBoard(int[][] board, int gSize) {
                  // Optional: Print the 2D array to verify the output
        System.out.println("\n");
        for (int row = 0; row < gSize; row++) {
            if (row % 3 == 0 && row != 0) {
                System.out.println("---------------------");
            }
            for (int col = 0; col < gSize; col++) {
                if (col % 3 == 0 && col != 0) {
                    System.out.print("| ");
                }
                System.out.print(board[row][col] + " ");
            }
            System.out.println();
        }
    }
}

// 3. Test the implementation
public class Main {
        public static void main(String[] args) {

            final int GRID_SIZE = 9;

            // Create a Pig object using the interface as the reference type    
            SudokuSolver mySolver = new Solver(); 
            String filePath = "data1.csv"; // Replace with your file path

            int[][] board = new int[GRID_SIZE][GRID_SIZE];
            
            mySolver.readArry(board, filePath);
            mySolver.printBoard(board, GRID_SIZE);    
            mySolver.bruteforce(board, GRID_SIZE);
            mySolver.printBoard(board, GRID_SIZE);
    }
}