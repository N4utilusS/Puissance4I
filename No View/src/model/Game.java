package model;

import model.players.AbstractPlayer;
import model.players.Decider;
import model.players.Player;
import observer.Observer;

/**
 * Represents a game, with 2 players playing against each other.
 *
 */
public class Game {
	public final static int COMPUTER_VS_COMPUTER = 0;
	public final static int HUMAN_VS_COMPUTER = 1;
	public final static int COMPUTER_VS_HUMAN = 2;

	private AbstractPlayer[] players;
	private Board board;
	private byte turn = 0;
	
	/**
	 * Creates a new game, but does not start it.
	 * @param mode The nature of the players.
	 * @param obs The observer of the game, here the view.
	 */
	public Game(int mode, Observer obs){
		
		this.board = new Board();
		this.players = new AbstractPlayer[2];
		
		switch (mode) {
		case COMPUTER_VS_COMPUTER:
			this.players[0] = new Decider(1, this.board, obs);
			this.players[1] = new Decider(2, this.board, obs);
			break;
		case HUMAN_VS_COMPUTER:
			this.players[0] = new Player(1, this.board, obs);
			this.players[1] = new Decider(2, this.board, obs);
			break;
		case COMPUTER_VS_HUMAN:
			this.players[0] = new Decider(1, this.board, obs);
			this.players[1] = new Player(2, this.board, obs);
		}
		
	}

	/**
	 * Starts/restarts the game.
	 */
	public void letsPlay() {
		
		while (!board.gameOver() && !board.isFull()) {
			players[turn].play();
			
			if (players[turn] instanceof Player)
				return;
			
			turn++;
			if (turn == 2)
				turn = 0;
		}
		
		// End of game:
		
		if (board.isFull()) {
			players[0].filled();
			players[1].filled();
			
		} else {
			// Looser is "turn":
			players[turn].looses();
			
			// The other is the winner:
			turn++;
			if (turn == 2)
				turn = 0;
			players[turn].wins();
		}
	}
	
	/**
	 * Called by the view after the human player has played,
	 * so his action is taken into account, and the game can continue by calling letsPlay() automatically.
	 * @param column
	 * @return
	 */
	public boolean humanPlayerPlayed(int column) {
		if (this.board.getHeights()[column] >= Board.HEIGHT)
			return false;
		
		Player p = (Player) players[turn];
		p.setAction(column);
		
		turn++;
		if (turn == 2)
			turn = 0;
		
		letsPlay();
		
		return true;
	}
	
	
}
