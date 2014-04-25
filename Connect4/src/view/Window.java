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

public class Window extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private JPanel mainPanel;
	private JMenuBar menuBar;
	private Connect4Panel connectPanel;
	
	public Window() {
		init();
		this.setTitle("Connect 4");
		this.pack();
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setMinimumSize(new Dimension(800,500));
		//this.setExtendedState(MAXIMIZED_BOTH);
		//this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	private void init() {
		this.mainPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		createMenu();
		
		this.connectPanel = new Connect4Panel();
		c.gridx = 0;
		c.gridy = 0;
		this.mainPanel.add(this.connectPanel,c);
		
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

}
