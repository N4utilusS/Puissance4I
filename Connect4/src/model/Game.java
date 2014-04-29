package model;

import model.players.AbstractPlayer;
import model.players.Decider;

public class Game {
	public final static int COMPUTER_VS_COMPUTER = 0;
	public final static int HUMAN_VS_COMPUTER = 1;
	public final static int COMPUTER_VS_HUMAN = 2;

	private AbstractPlayer[] players;
	
	public Game(int mode){
		switch (mode) {
		case COMPUTER_VS_COMPUTER:
			this.players[0] = new Decider();
		}
	}
}
