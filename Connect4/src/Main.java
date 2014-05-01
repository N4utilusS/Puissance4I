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
		model.addObserver(window);
		
		for (int i = 0; i < 10000; ++i)
			model.startGame();	// TODO REMOVE
	}
}
