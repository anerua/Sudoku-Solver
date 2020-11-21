package martins.anerua;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

public class GUILauncher {

	private JFrame frmSudokuSolver;
	private JTextPane messageField;
	private JLabel[][] cells;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUILauncher window = new GUILauncher();
					window.frmSudokuSolver.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public String[] solver(String[] board) {
		if (Stuff.isSolvable(board)) {
			Stuff stuff = new Stuff();
			String[] solution = stuff.DFS(board);
			if (solution.length == 1) {
				throw new IllegalArgumentException("Puzzle has no solution");
			}
			return solution;
		} else {
			throw new IllegalArgumentException("Puzzle either has duplicate solutions or is unsolvable");
		}
	}

	/**
	 * Create the application.
	 */
	public GUILauncher() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmSudokuSolver = new JFrame();
		frmSudokuSolver.setResizable(false);
		frmSudokuSolver.setTitle("Sudoku Solver");
		frmSudokuSolver.setBounds(100, 100, 450, 380);
		frmSudokuSolver.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frmSudokuSolver.setJMenuBar(menuBar);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmHowToUse = new JMenuItem("How to use");
		mnHelp.add(mntmHowToUse);
		
		JMenuItem mntmAbout = new JMenuItem("About");
		mnHelp.add(mntmAbout);
		frmSudokuSolver.getContentPane().setLayout(null);
		
		JPanel gridPanel = new JPanel();
		gridPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		gridPanel.setBounds(12, 36, 256, 256);
		frmSudokuSolver.getContentPane().add(gridPanel);
		gridPanel.setLayout(new GridLayout(9, 9, 0, 0));
		
		cells = new JLabel[9][9];
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				SudokuCell cell = new SudokuCell(" ");
				gridPanel.add(cell);
				cells[i][j] = cell;
			}
		}
		
		JPanel IOPanel = new JPanel();
		IOPanel.setBounds(291, 36, 135, 256);
		frmSudokuSolver.getContentPane().add(IOPanel);
		IOPanel.setLayout(null);
		
		JButton btnSolve = new JButton("Solve");
		btnSolve.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				messageField.setText("Solving...");
				new Thread(new Runnable() {
					@Override
					public void run() {
						String[] board = new String[9];
						for (int i = 0; i < 9; i++) {
							board[i] = "";
							for (int j = 0; j < 9; j++) {
								String value = cells[i][j].getText();
								board[i] += (value.equals(" ")) ? "0" : value;
							}
						}
						try {
							long startTime = System.nanoTime();
							String[] solution = solver(board);
							for (int i = 0; i < 9; i++) {
								for (int j = 0; j < 9; j++) {
									cells[i][j].setText(Character.toString(solution[i].charAt(j)));
								}
							}
							long endTime = System.nanoTime();
							float programTime = (float) (endTime - startTime) / 1000000000;
							String timeMessage = "Done. Took " + programTime + " seconds";
							messageField.setText(timeMessage);
						} catch (IllegalArgumentException e) {
							messageField.setText(e.getMessage());
						}
					}
				}).start();

			}
		});
		btnSolve.setBounds(12, 227, 117, 25);
		IOPanel.add(btnSolve);
		
		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for (int i = 0; i < 9; i++) {
					for (int j = 0; j < 9; j++) {
						cells[i][j].setText(" ");
					}
				}
				messageField.setText("");
			}
		});
		btnClear.setBounds(12, 190, 117, 25);
		IOPanel.add(btnClear);
		
		messageField = new JTextPane();
		messageField.setFont(new Font("Dialog", Font.PLAIN, 12));
		messageField.setFocusable(false);
		messageField.setEditable(false);
		messageField.setBounds(12, 12, 114, 120);
		IOPanel.add(messageField);
	}
}
