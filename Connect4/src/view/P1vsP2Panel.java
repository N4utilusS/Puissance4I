package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.Controller;

/**
 * Panel which contains the Player1 vs Player2 box. 
 * It indicates visually which player's turn is.
 */
public class P1vsP2Panel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	public final static String ADVISER = "Adviser";
	
	private String namePlayer1;
	private String namePlayer2;
	private int offset = 10;
	
	private JButton button1, button2, adviser;
	
	private Controller controller;
	
	public P1vsP2Panel(Controller controller, String name1, String name2) {
		this.controller = controller;
		this.namePlayer1 = name1;
		this.namePlayer2 = name2;
		this.setMinimumSize(new Dimension(500,50));
		this.setPreferredSize(new Dimension(500,50));
		init();
	}
	
	public void init() {
		this.button1 = new JButton(namePlayer1);
		this.button1.setOpaque(true);
		this.button1.setBackground(Color.YELLOW);
		//this.button1.setPreferredSize(new Dimension(100,50));
		this.button1.setBorderPainted(false);
		
		JLabel label = new JLabel("VS");
		
		this.button2 = new JButton(namePlayer2);
		this.button2.setOpaque(true);
		this.button2.setBackground(Color.RED);
		this.button2.setBorderPainted(false);
		this.button2.setEnabled(false);
		
		this.adviser = new JButton(ADVISER);
		this.adviser.setOpaque(true);
		this.adviser.setBackground(Color.GREEN);
		this.adviser.setBorderPainted(false);
		this.adviser.addActionListener(this);
		
		this.add(this.button1);
		this.add(label);
		this.add(this.button2);
		this.add(this.adviser);
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
	
	public void setPlayingPlayer(int player) {
		if (player == 1) {
			this.button1.setEnabled(true);
			this.button2.setEnabled(false);
		} else {
			this.button1.setEnabled(false);
			this.button2.setEnabled(true);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.controller.checkActionOnPlayersPanel(e.getActionCommand());
	}

}
