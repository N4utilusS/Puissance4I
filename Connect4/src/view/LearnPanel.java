package view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controller.Controller;

/**
 * Panel which will contain buttons to start the learning, stop it,... 
 * It will also display the number of games played until now by the program.
 */
public class LearnPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private JButton playButton;
	private JButton pauseButton;
	private JTextField nbOfGamesPlayed, nbOfGamesToLearn;
	
	private Controller controller;
	
	public LearnPanel(Controller controller) {
		this.controller = controller;
		init();
		this.setMinimumSize(new Dimension(500,50)); //220,110
		this.setPreferredSize(new Dimension(500,50));
		//this.setBorder(BorderFactory.createTitledBorder("Learn"));
	}
	
	public void init() {
		this.nbOfGamesPlayed=new JTextField(3);
		this.nbOfGamesPlayed.setText("0");
		//this.nbOfGamesPlayed.setEditable(false);
		JLabel label = new JLabel("Nb of Games played: ");
		label.setLabelFor(this.nbOfGamesPlayed);
		this.add(label);
		this.add(this.nbOfGamesPlayed);
		
		this.playButton = new JButton("Play");
		ImageIcon icon = new ImageIcon("pictures/play.png");

		// Need to resize the image
		Image img = icon.getImage();
		BufferedImage bi = new BufferedImage(30, 30, BufferedImage.TYPE_INT_ARGB);
		Graphics g = bi.createGraphics();
		g.drawImage(img, 0, 0, 30, 30, null);

		this.playButton.setIcon(new ImageIcon(bi));
		this.playButton.setToolTipText("Start to play against itself and learn.");
		this.playButton.addActionListener(this);
		this.playButton.setBorderPainted(false);
		this.add(this.playButton);
		
		this.nbOfGamesToLearn = new JTextField(3);
		this.add(this.nbOfGamesToLearn);
		
		this.pauseButton = new JButton("Pause");
		icon = new ImageIcon("pictures/pause.png");

		// Need to resize the image
		img = icon.getImage();
		bi = new BufferedImage(30, 30, BufferedImage.TYPE_INT_ARGB);
		g = bi.createGraphics();
		g.drawImage(img, 0, 0, 30, 30, null);

		this.pauseButton.setIcon(new ImageIcon(bi));
		this.pauseButton.setToolTipText("Stop to play against itself.");
		this.pauseButton.addActionListener(this);
		this.pauseButton.setBorderPainted(false);
		this.add(this.pauseButton);
	}
	
	public void setNbOfGamesPlayed(int nb) {
		this.nbOfGamesPlayed.setText(String.valueOf(nb));
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		this.controller.checkActionOnLearnPanel(arg0.getActionCommand());
	}

}
