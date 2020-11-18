package martins.anerua;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

//======================================================================
// Author:      Martins Anerua
// Created:     17 November 2020 17:45
// Copyright:   MIT License
// Description: Sudoku Solver
//=======================================================================

public class Stuff {

	public String printBoard(String[] board) {
		if (board.length == 1) {
			return "Puzzle has no solution";
		}
		String output = ".=============================.\n";
		for (int i = 0; i < board.length; i++) {
			output += "| ";
			for (int j = 0; j < board[i].length(); j++) {
				output += board[i].charAt(j) + " ";
				output += (j%3 == 2) ? "| " : " ";
			}
			output += (i%3 == 2 && i != 8) ? "\n: ------- + ------- + ------- :\n" : "\n";
		}
		output += "*=============================*";
		return output;
	}

	private String boxString(String[] board, int focusRow, int focusCol) {
		String box = "";
		int initRow = (focusRow / 3) * 3;
		int initCol = (focusCol / 3) * 3;
		for (int i = initRow; i < initRow + 3; i++) {
			for (int j = initCol; j < initCol + 3; j++) {
				box += board[i].charAt(j);
			}
		}
		return box;
	}

	private String colString(String[] board, int focusCol) {
		String col = "";
		for (String row : board) {
			col += row.charAt(focusCol);
		}
		return col;
	}

	private boolean checkConstraint(String[] board, int focusRow, int focusCol) {
		// check row
		String row = board[focusRow];
		String focus = Character.toString(row.charAt(focusCol));
		int rowCount = 0;
		for (int i = 0; i < row.length(); i++) {
			if (focus.equals(Character.toString(row.charAt(i))))
				++rowCount;
		}
		if (rowCount > 1) {
			return false;
		}

		// check column
		String col = colString(board, focusCol);
		int colCount = 0;
		for (int i = 0; i < col.length(); i++) {
			if (focus.equals(Character.toString(col.charAt(i))))
				++colCount;
		}
		if (colCount > 1) {
			return false;
		}

		// check box
		String box = boxString(board, focusRow, focusCol);
		int boxCount = 0;
		for (int i = 0; i < box.length(); i++) {
			if (focus.equals(Character.toString(box.charAt(i))))
				++boxCount;
		}
		if (boxCount > 1) {
			return false;
		}

		return true;
	}

	private String writeCell(String row, String candidate, int focusCol) {
		String newRow = "";
		for (int i = 0; i < row.length(); i++) {
			newRow += (i == focusCol) ? candidate : row.charAt(i);
		}
		return newRow;
	}

	public String[] DFS(String[] board) {
		int focusRow = 81, focusCol = 81;

		for (int i = 0; i < 9; i++) {
			if (board[i].contains("0")) {
				focusRow = i;
				focusCol = board[i].indexOf("0");
				break;
			}
		}

		if (focusRow == 81 && focusCol == 81) {
			return board; // puzzle is solved
		}

		String[] candidates = { "1", "2", "3", "4", "5", "6", "7", "8", "9" };
		for (String candidate : candidates) {
			String[] tempBoard = board.clone();
			tempBoard[focusRow] = writeCell(tempBoard[focusRow], candidate, focusCol);
			if (checkConstraint(tempBoard, focusRow, focusCol)) {
				String[] newBoard = DFS(tempBoard);
				if (newBoard.length != 1) {
					return newBoard;
				}
			}
		}

		return new String[] { "0" };
	}
	
	public static boolean isSolvable(String[] board) {
		Set<String> filledDigits = new HashSet<String>();
		int filledSquares = 0;
		Stuff st = new Stuff();
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length(); j++) {
				if (!Character.toString(board[i].charAt(j)).equals("0")) {
					if (!st.checkConstraint(board, i, j)) {
						return false;
					}
					++filledSquares;
					filledDigits.add(Character.toString(board[i].charAt(j)));
				}
				if (filledDigits.size() >= 8 && filledSquares >= 17) {
					return true;
				}
			}
		}
		return false;
	}
	
	private static boolean verifyInput(String inputRow) {
		if (inputRow.length() != 9) {
			return false;
		}
		String[] valids = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
		for (int i = 0; i < inputRow.length(); i++) {
			if (!Arrays.asList(valids).contains(Character.toString(inputRow.charAt(i)))) {
				return false;
			}
		}
		return true;
	}

	public static String[] getInput() {
		String[] board = new String[9];
		Scanner scanner = new Scanner(System.in);
		for (int i = 0; i < 9; i++) {
			boolean correct = false;
			while (!correct) {
				System.out.print("Row " + (i+1) + ": ");
				String inputRow = scanner.nextLine().strip();
				if (verifyInput(inputRow)) {
					board[i] = inputRow;
					correct = true;
				} else {
					System.out.println("Invalid input!");
				}
			}
		}
		scanner.close();
		return board;
	}
	
	public static void startMessage() {
		System.out.println("================================================");
		System.out.println("                 Sudoku Solver                  ");
		System.out.println("================================================");
		System.out.println();
		System.out.println("Enter the values of cells in each row.");
		System.out.println("Use 0 to represent an empty cell");
		System.out.println("Do not add space in between cell values.");
		System.out.println("A sample row with only one empty cell would be: 012345678");
		System.out.println();
	}

	public static void main(String[] args) {
		startMessage();
		String[] board = getInput();
		System.out.println("Verifying...");
		if (isSolvable(board)) {
			System.out.println("Solving...");
			long startTime = System.nanoTime();
			Stuff stuff = new Stuff();
			String[] solution = stuff.DFS(board);
			long endTime = System.nanoTime();
			float programTime = (float) (endTime - startTime) / 1000000000;
			System.out.println("Solution:");
			System.out.println(stuff.printBoard(solution));
			System.out.println("Program took: " + programTime + " seconds.");
		} else {
			System.out.println("Puzzle either has duplicate solutions or is unsolvable");
		}

	}

}
