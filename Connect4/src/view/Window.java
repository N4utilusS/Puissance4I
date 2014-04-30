package view;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

import model.players.Adviser;
import model.players.Player;
import observer.Observer;
import controller.Controller;

/**
 * Main window.
 */
public class Window extends JFrame implements Observer {
	private static final long serialVersionUID = 1L;
	
	private JPanel mainPanel;
	private JMenuBar menuBar;
	
	private Connect4Panel connectPanel;
	private LearnPanel learnPanel;
	private P1vsP2Panel playersPanel;
	
	private Controller controller;
	
	private int[][] state = new int[6][7];
	private int[] hint = new int[7];
	
	public Window(Controller controller) {
		this.controller = controller;
		init();
		this.setTitle("Connect 4");
		this.pack();
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setMinimumSize(new Dimension(700,500));
		//this.setExtendedState(MAXIMIZED_BOTH);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	private void init() {
		this.mainPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		createMenu();
		
		this.learnPanel = new LearnPanel(this.controller);
		c.gridx=0;
		c.gridy=0;
		this.mainPanel.add(this.learnPanel);
		
		this.connectPanel = new Connect4Panel(this.controller);
		c.gridy = 1;
		this.mainPanel.add(this.connectPanel,c);
		
		this.playersPanel = new P1vsP2Panel(this.controller, "Alexis", "Gilles");
		c.gridy = 2;
		this.mainPanel.add(this.playersPanel,c);
		
		this.getContentPane().add(this.mainPanel);
	}
	
	private void createMenu() {
		
		menuBar = new JMenuBar();
		JMenu menu = new JMenu("Connect4");
		menuBar.add(menu);
		
		JMenuItem menuItem = new JMenuItem("New Game");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Play a new game!");
		menu.add(menuItem);
		
		menuItem = new JMenuItem("Save");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Save the current game!");
		menu.add(menuItem);
		
		menu.addSeparator();
		
		menuItem = new JMenuItem("Rules");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Rules about the game");
		menu.add(menuItem);
		
		this.setJMenuBar(this.menuBar);
	}

	@Override
	public void update(Object o) {
		if (o instanceof Player) {
			this.state = ((Player) o).getState();
			// TODO Change grid ?
		} else if (o instanceof Adviser) {
			this.hint = ((Adviser) o).getValues();
			// TODO Display hint values
		}
	}

}
