package model;

import model.players.AbstractPlayer;
import model.players.Decider;
import model.players.Player;
import observer.Observer;

public class Game {
	public final static int COMPUTER_VS_COMPUTER = 0;
	public final static int HUMAN_VS_COMPUTER = 1;
	public final static int COMPUTER_VS_HUMAN = 2;

	private AbstractPlayer[] players;
	private Board board;
	
	public Game(int mode, Observer obs){
		
		this.board = new Board();
		
		switch (mode) {
		case COMPUTER_VS_COMPUTER:
			this.players[0] = new Decider(1, this.board);
			this.players[1] = new Decider(2, this.board);
			break;
		case HUMAN_VS_COMPUTER:
			this.players[0] = new Player(1, this.board, obs);
			this.players[1] = new Decider(2, this.board);
			break;
		case COMPUTER_VS_HUMAN:
			this.players[0] = new Decider(1, this.board);
			this.players[1] = new Player(2, this.board, obs);
		}
		
		letsPlay();
	}

	private void letsPlay() {
		
	}
	
	
}
