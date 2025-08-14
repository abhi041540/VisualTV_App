package ott_app;
import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class ScrollBar extends BasicScrollBarUI
{
    private final Color thumbColor;
    private final Color trackColor;

    public  ScrollBar(Color thumbColor, Color trackColor)
    {
        this.thumbColor = thumbColor;
        this.trackColor = trackColor;
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds)
    {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(thumbColor);
        g2.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, 0, 0);
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) 
    {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(trackColor);
        g2.fillRoundRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height, 0, 0);
    }
    @Override
    protected JButton createDecreaseButton(int orientation) 
    {
        JButton button = super.createDecreaseButton(orientation);
        button.setBackground(trackColor);
        button.setBorderPainted(false);
        button.setForeground(Color.white);
        return button;
    }

    @Override
    protected JButton createIncreaseButton(int orientation) 
    {
        JButton button = super.createIncreaseButton(orientation);
        button.setBorderPainted(false);
        button.setBackground(trackColor);
        button.setForeground(Color.white);
        return button;
    }
}