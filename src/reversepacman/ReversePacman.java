package reversepacman;

import java.awt.CardLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ReversePacman extends JFrame {

	public static final int WIDTH = Level.TILESIZE*23;
    public static final int HEIGHT = Level.TILESIZE*32;
    public static CardLayout cardLayout;
    public static JPanel mainPanel;

    public ReversePacman() {
        
        initUI();
    }
    
    private void initUI() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

//        mainPanel.add(new MainMenu(), "title");
        mainPanel.add(new Level(), "level");
//        mainPanel.add(new CreditPage(), "credit");
        
        add(mainPanel);
        
        setResizable(false);
        pack();
        
        setTitle("Reverse Pacman");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
        	ReversePacman ex = new ReversePacman();
            ex.setVisible(true);
        });
    }

}
