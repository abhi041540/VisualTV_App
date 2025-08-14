package ott_app;
import javax.swing.*;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;

public class Positioner extends BasicSliderUI 
{

     public Positioner (JSlider slider) 
     {
        super(slider);
    }

    @Override
    public void paintThumb(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(new Color(0,120,215)); // thumb
        g2d.fillOval(thumbRect.x, thumbRect.y, thumbRect.width, thumbRect.height);
        g2d.dispose();
    }

    @Override
    public void paintTrack(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.white); // Customize the color of the track
        g2d.fillRect(trackRect.x, trackRect.y + (trackRect.height / 2) - 2, trackRect.width, 4);
        g2d.dispose();
    }
}