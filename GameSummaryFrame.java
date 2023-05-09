import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Scrollable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class GameSummaryFrame extends JFrame {
	public GameSummaryFrame (String name) {
		super(name);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JScrollPane scrollPane = new JScrollPane(new GameSummaryPanel());
        add(scrollPane);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
	}
}
