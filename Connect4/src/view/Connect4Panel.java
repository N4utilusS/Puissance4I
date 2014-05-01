package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import controller.Controller;

/**
 * Panel which draws the game.
 */
public class Connect4Panel extends JPanel implements MouseListener {
	private static final long serialVersionUID = 1L;
	
	private int width=500;
	private int height=400;
	
	private int nRows=6;
	private int nColumns=7;
	private int table[][] = new int[nColumns][nRows];
	
	private Controller controller;

	public Connect4Panel(Controller controller) {
		this.controller = controller;
		this.setMinimumSize(new Dimension(width,height));
		this.setPreferredSize(new Dimension(width,height));
		init();
	}
	
	private void init() {
		for(int i=0; i<nColumns; i++)
			for(int j=0; j<nRows; j++)
				table[i][j]=0;
		//TODO�remove the 2 following lines.
		//table[6][5]=1;
		//table[0][5]=2;
		
		this.addMouseListener(this);
	}
	
	public void updateTable(int[][] table) {
		this.table = table;
		this.repaint();
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		//Background
		g2.setColor(Color.BLUE);
		g2.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		//Grid
		for(int i=0; i<nColumns; i++) {
			for(int j=0; j<nRows; j++) { 
				if(table[i][j]==0) {
					//Empty holes
					g2.setColor(new Color(237,237,237));
					g2.fillOval(this.getWidth()/nColumns*i, this.getHeight() - this.getHeight()/nRows*(j+1), this.getWidth()/nColumns, this.getHeight()/nRows);
				} else if(table[i][j]==1) {
					//Yellow discs
					g2.setColor(Color.YELLOW);
					g2.fillOval(this.getWidth()/nColumns*i, this.getHeight() - this.getHeight()/nRows*(j+1), this.getWidth()/nColumns, this.getHeight()/nRows);
				} else {
					//Red discs
					g2.setColor(Color.RED);
					g2.fillOval(this.getWidth()/nColumns*i, this.getHeight() - this.getHeight()/nRows*(j+1), this.getWidth()/nColumns, this.getHeight()/nRows);	
				}
			}
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		this.controller.checkPlayerDecision(arg0.getX() / (this.width / 7));
	}

}
