import javax.swing.JFrame;

public class Main {
	
	//Rock Paper Scissor CA
	public static void main(String[] args) {
		JFrame frame = new JFrame("Rock Paper Scissor CA");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		
		Panel p = new Panel();
		
		frame.add(p);
		frame.pack();
		frame.setLocationRelativeTo(null);

		frame.setVisible(true);
		p.update();
	}
}
