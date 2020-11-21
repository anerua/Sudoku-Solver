package martins.anerua;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

@SuppressWarnings("serial")
public class SudokuCell extends JLabel {

	String valids = "123456789";

	public SudokuCell(String text) {
		
		super(text);
		this.setSize(30, 30);
		this.setBorder(new LineBorder(new Color(0,0,0)));
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
	}
}
