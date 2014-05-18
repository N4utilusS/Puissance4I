package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import model.Model;
import model.players.Adviser;
import model.players.Decider;
import model.players.Player;
import model.players.RightPlayer;
import observer.Observer;
import controller.Controller;

/**
 * Main window.
 */
public class Window extends JFrame implements Observer, ActionListener, WindowListener {
	private static final long serialVersionUID = 1L;
	public final static String NEW_GAME_PLAYER_FIRST = "New Game Player First";
	public final static String NEW_GAME_COMPUTER_FIRST = "New Game Computer First";
	public final static String NEW_GAME_PLAYER_FIRST_COMPUTER_RIGHT_PLAYER = "New Game Player First (Computer right player)";
	
	private JPanel mainPanel;
	private JMenuBar menuBar;
	
	private Connect4Panel connectPanel;
	private LearnPanel learnPanel;
	private P1vsP2Panel playersPanel;
	private HintPanel hintPanel;
	
	private Controller controller;
	
	public Window(Controller controller) {
		this.controller = controller;
		init();
		this.setTitle("Connect 4");
		this.pack();
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(this);
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
		this.mainPanel.add(this.connectPanel, c);
		
		this.hintPanel = new HintPanel(this.controller);
		c.gridy = 2;
		this.mainPanel.add(this.hintPanel, c);
		
		this.playersPanel = new P1vsP2Panel(this.controller, "P1", "P2");
		c.gridy = 3;
		this.mainPanel.add(this.playersPanel, c);
		
		this.getContentPane().add(this.mainPanel);
	}
	
	private void createMenu() {
		
		menuBar = new JMenuBar();
		JMenu menu = new JMenu("Connect4");
		menuBar.add(menu);
		
		JMenuItem menuItem = new JMenuItem(NEW_GAME_PLAYER_FIRST);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Play a new game!");
		menuItem.addActionListener(this);
		menu.add(menuItem);
		
		menuItem = new JMenuItem(NEW_GAME_PLAYER_FIRST_COMPUTER_RIGHT_PLAYER);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Play a new game!");
		menuItem.addActionListener(this);
		menu.add(menuItem);
		
		menuItem = new JMenuItem(NEW_GAME_COMPUTER_FIRST);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Play a new game!");
		menuItem.addActionListener(this);
		menu.add(menuItem);
		
		menu.addSeparator();
		
		menuItem = new JMenuItem("Rules");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Rules about the game");
		menu.add(menuItem);
		
		this.setJMenuBar(this.menuBar);
	}

	@Override
	public void update(Object o) {
		if (o instanceof Player) {
			Player p = (Player) o;
			this.hintPanel.setBackground(Color.BLACK);
			this.connectPanel.updateTable(p.getState());
			if (p.getStatus() != Player.PLAYING) {
				String message;
				if (p.getStatus() == Player.WON) {
					message = "YOU WIN!";
				} else if (p.getStatus() == Player.LOST) {
					message = "YOU LOOSE!";
				} else {
					message = "NOBODY WINS!";
				}
				
				JOptionPane.showMessageDialog(this,
					    message,
					    "GameOver",
					    JOptionPane.INFORMATION_MESSAGE);
				this.connectPanel.removeMouseListener(this.connectPanel.getMouseListeners()[0]);
			}
		} else if (o instanceof Adviser) {
			this.hintPanel.setBackground(Color.GREEN);
			this.hintPanel.updateHint(((Adviser) o).getValues());
			//this.hintPanel.repaint();
			
		} else if (o instanceof Decider) {
			Decider d = (Decider) o;
			this.connectPanel.updateTable(d.getState());
			this.hintPanel.setBackground(Color.RED);
			this.hintPanel.updateHint(d.getValues());
			//this.hintPanel.repaint();
			
		} else if (o instanceof RightPlayer) {
			RightPlayer r = (RightPlayer) o;
			this.connectPanel.updateTable(r.getState());
			this.hintPanel.setBackground(Color.RED);
			this.hintPanel.updateHint(r.getValues());
			//this.hintPanel.repaint();

		} else if (o instanceof Model) {
			this.learnPanel.setNbOfGamesPlayed(((Model) o).getGamesPlayed());
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.controller.checkActionOnWindow(e.getActionCommand());
	}

	public void addListeners() {
		this.connectPanel.addMouseListener(this.connectPanel);
	}

	public void removeListeners() {
		if (this.connectPanel.getMouseListeners() != null) {
			for (MouseListener l: this.connectPanel.getMouseListeners()) {
				this.connectPanel.removeMouseListener(l);
			}
		}
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		System.exit(0);
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		this.controller.saveToDB();
		this.dispose();
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
