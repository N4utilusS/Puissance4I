package controller;

import view.P1vsP2Panel;
import view.Window;
import model.Game;
import model.Model;

public class Controller {
	
	private Model model;
	private Window view;

	public Controller (Model model) {
		this.model = model;
	}
	
	/**
	 * Checks the button selected on the LearnPanel.
	 * @param actionCommand the command string associated to this button
	 */
	public void checkActionOnLearnPanel(String actionCommand) {
		if ("Play".equals(actionCommand)) {
			this.model.startLearning(-1);
		} else { //else if("Pause".equals(actionCommand)) {			
			this.model.stopLearning();
			
		}
	}
	
	/**
	 * Checks the chosen column.
	 * @param column the column chosen by the player (0 for the first column, 1 for the second one, ...)
	 */
	public void checkPlayerDecision(int column) {
		int temp = Math.min(column, 6);
		temp = Math.max(0, temp);
		
		this.model.humanPlayerPlayed(temp);
	}

	public void checkActionOnWindow(String actionCommand) {
		if (Window.NEW_GAME_PLAYER_FIRST.equals(actionCommand)) {
			this.view.removeListeners();
			this.view.addListeners();
			this.model.startGame(Game.HUMAN_VS_COMPUTER);
		} else if (Window.NEW_GAME_COMPUTER_FIRST.equals(actionCommand)) {
			this.view.removeListeners();
			this.view.addListeners();
			this.model.startGame(Game.COMPUTER_VS_HUMAN);
		}
	}

	public void setView(Window window) {
		this.view = window;
	}

	public void checkActionOnPlayersPanel(String actionCommand) {
		if (P1vsP2Panel.ADVISER.equals(actionCommand)) {
			this.model.getAdvise();
		}
	}

	public void saveToDB() {
		this.model.saveDatabase();
	}
}
