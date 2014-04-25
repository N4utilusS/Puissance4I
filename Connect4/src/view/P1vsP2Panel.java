package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

/**
 * Panel which contains the Player1 vs Player2 box. 
 * It indicates visually which player's turn is.
 */
public class P1vsP2Panel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private String namePlayer1="Alexis";
	private String namePlayer2="Gilles";
	private int offset=10;
	
	public P1vsP2Panel() {
		this.setMinimumSize(new Dimension(500,50));
		this.setPreferredSize(new Dimension(500,50));
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(Color.RED);
		g2.fillRoundRect(1*this.getWidth()/5, offset, this.getWidth()/5, this.getHeight()-offset, 10, 10);
		g2.setColor(Color.YELLOW);
		g2.fillRoundRect(3*this.getWidth()/5, offset, this.getWidth()/5, this.getHeight()-offset, 10, 10);
		
		g2.setColor(Color.black);
		g2.drawString(namePlayer1, this.getWidth()/5+this.getWidth()/10-(4*this.namePlayer1.length()), this.getHeight()/2+offset);
		g2.drawString("VS", 2*this.getWidth()/5+this.getWidth()/10-10, this.getHeight()/2+offset);
		g2.drawString(namePlayer2, 3*this.getWidth()/5+this.getWidth()/10-(4*this.namePlayer2.length()), this.getHeight()/2+offset);
	}

}
