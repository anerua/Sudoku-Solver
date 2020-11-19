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
		
		String[] test1 = { "001900070", "780001090", "004030805", "060000009", "008020100", "100000060", "906040200",
				"030200041", "040008500" }; // very easy
		String[] test2 = { "005300008", "000000200", "681200030", "504030600", "070420003", "902010700", "197800050",
				"000000400", "003100007" }; // very hard ubuntu
		String[] test3 = { "010000030", "900020100", "000100064", "700000000", "800390506", "000000049", "500071000",
				"008000091", "040260005" }; // very hard clean sudoku
		String[] test4 = { "910000050", "003009021", "000402000", "080040902", "000070000", "504060010", "000506000",
				"250700800", "030000095" }; // fiendish Sudoku ProX
		String[] test5 = { "800000000", "003600000", "070090200", "050007000", "000045700", "000100030", "001000068",
				"008500010", "090000400" }; // hardest Arto Inkala 1
		String[] test6 = { "850002400", "720000009", "004000000", "000107002", "305000900", "040000000", "000080070",
				"017000000", "000036040" }; // hardest Arto Inkala 2006 from Peter Norvig
		String[] test7 = { "005300000", "800000020", "070010500", "400005300", "010070006", "003200080", "060500009",
				"004000030", "000009700" }; // hardest Arto Inkala 2010 from Peter Norvig
		String[] test10 = { "000003540", "010060002", "309002007", "240030000", "006000300", "000010024", "700300406",
				"500020090", "031500000" }; // Solo 3x3 Unreasonable
		
		startMessage();
//		String[] board = getInput();
		String[] board = test10;
		System.out.println("Verifying...");
		if (!Stuff.isSolvable(board)) {
			System.out.println("Solving...");
			long startTime = System.nanoTime();
			Stuff stuff = new Stuff();
			String[] solution = stuff.DFS(board);
			
			float boxST = stuff.boxST, 
					colST = stuff.colST,
					checkCT = stuff.checkCT,
					writeCT = stuff.writeCT;
			
			long endTime = System.nanoTime();
			float programTime = (float) (endTime - startTime) / 1000000000;
			System.out.println("Solution:");
			System.out.println(printBoard(solution));
			System.out.println("Program took:    " + programTime + " seconds.");
			System.out.println("boxString:       " + boxST + " seconds;  " + (int)((boxST/programTime) * 100) + "%");
			System.out.println("colString:       " + colST + " seconds;  " + (int)((colST/programTime) * 100) + "%");
			System.out.println("checkConstraint: " + checkCT + " seconds;  " + (int)((checkCT/programTime) * 100) + "%");
			System.out.println("writeCell:       " + writeCT + " seconds;  " + (int)((writeCT/programTime) * 100) + "%");
	
		} else {
			System.out.println("Puzzle either has duplicate solutions or is unsolvable");
		}

	}

}
