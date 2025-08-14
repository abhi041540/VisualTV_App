package ott_app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;
public class UserLogin extends JFrame implements ActionListener 
{
JSplitPane jsp;JPanel lp,rp,vp;JLabel logo,lt,il;JButton b1,b2;
   Font fn;
	public UserLogin(String str)
	{
		super(str);
		fn= new Font("Arial",Font.BOLD,20);
		setSize(Toolkit.getDefaultToolkit().getScreenSize());
		addWindowListener(new WindowAdapter() 
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
		setIconImage(Image_Box.LOGO);
		jsp= new JSplitPane();
		jsp.setEnabled(false);
		getContentPane().setBackground(new Color(0,0,25));
		jsp.setDividerSize(0);
		lp= new JPanel();
		lp.setBackground(new Color(0,0,13));
		rp= new JPanel();
		rp.setBackground(new Color(0,0,13));
		jsp.setOrientation(JSplitPane.VERTICAL_SPLIT);
		
		jsp.setDividerLocation(90);
          
		
	 logo=new JLabel(new ImageIcon(Image_Box.LOGO.getScaledInstance(90, 65,Image.SCALE_SMOOTH)));
//	 rp.addMouseListener(new MouseAdapter()
//	{
//		 @Override
//		 public void mouseClicked(MouseEvent me)
//		 {
//			 System.out.println(me.getLocationOnScreen().x+","+me.getLocationOnScreen().y);
//		 }
//	});
	 logo.setBounds(40,15,113,69);
	  lp.add(logo);
		lp.add(new JLabel("ab"));
		b1=new JButton("Login");
		b1.setBackground(new Color(0,20,155));
		b1.setForeground(Color.white);
		b1.setBounds(1060,20,110,45);
		b1.setFont(fn);
		b1.setBorderPainted(false);
		b2=new JButton("Signup");
		b2.setBackground(Color.gray);
		b2.setForeground(Color.white);
		b2.setBounds(1197,20,150,45);
		b2.setFont(fn);
		b2.setBorderPainted(false);
		lp.add(b2);lp.add(b1);
		lp.setLayout(null);
		jsp.setLeftComponent(lp);
		rp.setLayout(null);
		vp= new JPanel();
		vp.setLayout(new BorderLayout());
		EmbeddedMediaPlayerComponent mc= new EmbeddedMediaPlayerComponent();
		vp.add(mc);
		vp.setBounds(70,104,500,600);
		rp.add(vp);
		jsp.setRightComponent(rp);
		add(jsp);
		b1.addActionListener(this);
		b2.addActionListener(this);
		lt= new JLabel("Login Now To Start Streaming");
		lt.setForeground(Color.white);
		lt.setFont(new Font("Arial",Font.BOLD,40));
		lt.setBounds(40,20, 640, 90);
		rp.add(lt);
		il= new JLabel(new ImageIcon(Image_Box.INSTRUCTIONS.getScaledInstance(650, 600,Image.SCALE_SMOOTH)));
		il.setBounds(700,30,650,600);
		rp.add(il);
		setVisible(true);
		mc.mediaPlayer().media().play("C:\\Users\\DELL\\Downloads\\intro.mp4");
		mc.mediaPlayer().events().addMediaPlayerEventListener(new MediaPlayerEventAdapter()
		{
			@Override
			public void finished(MediaPlayer mp)
			{
				SwingUtilities.invokeLater(new Runnable() {
					
					@Override
					public void run() 
					{
						try
						{
						mp.media().play("C:\\Users\\DELL\\Downloads\\intro.mp4");
						}
						catch(Exception e)
						{
//							System.out.println("yes");
						}
					}
				});
			}
		});
	}
	public static void main(String[] args)
	{
		new UserLogin("VisualTV");
	}
	@Override
	public void actionPerformed(ActionEvent e)
	{
	   JButton b= (JButton)e.getSource();
	   if(b.equals(b1))
	   {
		 new LoginDialog(this,"Login To Continue");  
	   }
	   else
	   {
		   new SignupDialog(this,"Signup To Continue");  
	   }
	}

}
