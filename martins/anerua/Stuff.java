package martins.anerua;

public class Stuff {

	public String printBoard(String[] board) {
		String output = "";
		for (String row : board) {
			for (int i = 0; i < row.length(); i++) {
				output += row.charAt(i) + " " + "|";
			}
			output += "\n---------------------------\n";
		}
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
		if (((row.split(focus).length) - 1) > 1) {
			return false;
		}
		
		// check column
		String col = colString(board, focusCol);
		focus = Character.toString(col.charAt(focusRow));
		if (((col.split(focus).length) - 1) > 1) {
			return false;
		}
		
		// check box
		String box = boxString(board, focusRow, focusCol);
		int boxIndex = (((focusRow % 3) + 1) * 3) + (focusCol % 3);
		focus = Character.toString(box.charAt(boxIndex));
		if (((box.split(focus).length) - 1) > 1) {
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
		
		String[] candidates = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
		for (String candidate : candidates) {
			String[] tempBoard = board.clone();
			tempBoard[focusRow] = writeCell(tempBoard[focusRow], candidate, focusCol);
			if (checkConstraint(tempBoard, focusRow, focusCol)) {
				String[] newBoard = DFS(tempBoard);
				if (newBoard.length == 1) {
					continue;
				} else {
					return newBoard;
				}
			}
		}
		
		return new String[] {"0"};
	}
	
	public static void main(String[] args) {
		String[] test1 = {"001900070",
				"780001090",
				"004030805",
				"060000009",
				"008020100",
				"100000060",
				"906040200",
				"030200041",
				"040008500"
		};
		
		long startTime = System.nanoTime();
		Stuff stuff = new Stuff();
		String[] solution = stuff.DFS(test1);
		long endTime = System.nanoTime();
		long programTime = (endTime - startTime) / 1000000000;
		System.out.println("Solution:");
		System.out.println(stuff.printBoard(solution));
		System.out.println("Program took: " + programTime + " seconds.");
		
	}
	
}
