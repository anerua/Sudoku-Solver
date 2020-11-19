package martins.anerua;

import java.util.HashSet;
import java.util.Set;

//======================================================================
// Author:      Martins Anerua
// Created:     17 November 2020 17:45
// Copyright:   MIT License
// Description: Sudoku Solver
//=======================================================================

public class Stuff {
	
	float boxST = 0, colST = 0, checkCT = 0, writeCT = 0;

	private String boxString(String[] board, int focusRow, int focusCol) {
		long startT = System.nanoTime();
		String box = "";
		int initRow = (focusRow / 3) * 3;
		int initCol = (focusCol / 3) * 3;
		for (int i = initRow; i < initRow + 3; i++) {
			for (int j = initCol; j < initCol + 3; j++)
				box += board[i].charAt(j);
		}
		long endT = System.nanoTime();
		boxST += (float) (endT - startT) / 1000000000;
		return box;
	}

	private String colString(String[] board, int focusCol) {
		long startT = System.nanoTime();
		String col = "";
		for (String row : board)
			col += row.charAt(focusCol);
		long endT = System.nanoTime();
		colST += (float) (endT - startT) / 1000000000;
		return col;
	}
	
	private boolean checkConstraint(String[] board, String focus, int focusRow, int focusCol) {
		long startT = System.nanoTime();
		// check row
		String row = board[focusRow];
		if (row.contains(focus)) {
			long endT = System.nanoTime();
			checkCT += (float) (endT - startT) / 1000000000;
			return false;
		}
		// check column
		String col = colString(board, focusCol);
		if (col.contains(focus)) {
			long endT = System.nanoTime();
			checkCT += (float) (endT - startT) / 1000000000;
			return false;
		}
		// check box
		String box = boxString(board, focusRow, focusCol);
		if (box.contains(focus)) {
			long endT = System.nanoTime();
			checkCT += (float) (endT - startT) / 1000000000;
			return false;
		}
		return true; // puzzle satisfies constraints
	}

	private String writeCell(String row, String candidate, int focusCol) {
		long startT = System.nanoTime();
		String newRow = "";
		for (int i = 0; i < row.length(); i++)
			newRow += (i == focusCol) ? candidate : row.charAt(i);
		long endT = System.nanoTime();
		writeCT += (float) (endT - startT) / 1000000000; 
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
			if (checkConstraint(board, candidate, focusRow, focusCol)) {
				String[] tempBoard = board.clone();
				tempBoard[focusRow] = writeCell(tempBoard[focusRow], candidate, focusCol);
				String[] newBoard = DFS(tempBoard);
				if (newBoard.length != 1)
					return newBoard;
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
				String focus = Character.toString(board[i].charAt(j));
				if (!focus.equals("0")) {
					if (!st.checkConstraint(board, focus, i, j)) {
						return false;
					}
					++filledSquares;
					filledDigits.add(Character.toString(board[i].charAt(j)));
				}
				if (filledDigits.size() >= 8 && filledSquares >= 17)
					return true;
			}
		}
		return false;
	}

}
