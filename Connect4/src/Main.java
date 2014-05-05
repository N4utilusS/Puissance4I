import controller.Controller;
import model.Model;
import view.Window;

/**
 * Connect4 - Reinforcement Learning.
 * @author Anthony Debruyn, Gilles Degols, Brian Delhaisse and Alexis Lefebvre.
 */
public class Main {
	
	public static void main(String[] args) {
		Model model = new Model();
		Controller controller = new Controller(model);
		Window window = new Window(controller);
		controller.setView(window);
		model.addObserver(window);
	}
}
