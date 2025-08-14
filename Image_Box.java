package ott_app;

import java.awt.Image;
import java.awt.Toolkit;


public interface Image_Box 
{

     Image LOGO=Toolkit.getDefaultToolkit().getImage(Image_Box.class.getResource("logo.PNG"));
     Image INSTRUCTIONS=Toolkit.getDefaultToolkit().getImage(Image_Box.class.getResource("instruction.png"));
     Image NEXT=Toolkit.getDefaultToolkit().getImage(Image_Box.class.getResource("next.PNG"));
     Image PREVIOUS=Toolkit.getDefaultToolkit().getImage(Image_Box.class.getResource("previous.PNG"));
     Image PLAY=Toolkit.getDefaultToolkit().getImage(Image_Box.class.getResource("play.PNG"));
     Image PAUSE=Toolkit.getDefaultToolkit().getImage(Image_Box.class.getResource("pause.PNG"));
     Image FORW=Toolkit.getDefaultToolkit().getImage(Image_Box.class.getResource("forward.PNG"));
     Image BACKW=Toolkit.getDefaultToolkit().getImage(Image_Box.class.getResource("backword.PNG"));
     Image SEARCH=Toolkit.getDefaultToolkit().getImage(Image_Box.class.getResource("search.PNG"));
     Image COLORBACKGROUND=Toolkit.getDefaultToolkit().getImage(Image_Box.class.getResource("colorbackground.png"));

}
