import javax.swing.JFrame;

public class KingdomBuilder extends JFrame {
	public static int WIDTH = 1620, HEIGHT = 1005;
	
	public KingdomBuilder(String title) {
		super(title);
		setSize(WIDTH, HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(new KingdomBuilderPanel());
		setVisible(true);
	}
}
