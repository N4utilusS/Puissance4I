package controller;

import model.Model;

public class Controller {
	
	private Model model;

	public Controller (Model model) {
		this.model = model;
	}
	
	/**
	 * Checks the button selected on the LearnPanel.
	 * @param actionCommand the command string associated to this button
	 */
	public void checkActionOnLearnPanel(String actionCommand) {
		if ("Play".equals(actionCommand)) {
			
		} else { //else if("Pause".equals(actionCommand)) {
			
		}
	}
	
	/**
	 * Checks the chosen column.
	 * @param column the column chosen by the player (0 for the first column, 1 for the second one, ...)
	 */
	public void checkPlayerDecision(int column) {
		System.out.println(column);
	}
}
