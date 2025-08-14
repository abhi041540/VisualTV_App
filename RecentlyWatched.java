package ott_app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;

public class RecentlyWatched extends JFrame {
    JFrame f, cf = this;
    String email;
    JPanel mp,wp;
    EmbeddedMediaPlayerComponent em;
    int m = 0;
    Map<String, String> data;
   
    public RecentlyWatched(JFrame f, String str, String email) {
        super(str);
        this.f = f;
        getContentPane().setBackground(new Color(0,0,10));
        repaint();
        
        this.email = email;
        this.data = new HashMap<>();
        this.mp = new JPanel();
        this.wp = new JPanel();
//        this.wp.setOpaque(false);
        this.wp.setBackground(new Color(0, 0, 10));
        ScheduledExecutorService sc = Executors.newScheduledThreadPool(1);

        ScheduledFuture<?> scf = sc.schedule(() -> {
        	 getContentPane().setBackground(new Color(0,0,10));
             repaint();
            loadRecentlyWatched();
        }, 10, TimeUnit.MICROSECONDS);
         Thread nt= new Thread(()->{
        	 try 
 		    {
 	            scf.get();
 	        } catch (InterruptedException | ExecutionException e) {
 	            e.printStackTrace();
 	        }
 		    cleanUpMediaPlayer();
 		    revalidate();
         	repaint();
    	  	    setUpWatchPanel();
    	        	revalidate();
            		repaint();
         },"thread-2");
        SwingUtilities.invokeLater(()->
        	{
        		setUpFrame();
        		revalidate();
        		repaint();
                em = new EmbeddedMediaPlayerComponent();
                revalidate();
        		repaint();
    		    setUpMediaPlayer();
    		    revalidate();
        		repaint();
    		    nt.start();
        	});  
    }

    private void setUpFrame() {
        cf.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        cf.setIconImage(Image_Box.LOGO);
        cf.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                cf.dispose();
                f.setVisible(true);
            }
        });
        cf.setLayout(null);
        cf.getContentPane().setBackground(new Color(0, 0, 13));
        cf.setVisible(true);
    }

    private void setUpMediaPlayer() {
        mp.setLayout(new BorderLayout());
        mp.add(em);
        mp.setBounds((cf.getWidth()/2)-35,(cf.getHeight()/2)-35, 70, 70);
        cf.add(mp);
        revalidate();
        repaint();
        em.mediaPlayer().media().play("C:\\Users\\DELL\\Downloads\\loading.mp4");
        em.mediaPlayer().events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void finished(MediaPlayer me) {
                SwingUtilities.invokeLater(() -> {
                    if (m != 1) {
                        me.media().play("C:\\Users\\DELL\\Downloads\\loading.mp4");
                    }
                });
            }
        });
    }

    private void loadRecentlyWatched() {
        try
        {
        Connection con = DataBase.getConnection();
        PreparedStatement ps = con.prepareStatement("SELECT u.name AS name, m.data AS data FROM userhistory u JOIN moviedata m ON m.name = u.name WHERE u.email = ?");
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
                if (rs.next())
                {
                    do {
                        String name = rs.getString("name");
                        byte[] bytes = rs.getBytes("data");
                        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                             ObjectInputStream ois = new ObjectInputStream(bis)) {
                            MovieObject mv = (MovieObject) ois.readObject();
                            String imageUrl = mv.getImage();
                            data.put(name, imageUrl);
                        }
                    } while (rs.next());
                } 
                else 
                {
                    System.out.println("not found");
                }
                 ps = con.prepareStatement("SELECT u.name AS name, m.data AS data FROM usershowhistory u JOIN moviedata m ON m.name = u.name WHERE u.email = ?");
                ps.setString(1, email);
                 rs = ps.executeQuery();
                    if (rs.next())
                    {
                        do {
                            String name = rs.getString("name");
                            byte[] bytes = rs.getBytes("data");
                            try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                                 ObjectInputStream ois = new ObjectInputStream(bis)) {
                                MovieObject mv = (MovieObject) ois.readObject();
                                String imageUrl = mv.getImage();
                                data.put(name, imageUrl);
                            }
                        } while (rs.next());
                    } 
                    else 
                    {
                        System.out.println("not found");
                    } 
            
        }
        catch (Exception e) {
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(cf, "Difficulty to connect with server. Try again later!");
                cf.dispose();
                f.setVisible(true);
            });
        }
    }

    private void addMoviePanel(String name, ImageIcon icon) {
        JPanel posp = new JPanel();
        JLabel nl = new JLabel(new ImageIcon(icon.getImage().getScaledInstance(200, 273, Image.SCALE_SMOOTH)));
        nl.setText(name);
        nl.setHorizontalTextPosition(SwingConstants.CENTER);
        nl.setForeground(new Color(0, 0, 0, 0));
        posp.setLayout(new BorderLayout());
        posp.add(BorderLayout.CENTER, nl);
        posp.setBorder(BorderFactory.createLineBorder(new Color(0,0,0,0), 2));
        posp.setBackground(new Color(0,0,10));
        nl.setBorder(BorderFactory.createLineBorder(new Color(125,125,125),2));
        wp.add(posp);
        revalidate();
        repaint();
        nl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                new MediaDetail(name, cf, email);
                cf.setVisible(false);
            }
        });
    }

    private void cleanUpMediaPlayer() {
        em.mediaPlayer().controls().stop();
        em.release();
        cf.getContentPane().remove(mp);
        m = 1;
    }

    private void setUpWatchPanel() {
    	if(data.size()==0)
    	{
    		SwingUtilities.invokeLater(new Runnable()
    		{
				public void run() 
				{
					f.setVisible(true);
		    		cf.dispose();
		    		JOptionPane.showMessageDialog(f,"There Is No WatchHistory Available!");
		    		f.setVisible(true);
				}
			});
    		
    	}
    	else
    	{
    		cf.setLayout(new BorderLayout());
    		  wp.setLayout(new GridLayout((data.size()/5),5,10,10));
    		  JScrollPane mjs= new JScrollPane(wp);
    		  mjs.setOpaque(false);
    	      mjs.getViewport().setBackground(new Color(0,0,0,0));
    	      JScrollBar hs=mjs.getHorizontalScrollBar();
				hs.setUI(new ScrollBar(Color.LIGHT_GRAY,new Color(0,0,10)));
				JScrollBar vs=mjs.getVerticalScrollBar();
				vs.setUI(new ScrollBar(Color.LIGHT_GRAY,new Color(0,0,10)));
  	          cf.add(mjs);
    	        revalidate();
    	        repaint();
    	        for (Map.Entry<String, String> entry : data.entrySet()) {
    	            try {
    	                URL imdb = new URL("https://www.imdb.com/");
    	                ImageIcon imc = new ImageIcon(new URL(imdb, entry.getValue()));
//    	                System.out.println(entry.getKey()+",,,,"+entry.getValue());
    	                addMoviePanel(entry.getKey(), imc);
    	            } catch (Exception e) {
    	                e.printStackTrace();
    	            }
    	        }
    	        revalidate();
    	        repaint();
    	       
    	}
       
    }
}
