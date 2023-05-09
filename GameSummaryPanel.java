import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Scrollable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class GameSummaryPanel extends JPanel implements Scrollable {

        public GameSummaryPanel() {
        	setAlignmentX( JPanel.LEFT_ALIGNMENT );
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.WEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1;
            gbc.weighty = 1; 

            for (int index = 0; index < 100; index++) {
                add(new JLabel("     Row " + index), gbc);
                gbc.gridy++;
            }
        }

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return new Dimension(500, 800);
        }

        @Override
        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 32;
        }

        @Override
        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 32;
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return getPreferredSize().width <= getWidth();
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            return false;
        }

    }