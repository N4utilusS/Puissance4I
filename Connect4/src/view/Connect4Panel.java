package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

/**
 * Panel which draws the game.
 */
public class Connect4Panel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private int width=500;
	private int height=400;
	
	private int nRows=6;
	private int nColumns=7;
	private int table[][] = new int[nRows][nColumns];

	public Connect4Panel() {
		this.setMinimumSize(new Dimension(width,height));
		this.setPreferredSize(new Dimension(width,height));
		init();
	}
	
	private void init() {
		for(int i=0; i<nRows; i++)
			for(int j=0; j<nColumns; j++)
				table[i][j]=0;
		//TODOÊremove the 2 following lines.
		table[5][6]=1;
		table[5][0]=2;
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		//Background
		g2.setColor(Color.BLUE);
		g2.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		//Grid
		for(int i=0; i<nRows; i++) {
			for(int j=0; j<nColumns; j++) { 
				if(table[i][j]==0) {
					//Empty holes
					g2.setColor(new Color(237,237,237));
					g2.fillOval(this.getWidth()/nColumns*j, this.getHeight()/nRows*i, this.getWidth()/nColumns, this.getHeight()/nRows);
				} else if(table[i][j]==1) {
					//Yellow discs
					g2.setColor(Color.YELLOW);
					g2.fillOval(this.getWidth()/nColumns*j, this.getHeight()/nRows*i, this.getWidth()/nColumns, this.getHeight()/nRows);
				} else {
					//Red discs
					g2.setColor(Color.RED);
					g2.fillOval(this.getWidth()/nColumns*j, this.getHeight()/nRows*i, this.getWidth()/nColumns, this.getHeight()/nRows);	
				}
			}
		}
		
	}

}
