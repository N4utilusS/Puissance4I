package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class Connect4Panel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private int nRows=6;
	private int nColumns=7;

	public Connect4Panel() {
		this.setMinimumSize(new Dimension(500,400));
		this.setPreferredSize(new Dimension(500,400));
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		//Background
		g2.setColor(Color.BLUE);
		g2.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		//Grid with the empty holes
		g2.setColor(new Color(237,237,237));
		for(int i=0; i<nRows; i++) {
			for(int j=0; j<nColumns; j++) { 
				g2.fillOval(this.getWidth()/nColumns*j, this.getHeight()/nRows*i, this.getWidth()/nColumns, this.getHeight()/nRows);
			}
		}
		
		//Yellow discs
		g2.setColor(Color.YELLOW);
		
		//Red discs
		g2.setColor(Color.RED);
	}

}
