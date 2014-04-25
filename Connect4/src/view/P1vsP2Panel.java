package view;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Panel which contains the Player1 vs Player2 box. 
 * It indicates visually which player's turn is.
 */
public class P1vsP2Panel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private String namePlayer1;
	private String namePlayer2;
	private int offset=10;
	
	private JButton button1, button2;
	
	public P1vsP2Panel(String name1, String name2) {
		this.namePlayer1=name1;
		this.namePlayer2=name2;
		this.setMinimumSize(new Dimension(500,50));
		this.setPreferredSize(new Dimension(500,50));
		init();
	}
	
	public void init() {
		this.button1 = new JButton(namePlayer1);
		this.button1.setOpaque(true);
		this.button1.setBackground(Color.RED);
		//this.button1.setPreferredSize(new Dimension(100,50));
		this.button1.setBorderPainted(false);
		
		JLabel label = new JLabel("VS");
		
		this.button2 = new JButton(namePlayer2);
		this.button2.setOpaque(true);
		this.button2.setBackground(Color.YELLOW);
		this.button2.setBorderPainted(false);
		this.button2.setEnabled(false);
		
		this.add(this.button1);
		this.add(label);
		this.add(this.button2);
	}
	
	/*public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(Color.RED);
		g2.fillRoundRect(1*this.getWidth()/5, offset, this.getWidth()/5, this.getHeight()-offset, 10, 10);
		g2.setColor(Color.YELLOW);
		g2.fillRoundRect(3*this.getWidth()/5, offset, this.getWidth()/5, this.getHeight()-offset, 10, 10);
		
		g2.setColor(Color.black);
		g2.drawString(namePlayer1, this.getWidth()/5+this.getWidth()/10-(4*this.namePlayer1.length()), this.getHeight()/2+offset);
		g2.drawString("VS", 2*this.getWidth()/5+this.getWidth()/10-10, this.getHeight()/2+offset);
		g2.drawString(namePlayer2, 3*this.getWidth()/5+this.getWidth()/10-(4*this.namePlayer2.length()), this.getHeight()/2+offset);
	}*/

}
