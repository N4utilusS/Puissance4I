import model.Model;

/**
 * Connect4 - Reinforcement Learning.
 * @author Anthony Debruyn, Gilles Degols, Brian Delhaisse and Alexis Lefebvre.
 */
public class Main {
	
	public static void main(String[] args) {
		Model model = new Model();
		//Controller controller = new Controller(model);
		model.startLearning(Integer.parseInt(args[0]));
	}
}
