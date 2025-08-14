package ott_app;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.Buffer;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.Position;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaListPlayerComponent;

public class HomeWindow extends JFrame  implements Runnable,ActionListener
{
    JFrame f = this, loginwindow;
    String email;
     
     ImageIcon logo;
    private static JLabel logoi,developer;
    JPanel imagpan;
    ScheduledExecutorService scheduler;
    Font fn;
    JLabel lu1;
    URL web,imdb;
    Date de;
    String year;
    JPanel hp1, hp2, hp3;
    CardLayout hc1, hc2, hc3;
    JPanel loc;
    JPanel wp,midp;
    JScrollPane jscr;
    ScheduledFuture<?> jso;
    String postername="";
    Thread main;
    EmbeddedMediaListPlayerComponent em;
    Map<String,URL>top;
    volatile int m=0;
   
    public HomeWindow(String str, JFrame login, String email)throws MalformedURLException 
    {
        super(str);
       setLayout(null);
        imdb= new URL("https://www.imdb.com/");
        top= new LinkedHashMap<String, URL>();
        midp= new JPanel();
        midp.setBackground(new Color(0,0,0,0));
        main= new Thread(this,"main thread");
        wp = new JPanel();
        wp.setBackground(new Color(0, 0, 10));
        
        loc= new JPanel();
        loc.setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(0, 0, 13));
        web = new URL("https://en.wikipedia.org/");
        this.email = email;
        loginwindow = login;
        de = new Date();
        SimpleDateFormat spt = new SimpleDateFormat("yyyy");
        year = spt.format(de);
        scheduler = Executors.newScheduledThreadPool(5);
        hc1 = new CardLayout();
        hc2 = new CardLayout();
        hc3 = new CardLayout();
        hp1 = new JPanel();
        hp1.setOpaque(false);
        hp1.setBackground(new Color(0, 0, 0, 0));
        hp1.setLayout(hc1);
        hp2 = new JPanel();
        hp2.setOpaque(false);
        hp2.setBackground(new Color(0, 0, 0, 0));
        hp2.setLayout(hc2);
        hp3 = new JPanel();
        hp3.setOpaque(false);
        hp3.setBackground(new Color(0, 0, 0, 0));
        hp3.setLayout(hc3);
         jso=   scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                try {
                
//                 year="2024";
                    URL docu = new URL(web, "https://en.wikipedia.org/wiki/List_of_Hindi_films_of_"+year);
                    Document doc = Jsoup.connect(docu.toString()).get();
                    Element ell = doc.selectFirst("div.mw-content-ltr.mw-parser-output table.wikitable.sortable tbody");
                    Elements el = ell.select("tr");
                    for (int i = 1; i < el.size(); i++) 
                    {
                    
                        Element tr = el.get(i);
                        Element il = tr.selectFirst("td i a");
                        String imgur = il.attr("href");
                        String imgnam=il.text();
                        URL imgpag = new URL(web, imgur);
                       
                        Document imdoc = Jsoup.connect(imgpag.toString()).get();
                        Element img_url = imdoc.selectFirst("div.mw-content-ltr.mw-parser-output table tbody tr:nth-child(2) td span a img");
                        String imgurl = img_url.attr("src");
                        URL imgadd = new URL(web, imgurl);
                        top.put(imgnam, imgadd);
                        ImageIcon imi = new ImageIcon(imgadd);
                        Image imag = imi.getImage();
                        JLabel lu = new JLabel(new ImageIcon(imag.getScaledInstance(200, 280, Image.SCALE_SMOOTH)));
                        lu.setText(imgnam);
                        lu.setHorizontalTextPosition(SwingConstants.CENTER);
                        lu.setForeground(new Color(0,0,0,0));
                        lu1= new JLabel(new ImageIcon(imag.getScaledInstance(130, 160, Image.SCALE_SMOOTH)));
                        lu1.setText(imgnam);
                        lu1.setHorizontalTextPosition(SwingConstants.CENTER);
                        lu1.setForeground(new Color(0,0,0,0));
                        hp1.add(lu, "" + i);
                        hp2.add(lu1, "" + i);
                        
                        lu1.addMouseListener(new MouseAdapter() 
					     {
					    	 @Override
					    	 public void mouseClicked(MouseEvent me)
					    	 {
					    		 JLabel source=(JLabel)me.getSource();
					    		new MediaDetail(source.getText(),f,email);
					    		f.setVisible(false);
					    	 }
						});
                        lu1= new JLabel(new ImageIcon(imag.getScaledInstance(130, 160, Image.SCALE_SMOOTH)));
                        lu1.setText(imgnam);
                        lu1.setHorizontalTextPosition(SwingConstants.CENTER);
                        lu1.setForeground(new Color(0,0,0,0));
                        hp3.add(lu1, "" + i);
                        if (i == 4) 
                         {
                            	main.start();
                            	
                         }
                        lu.addMouseListener(new MouseAdapter() 
					     {
					    	 @Override
					    	 public void mouseClicked(MouseEvent me)
					    	 {
					    		 JLabel source=(JLabel)me.getSource();
					    		new MediaDetail(source.getText(),f,email);
					    		f.setVisible(false);
					    	 }
						});
                        lu1.addMouseListener(new MouseAdapter() 
					     {
					    	 @Override
					    	 public void mouseClicked(MouseEvent me)
					    	 {
					    		 JLabel source=(JLabel)me.getSource();
						    		new MediaDetail(source.getText(),f,email);
						    		f.setVisible(false);
					    	 }
						});
                    }
                } 
                catch (Exception e) 
                {
                
                    SwingUtilities.invokeLater(() -> {
                    	f.getContentPane().remove(loc);
                        JOptionPane.showMessageDialog(f, "Connection Lost Please Try Again");
                        System.exit(0);
                    });
                    e.printStackTrace();
                }
            }
        }, 0, TimeUnit.MICROSECONDS);

         scheduler.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run()
                    {
                    	if(m==1)
                    	{
                        hc1.next(hp1);
                        hc2.next(hp2);
                        hc3.next(hp3);
                        revalidate();
                        repaint();
                    	}
                    }
                });
            }
        }, 22, 10, TimeUnit.SECONDS);
    ScheduledFuture<?> imd=   scheduler.schedule(new Runnable() 
       {
		@Override
		public void run() 
		{
			int count=0;
			try
			{
				Document doc=Jsoup.connect("https://editorial.rottentomatoes.com/guide/popular-tv-shows/").get();

				Element e1= doc.selectFirst("div.panel-rt.panel-box.article_body div.articleContentBody");
				Elements li=e1.select("div.row.countdown-item");
				for(Element el:li)
				{
					try
					{
						count+=1;
						Element at= el.selectFirst("div a div img");
						String poser=at.attr("src");
					    Element lin=el.selectFirst("div.countdown-item-content div.row.countdown-item-title-bar div div h2 a");
					    String name1="";
					    if(lin.text().indexOf(':')!=-1)
					    {
					     name1=lin.text().substring(0,lin.text().indexOf(':'));
					    }
					    else
					    {
					    	name1=lin.text();
					    }
					     postername=name1;	
						SwingUtilities.invokeAndWait(new Runnable() {
							
							@Override
							public void run()
							{
								 ImageIcon imc;
								try
								{
									imc = new ImageIcon(new URL(imdb,poser));
									JPanel posp=new JPanel();
								     JLabel nl=new JLabel(new ImageIcon(imc.getImage().getScaledInstance(170, 243, Image.SCALE_SMOOTH)));
								     nl.setText(postername);
								     nl.setHorizontalTextPosition(SwingConstants.CENTER);
								     nl.setForeground(new Color(0,0,0,0));
								     posp.setLayout(new BorderLayout());
								     posp.add(BorderLayout.CENTER,nl);
								     midp.add(posp);
								     nl.addMouseListener(new MouseAdapter() 
								     {
								    	 @Override
								    	 public void mouseClicked(MouseEvent me)
								    	 {
								    		 JLabel source=(JLabel)me.getSource();
									    		new MediaDetail(source.getText(),f,email);
									    		f.setVisible(false);
								    	 }
									});
//								    
								     revalidate();
								     repaint();
								    
								     
								} 
								catch (MalformedURLException e) 
								{
									e.printStackTrace();
								}
							     
							}
						});
						
					}
					catch(Exception e)
					{
						continue;
					}
					
				}
			
			} 
			catch (Exception e)
			{

                SwingUtilities.invokeLater(() -> {
                	
                    JOptionPane.showMessageDialog(f, "Connection Lost Please Try Again");
                    System.exit(0);
                });
                e.printStackTrace();
			} 
		}
	}, 0,TimeUnit.MICROSECONDS);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                fn = new Font("Arial", Font.BOLD, 20);
                f.setSize(Toolkit.getDefaultToolkit().getScreenSize());
                f.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent we)
                    {
                    	f.getContentPane().remove(loc);
                        scheduler.shutdownNow();
                        System.exit(0);
                    }
                   
                });
               
                f.setIconImage(Image_Box.LOGO);
                logo = new ImageIcon(Image_Box.LOGO.getScaledInstance(90, 65, Image.SCALE_SMOOTH));
                logoi = new JLabel(logo);
                wp.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent me) {
                        if (me.getButton() == MouseEvent.BUTTON1) {
                            System.out.println(me.getX() + "," + me.getY());
                        }
                    }
                });

                loc.setBounds((f.getWidth()/2)-70,((f.getHeight()/2)-70),70, 70);
                f.add(loc);
               
                em = new EmbeddedMediaListPlayerComponent();
                loc.add(em);
                hp2.setBounds((int)((f.getWidth()/3)), 80, 130, 160);
                hp1.setBounds((int)((f.getWidth()/3)+180), 10, 230, 300);
                hp3.setBounds((int)((f.getWidth()/3)+180)+280, 80, 130, 160);
               
                logoi.setBounds(10, 10, 90, 65);
                wp.add(logoi);
                wp.setLayout(null);
                
                f.setVisible(true);
              
               
                em.mediaPlayer().media().play("C:\\Users\\DELL\\Downloads\\loading.mp4");
                em.mediaPlayer().events().addMediaPlayerEventListener(new MediaPlayerEventAdapter()
                {
                    @Override
                    public void finished(MediaPlayer me) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run()
                            {
                            	if(m!=1)
                            	{
                                me.media().play("C:\\Users\\DELL\\Downloads\\loading.mp4");
                            	}
                            }
                        });
                    }
                });
            }
        });
    }
	@Override
	public void run()
	{ 
		
		try 
		{
			Thread.sleep(1000);
		} 
		catch (InterruptedException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
           SwingUtilities.invokeLater(()->{
		
			 JLabel title= new JLabel("TOP Releases Of "+year);
             title.setForeground(Color.white);
             title.setFont(new Font("Arial",Font.BOLD ,30));
             title.setBounds((f.getWidth()/2)-120,(f.getHeight()/3)+15, 500, 60);
             developer= new JLabel("Developed by:Abhishek Jain");
             developer.setFont(new Font("Arial",Font.BOLD,10));
             developer.setForeground(Color.white);
             developer.setBounds(f.getWidth()-((((f.getWidth())/2)/2)/2), 2, 300, 15);
             
             JLabel mainlable= new JLabel("The Most Watched Shows :");
             mainlable.setFont(new Font("Arial", Font.BOLD, 30));
             mainlable.setForeground(Color.white);
             mainlable.setBounds(10, (f.getHeight()/3)+60, 500, 150);
             JScrollPane nps= new JScrollPane(midp);
             nps.setBackground(new Color(0,0,0,0));
             nps.setOpaque(false);
             nps.setBounds(34,(f.getHeight()/2)+35,f.getWidth()-105, 246);
             nps.setBorder(BorderFactory.createLineBorder(new Color(0,0,10),2));
             nps.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
             nps.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
             JButton next,previous;
             next= new JButton(new ImageIcon(Image_Box.NEXT.getScaledInstance(32,50,Image.SCALE_SMOOTH)));
             next.setBackground(new Color(0,0,0,0));
             next.setOpaque(false);
             next.setBorderPainted(false);
             previous= new JButton(new ImageIcon(Image_Box.PREVIOUS.getScaledInstance(32,50,Image.SCALE_SMOOTH)));
             previous.setBackground(new Color(0,0,0,0));
             previous.setOpaque(false);
             previous.setBorderPainted(false);
             previous.setBounds(1, (f.getHeight()/2)+123, 32, 50);
             next.setBounds(f.getWidth()-70, (f.getHeight()/2)+123, 32, 50);
             JViewport vp= nps.getViewport();
             Point ps=vp.getViewPosition();
             next.addActionListener(new ActionListener()
             {
				
				@Override
				public void actionPerformed(ActionEvent e)
				{
					Dimension vr=vp.getExtentSize();
					Dimension vd= vp.getViewSize();
				  if((ps.x+vr.width)<vd.width)
				  {
					           ps.x=ps.x+50;
							   vp.setViewPosition(ps);
							   revalidate();
							   repaint();
				  }
				}
			});
             previous.addActionListener(new ActionListener()
             {
				
				@Override
				public void actionPerformed(ActionEvent e)
				{
				
					Dimension vr=vp.getExtentSize();
				Dimension vd= vp.getViewSize();
			         if((vd.width-(vd.width-vr.width))<(ps.x+vr.width))
			         {
			        	 ps.x=ps.x-50;
							   vp.setViewPosition(ps);
							   revalidate();
							   repaint();  
			        	
			         }
				}
			});
             JPanel btp= new JPanel();
             btp.setOpaque(false);
             btp.setBackground(new Color(0,0,0,0));
             JLabel load= new JLabel("Loading.......");
             load.setForeground(new Color(125,125,125));
             load.setBounds((f.getWidth()/2)-25, (f.getHeight()-(f.getHeight()/5)), 200, 50);
             load.setFont(fn);
             JButton bollywood,hollywood,webseries,rs,tophit;
             bollywood= new JButton("Bollywood");
             bollywood.setForeground(Color.white);
             bollywood.setBackground(new Color(0,0,30));
             hollywood= new JButton("Hollywood");
             hollywood.setForeground(Color.white);
             hollywood.setBackground(new Color(0,0,30));
             webseries= new JButton("WebShows");
             webseries.setForeground(Color.white);
             webseries.setBackground(new Color(0,0,30));
             rs= new JButton("Recently Watched");
             rs.setForeground(Color.white);
             rs.setBackground(new Color(0,0,30));
             tophit= new JButton("Top Listed");
             tophit.setForeground(Color.white);
             tophit.setBackground(new Color(0,0,30));
             btp.add(tophit);
             btp.add(bollywood);
             btp.add(hollywood);
             btp.add(webseries);
             btp.add(rs);
             btp.setLayout(new GridLayout(1,5,5,5));
             btp.setBounds(15,(f.getHeight()-(((f.getHeight()/2)/2)/2))-30,f.getWidth()-50,40);
             
             m=1;
             System.out.println("abhi");
             try {
				jso.get();
			     } 
             catch (InterruptedException e1) 
             {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			 } 
             catch (ExecutionException e1) 
             {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			 }
             
             jscr = new JScrollPane(wp);
             loc.remove(em);
             f.getContentPane().remove(loc);
             revalidate();
             repaint();
             f.setLayout(new BorderLayout());
             f.add(jscr);
             revalidate();
             repaint();
             wp.add(developer);
             wp.add(title);
             hc1.show(hp1, "2");
             hc2.show(hp2, "1");
             hc3.show(hp3, "3");
             wp.add(hp2);
             wp.add(hp3);
             wp.add(hp1);
             wp.add(btp);
             wp.add(nps);
             wp.add(load);
             wp.add(previous);
             wp.add(next);
             wp.add(mainlable);
//             wp.add(watermark);
             revalidate();
             repaint();
             tophit.addActionListener(this);
             bollywood.addActionListener(this);
             hollywood.addActionListener(this);
             webseries.addActionListener(this);
             rs.addActionListener(this);
//           f.addKeyListener(new KeyAdapter()
//           {
//   			
//			@Override
//			public void keyTyped(KeyEvent e) 
//			{
//			       if(e.getKeyCode()==KeyEvent.VK_RIGHT)
//			       {
//			    	   System.out.println("1");
//			    	   next.doClick();
//			       }
//			       else if(e.getKeyCode()==KeyEvent.VK_LEFT)
//			       {
//			    	   System.out.println("2");
//		 	    	   previous.doClick();
//			       }
//			}
//			
//			
//		      });
           });
           
           
	}
	public static void main(String args[]) throws MalformedURLException
	{
		new HomeWindow("Visual-Tv",null,"abhi");
	}
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		JButton b=(JButton)e.getSource();
		if(b.getText().equalsIgnoreCase("Bollywood"))
		{
			new BollyWood("Bollywood-Movies", email,f);
			f.setVisible(false);
		}
		else if(b.getText().equalsIgnoreCase("top listed"))
		{
			f.setVisible(false);
			new TopListed(top,"Tranding Movies",email,this);
		}
		else if(b.getText().equalsIgnoreCase("Hollywood"))
		{
			new HollyWood("Hollywood-Movies", email,f);
			f.setVisible(false);
		}
		else if(b.getText().equalsIgnoreCase("webshows"))
		{
			new WebShows("Web-Shows", email,f);
			f.setVisible(false);
		}
		else if(b.getText().equalsIgnoreCase("Recently Watched"))
		{
			new RecentlyWatched(this, "UserHistory", email);
			setVisible(false);
		}
	}
}