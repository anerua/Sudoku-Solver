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
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.Toolkit;

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
		frmSudokuSolver.setIconImage(Toolkit.getDefaultToolkit().getImage(GUILauncher.class.getResource("/martins/anerua/icon.png")));
		frmSudokuSolver.setResizable(false);
		frmSudokuSolver.setTitle("Sudoku Solver");
		frmSudokuSolver.setBounds(100, 100, 470, 360);
		frmSudokuSolver.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frmSudokuSolver.setJMenuBar(menuBar);
		
		JMenuItem menuAbout = new JMenuItem("About");
		menuAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String text = "Sudoku Solver v1.0\n\nMIT License\nCopyright (c) 2020 Martins Anerua" 
						+ "\n\ngithub.com/anerua/Stuff"
						+ "\n\nIcon credit:\nicons8.com/set/sudoku";
				messageField.setText(text);
			}
		});
		menuBar.add(menuAbout);
		frmSudokuSolver.getContentPane().setLayout(null);
		
		JPanel gridPanel = new JPanel();
		gridPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		gridPanel.setBounds(12, 12, 256, 256);
		frmSudokuSolver.getContentPane().add(gridPanel);
		gridPanel.setLayout(new GridLayout(9, 9, 0, 0));
		
		cells = new JLabel[9][9];
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				SudokuCell cell = new SudokuCell(" ", i, j);
				gridPanel.add(cell);
				cells[i][j] = cell;
			}
		}
		
		JPanel IOPanel = new JPanel();
		IOPanel.setBounds(291, 12, 167, 256);
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
								for (int j = 0; j < 9; j++)
									cells[i][j].setText(Character.toString(solution[i].charAt(j)));
							}
							long endTime = System.nanoTime();
							float programTime = (float) (endTime - startTime) / 1000000000;
							String timeMessage = "Done.\n\nTook " + programTime + " seconds";
							messageField.setText(timeMessage);
						} catch (IllegalArgumentException e) {
							messageField.setText(e.getMessage());
						}
					}
				}).start();

			}
		});
		btnSolve.setBounds(26, 225, 117, 25);
		IOPanel.add(btnSolve);
		
		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for (int i = 0; i < 9; i++) {
					for (int j = 0; j < 9; j++)
						cells[i][j].setText(" ");
				}
				messageField.setText("");
			}
		});
		btnClear.setBounds(26, 188, 117, 25);
		IOPanel.add(btnClear);
		
		messageField = new JTextPane();
		messageField.setBorder(new TitledBorder(null, "Output", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		messageField.setOpaque(false);
		messageField.setFont(new Font("Dialog", Font.PLAIN, 11));
		messageField.setFocusable(false);
		messageField.setEditable(false);
		messageField.setBounds(0, 0, 167, 176);
		IOPanel.add(messageField);
	}
}
