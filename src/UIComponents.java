import javax.swing.*;
import java.awt.*;

public class UIComponents {
    public static final Color BG_COLOR = new Color(250, 248, 245);
    public static final Color CARD_BG = Color.WHITE;
    public static final Color ACCENT_COLOR = new Color(193, 154, 107);
    public static final Color TEXT_DARK = new Color(60, 40, 30);
    public static final Color TEXT_LIGHT = new Color(120, 100, 90);

    public static Font getHeaderFont() { return new Font("Serif", Font.BOLD, 28); }
    public static Font getItemFont() { return new Font("SansSerif", Font.BOLD, 14); }
    public static Font getBodyFont() { return new Font("SansSerif", Font.PLAIN, 12); }
    public static Font getMonoFont() { return new Font("Monospaced", Font.PLAIN, 12); }

    public static class ModernButton extends JButton {
        public ModernButton(String text, Color bg) {
            super(text);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setBackground(bg);
            setForeground(Color.WHITE);
            setFont(new Font("SansSerif", Font.BOLD, 12));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            g2.setColor(getForeground());
            FontMetrics fm = g2.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(getText())) / 2;
            int y = (getHeight() + fm.getAscent()) / 2 - 2;
            g2.drawString(getText(), x, y);
            g2.dispose();
        }
    }

    public static class CardPanel extends JPanel {
        public CardPanel() {
            setBackground(CARD_BG);
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                    BorderFactory.createEmptyBorder(15, 15, 15, 15)
            ));
        }
    }
}