package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import controller.Controller;

/**
 * Panel indicating the best action for the player
 */
public class HintPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private int width = 500;
	private int height = 50;
	private int hintSize = 7;
	private int[] hintValues = new int[hintSize];

	private Controller controller;


	public HintPanel(Controller controller) {
		this.controller = controller;
		this.setMinimumSize(new Dimension(this.width, 50));
		this.setPreferredSize(new Dimension(this.width, 50));
		init();
	}

	public void init() {
		
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(Color.BLACK);
		for (int i = 0; i < this.hintSize; i++) {
			g2.drawString(Integer.toString(this.hintValues[i]), this.width / 7 * i + (500 / 14), this.height / 2);
		}
	}

	public void updateHint(int[] hintValues) {
		this.hintValues = hintValues;
		this.repaint();
	}
}
