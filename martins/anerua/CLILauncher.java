package martins.anerua;

import java.util.Arrays;
import java.util.Scanner;

public class CLILauncher {

	public static String printBoard(String[] board) {
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
		if (Stuff.isSolvable(board)) {
			System.out.println("Solving...");
			long startTime = System.nanoTime();
			Stuff stuff = new Stuff();
			String[] solution = stuff.DFS(board);
			long endTime = System.nanoTime();
			float programTime = (float) (endTime - startTime) / 1000000000;
			System.out.println("Solution:");
			System.out.println(printBoard(solution));
			System.out.println("Program took: " + programTime + " seconds.");
		} else {
			System.out.println("Puzzle either has duplicate solutions or is unsolvable");
		}

	}

}
