package martins.anerua;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;

@SuppressWarnings("serial")
public class SudokuCell extends JLabel {

	String valids = "123456789";

	public SudokuCell(String text, int row, int col) {
		
		super(text);
		int left = 0, right = 0, top = 0, bottom = 0;
		switch (row) {
		case 0:
			top = 3;
			bottom = 1;
			break;
		case 1, 4, 7:
			top = bottom = 1;
			break;
		case 2, 5:
			top = 1;
			bottom = 2;
			break;
		case 3, 6:
			top = 2;
			bottom = 1;
			break;
		case 8:
			top = 1;
			bottom = 3;
			break;
		}
		
		switch (col) {
		case 0:
			left = 3;
			right = 1;
			break;
		case 1, 4, 7:
			left = right = 1;
			break;
		case 2, 5:
			left = 1;
			right = 2;
			break;
		case 3, 6:
			left = 2;
			right = 1;
			break;
		case 8:
			left = 1;
			right = 3;
			break;
		}
		this.setFont(new Font("Dialog", Font.BOLD, 16));
		this.setBorder(new MatteBorder(top, left, bottom, right, (Color) new Color(0,0,0)));
		this.setText(text);
		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.setFocusable(true);
		this.setRequestFocusEnabled(true);
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				requestFocus();
			}
		});
		this.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				String text = KeyEvent.getKeyText(e.getKeyCode());
				if (valids.contains(text)) {
					setText(text);
				} else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_SPACE) {
					setText(" ");
				}
			}
		});
		this.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) {
				setOpaque(true);
				setBackground(new Color(211,211,211));
				repaint();
				
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				setOpaque(false);
				repaint();
				
			}
			
		});
	}
}
