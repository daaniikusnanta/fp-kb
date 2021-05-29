package reversepacman;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class CreditPage extends Menu {

    public CreditPage() {
        JButton backButton = new JButton("back");

        backButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ReversePacman.cardLayout.show(ReversePacman.mainPanel, "title");
            }

        });

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        add(backButton, gbc);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(bg, 0, 0, this);

        String title = "Credit";

        Font fontTitle = new Font("Helvetica", Font.BOLD, 36);
        FontMetrics fmTitle = getFontMetrics(fontTitle);

        Font fontCredit = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics fmCredit = getFontMetrics(fontCredit);

        String[] credits = { "05111940000140 - Muhammad Nur Abdurrauf", 
                "05111940000163 - Daanii Nabil Ghinannafsi Kusnanta",
                "05111940000196 - Syamil Difaul Haq Sukur" };

        int textX = (ReversePacman.WIDTH - fmTitle.stringWidth(title)) / 2;
        int textY = 200;

        g.setColor(Color.WHITE);
        g.setFont(fontTitle);
        g.drawString(title, textX, textY);

        textX = 10;
        textY += fmTitle.getHeight();

        for (String credit : credits) {
            g.setFont(fontCredit);
            g.drawString(credit, textX, textY);
            textY += fmCredit.getHeight();
        }
    }
}
