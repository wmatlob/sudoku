package net.javasudoku.java;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final int GRID_SIZE = 9;

    public static void main(String[] args) {
        String filePath = "data1.csv"; // Replace with your file path
        String[][] startArry = loadCSV(filePath);
        int[][] cArry = new int[9][9]; 
        int i,j;
        // Optional: Print the 2D array to verify the output
        i=0;
        System.out.println();
        for (String[] row : startArry) {
            j=0;
            for (String cell : row) {
                if (cell.isEmpty()) {
                    cell = "0";
                }
                if (i==0 && j==0) {
                cArry[i][j++] = Character.getNumericValue(cell.charAt(1));
                } else {
                cArry[i][j++] = Character.getNumericValue(cell.charAt(0));
                }
                System.out.print(String.valueOf(cArry[i][j-1]) + " | ");
            }
            System.out.println();
            i++;
        }

          if (solveSudoku(cArry)) {
            System.out.println("Sudoku solved successfully:");
            printBoard(cArry);
        } else {
            System.out.println("This Sudoku puzzle cannot be solved.");
        }
 
    }
    
     public static String[][] loadCSV(String filePath) {
        List<String[]> lines = new ArrayList<>();
        
        // Try-with-resources automatically closes the reader
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Split the line by commas
                String[] values = line.split(",");
                lines.add(values);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Convert the dynamic List into a fixed 2D String array
        return lines.toArray(new String[0][]);
    }

    
    /**
     * Main recursive function that solves the puzzle using backtracking.
    **/
    public static boolean solveSudoku(int[][] board) {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                // Find an empty cell
                if (board[row][col] == 0) {
                    // Try numbers 1 through 9
                    for (int numberToTry = 1; numberToTry <= 9; numberToTry++) {
                        if (isValidPlacement(board, numberToTry, row, col)) {
                            board[row][col] = numberToTry;

                            // Recursively attempt to solve the rest of the board
                            if (solveSudoku(board)) {
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
    private static boolean isValidPlacement(int[][] board, int number, int row, int col) {
        return !isNumberInRow(board, number, row) &&
               !isNumberInColumn(board, number, col) &&
               !isNumberInBox(board, number, row, col);
    }

    private static boolean isNumberInRow(int[][] board, int number, int row) {
        for (int i = 0; i < GRID_SIZE; i++) {
            if (board[row][i] == number) {
                return true;
            }
        }
        return false;
    }

    private static boolean isNumberInColumn(int[][] board, int number, int col) {
        for (int i = 0; i < GRID_SIZE; i++) {
            if (board[i][col] == number) {
                return true;
            }
        }
        return false;
    }

    private static boolean isNumberInBox(int[][] board, int number, int row, int col) {
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

    private static void printBoard(int[][] board) {
        for (int row = 0; row < GRID_SIZE; row++) {
            if (row % 3 == 0 && row != 0) {
                System.out.println("---------------------");
            }
            for (int col = 0; col < GRID_SIZE; col++) {
                if (col % 3 == 0 && col != 0) {
                    System.out.print("| ");
                }
                System.out.print(board[row][col] + " ");
            }
            System.out.println();
        }
    }
}