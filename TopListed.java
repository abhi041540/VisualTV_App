package ott_app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
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
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;

public class TopListed extends JFrame implements Runnable
{
	String email;
	JFrame f,cf=this;
	Map<String, URL>top;
	ScheduledExecutorService scheduer;
	JPanel loc,wp;
	int m=0;
	Font fn;
	URL imdb;
	ScheduledFuture<?> sf1,sf2,sf3,sf4,sf5;
	EmbeddedMediaPlayerComponent em;
    public TopListed(Map<String, URL>top,String str,String email,JFrame f)
    {
    	super(str);
    	getContentPane().setBackground(new Color(0,0,10));
    	repaint();
    	this.email=email;
    	this.f=f;
    	this.top=top;
    	try 
    	{
			imdb= new URL("https://www.imdb.com/");
		}
    	catch (MalformedURLException e) 
    	{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	fn= new Font("Arial",Font.BOLD,25);
    	setSize(Toolkit.getDefaultToolkit().getScreenSize());
    	setVisible(true);
       setLayout(null);
       wp= new JPanel();
       wp.setOpaque(false);
       wp.setBackground(new Color(0,0,0,0));
       setIconImage(Image_Box.LOGO);
       loc= new JPanel();
       loc.setOpaque(false);
       loc.setBackground(new Color(0,0,0,0));
      
    	scheduer= Executors.newScheduledThreadPool(10);
    	try {
			SwingUtilities.invokeLater(new Runnable() {
						
						@Override
						public void run()
						{
							cf.addWindowListener(new WindowAdapter() 
							{
								@Override
								public void windowClosing(WindowEvent e)
								{
									cf.dispose();
									f.setVisible(true);
									scheduer.shutdownNow();
									 
								}
							});
							getContentPane().setBackground(new Color(0,0,10));
					    	
							
							em= new EmbeddedMediaPlayerComponent();
							 loc.setBounds((cf.getWidth()/2)-35,(cf.getHeight()/2)-35,70, 70);
							 loc.setLayout(new BorderLayout());
							 loc.add(em);
							   cf.add(loc);
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
				                wp.setLayout(new GridLayout(5,1,10,10));
				                
				             
						}
					});
		}
    	catch (Exception e) 
    	{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    sf1= scheduer.schedule(new Runnable() {
			
			@Override
			public void run()
			{
				try {
					SwingUtilities.invokeAndWait(new Runnable()
					{
						
						@Override
						public void run()
						{
							Date d= new Date();
							SimpleDateFormat sf= new SimpleDateFormat("yyyy");
							String year=sf.format(d);
							JPanel p1= new JPanel();
							p1.setOpaque(false);
							p1.setBackground(new Color(0,0,0,0));
							JSplitPane js= new JSplitPane();
							js.setEnabled(false);
							js.setDividerSize(0);
							js.setBackground(new Color(0,0,0,0));
							js.setOpaque(false);
							js.setOrientation(JSplitPane.VERTICAL_SPLIT);
							JPanel lefp= new JPanel();
							lefp.setBackground(new Color(0,0,0,0));
							lefp.setOpaque(false);
							lefp.setLayout(new GridLayout(1,2,20,02));
							JLabel tl= new JLabel("*Top Trending in "+year+"*");
							tl.setForeground(Color.white);
							tl.setFont(fn);
							lefp.add(tl);
							js.setLeftComponent(lefp);
							JPanel rp= new JPanel();
							rp.setBackground(new Color(0,0,0,0));
							rp.setOpaque(false);
//						rp.setLayout(new GridLayout(1,10,5,3));
							js.setBorder(BorderFactory.createLineBorder(new Color(0,0,10),2));
							for(Map.Entry<String,URL>mp:top.entrySet())
							{
								ImageIcon im= new ImageIcon(mp.getValue());
								Image img= im.getImage();
								JLabel jl= new JLabel(new ImageIcon(img.getScaledInstance(220, 300,Image.SCALE_SMOOTH)));
								jl.setText(mp.getKey());
								jl.setForeground(new Color(0,0,0,0));
								jl.setPreferredSize(new Dimension(220,300));
								jl.setBorder(BorderFactory.createLineBorder(new Color(125,125,125),2));
								rp.add(jl);
								jl.addMouseListener(new MouseAdapter()
								{
									@Override
									public void mouseClicked(MouseEvent me)
									{
										JLabel cl= (JLabel)me.getSource();
										cf.setVisible(false);
										new MediaDetail(cl.getText(),cf, email);
									}
								});
							}

							revalidate();
							repaint();
							js.setRightComponent(rp);
							p1.add(js);
							
							
							wp.add(p1);
							revalidate();
							repaint();
						}
					});
				} 
				catch (Exception e) 
				{
					SwingUtilities.invokeLater(new Runnable() {
						
						@Override
						public void run() 
						{
							JOptionPane.showMessageDialog(cf, "Network Issue Please Try Again Later!");
							cf.dispose();
							f.setVisible(true);
							scheduer.shutdownNow();
							
						}
					});
				}
				
				
			}
		}, 1,TimeUnit.MICROSECONDS);
   
    
    sf2=scheduer.schedule(new Runnable() 
    {
		
		@Override
		public void run() 
		{
			SwingUtilities.invokeLater(new Runnable()
			{
				
				@Override
				public void run() 
				{
					JPanel p2= new JPanel();
					p2.setOpaque(false);
					p2.setLayout(new BorderLayout());
					p2.setBackground(new Color(0,0,0,0));
					JSplitPane jsp= new JSplitPane();
					jsp.setDividerSize(0);
					jsp.setBorder(BorderFactory.createLineBorder(new Color(0,0,15),2));
					jsp.setEnabled(false);
					jsp.setOrientation(JSplitPane.VERTICAL_SPLIT);
					jsp.setOpaque(false);
					jsp.setBackground(new Color(0,0,0,0));
					JPanel lp= new JPanel();
					lp.setOpaque(false);
					lp.setBackground(new Color(0,0,0,0));
					lp.setLayout(new GridLayout(1,2,5,5));
					JLabel tagn= new JLabel("*Global Top 10 Movies This Week*");
					tagn.setForeground(Color.white);
					tagn.setFont(fn);
					lp.add(tagn);
					jsp.setLeftComponent(lp);
					JPanel rp= new JPanel();
					rp.setOpaque(false);
					rp.setBackground(new Color(0,0,0,0));
		           
					
					try 
					{
						Document doc= Jsoup.connect("https://www.netflix.com/tudum/top10").get();
						Element t1=doc.selectFirst("#appMountPoint div div.lang-en div div.page.page-kind-TOP10.has-sub-nav section.medCard div div table tbody");
						Elements tr=t1.select("tr");
					    for(Element e1 :tr)
					    {
					    	Element imgtag=e1.selectFirst("td img");
					         String imgsrc=(String)imgtag.attr("src");
					         Element nametag=e1.selectFirst("td button");
					       String name=nametag.text();
					         
									ImageIcon im=null;
									try 
									{
										im = new ImageIcon(new URL(imdb,imgsrc));
									} 
									catch (MalformedURLException e) 
									{
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									Image img= im.getImage();
									JLabel jl= new JLabel(new ImageIcon(img.getScaledInstance(220, 300,Image.SCALE_SMOOTH)));
									jl.setText(name);
									jl.setForeground(new Color(0,0,0,0));
									jl.setPreferredSize(new Dimension(220,300));
									jl.setBorder(BorderFactory.createLineBorder(new Color(125,125,125),2));
									rp.add(jl);
									revalidate();
									repaint();
									jl.addMouseListener(new MouseAdapter()
									{
										@Override
										public void mouseClicked(MouseEvent me)
										{
											JLabel cl= (JLabel)me.getSource();
											cf.setVisible(false);
											new MediaDetail(cl.getText(),cf, email);
										}
									});
								
					    }
					    jsp.setRightComponent(rp);
					    wp.add(jsp);
					    revalidate();
					    repaint();
					   
					} 
					catch (Exception e)
					{
                            SwingUtilities.invokeLater(new Runnable() {
							
							@Override
							public void run() 
							{
								JOptionPane.showMessageDialog(cf, "Network Issue Please Try Again Later!");
								cf.dispose();
								f.setVisible(true);
								scheduer.shutdownNow();
								
							}
						});
						
					}
					
				}
			});
			
		}
	}, 1, TimeUnit.MICROSECONDS);
    
    sf3=scheduer.schedule(new Runnable() 
    {
		
		@Override
		public void run() 
		{
			SwingUtilities.invokeLater(new Runnable()
			{
				
				@Override
				public void run() 
				{
					JPanel p2= new JPanel();
					p2.setOpaque(false);
					p2.setLayout(new BorderLayout());
					p2.setBackground(new Color(0,0,0,0));
					JSplitPane jsp= new JSplitPane();
					jsp.setDividerSize(0);
					jsp.setBorder(BorderFactory.createLineBorder(new Color(0,0,15),2));
					jsp.setEnabled(false);
					jsp.setOrientation(JSplitPane.VERTICAL_SPLIT);
					jsp.setOpaque(false);
					jsp.setBackground(new Color(0,0,0,0));
					JPanel lp= new JPanel();
					lp.setOpaque(false);
					lp.setBackground(new Color(0,0,0,0));
					lp.setLayout(new GridLayout(1,2,5,5));
					JLabel tagn= new JLabel("*Top 10 Movies in India This Week*");
					tagn.setForeground(Color.white);
					tagn.setFont(fn);
					lp.add(tagn);
					jsp.setLeftComponent(lp);
					JPanel rp= new JPanel();
					rp.setOpaque(false);
					rp.setBackground(new Color(0,0,0,0));
		           
					
					try 
					{
						Document doc= Jsoup.connect("https://www.netflix.com/tudum/top10/india").get();
						Element t1=doc.selectFirst("#appMountPoint div div.lang-en div div.page.page-kind-TOP10.has-sub-nav section.medCard div div table tbody");
						Elements tr=t1.select("tr");
					    for(Element e1 :tr)
					    {
					    	Element imgtag=e1.selectFirst("td img");
					         String imgsrc=(String)imgtag.attr("src");
					         Element nametag=e1.selectFirst("td button");
					       String name=nametag.text();
					         
									ImageIcon im=null;
									try 
									{
										im = new ImageIcon(new URL(imdb,imgsrc));
									} 
									catch (MalformedURLException e) 
									{
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									Image img= im.getImage();
									JLabel jl= new JLabel(new ImageIcon(img.getScaledInstance(220, 300,Image.SCALE_SMOOTH)));
									jl.setText(name);
									jl.setForeground(new Color(0,0,0,0));
									jl.setPreferredSize(new Dimension(220,300));
									jl.setBorder(BorderFactory.createLineBorder(new Color(125,125,125),2));
									rp.add(jl);
									revalidate();
									repaint();
									jl.addMouseListener(new MouseAdapter()
									{
										@Override
										public void mouseClicked(MouseEvent me)
										{
											JLabel cl= (JLabel)me.getSource();
											cf.setVisible(false);
											new MediaDetail(cl.getText(),cf, email);
										}
									});
								
					    }
					    jsp.setRightComponent(rp);
					    wp.add(jsp);
					    revalidate();
					    repaint();
					   
					} 
					catch (Exception e)
					{
                          SwingUtilities.invokeLater(new Runnable() {
							
							@Override
							public void run() 
							{
								JOptionPane.showMessageDialog(cf, "Network Issue Please Try Again Later!");
								cf.dispose();
								f.setVisible(true);
								scheduer.shutdownNow();
								
							}
						});
						
					}
					
				}
			});
			
		}
	}, 1, TimeUnit.MICROSECONDS);
    
    sf4=scheduer.schedule(new Runnable() 
    {
		
		@Override
		public void run() 
		{
			SwingUtilities.invokeLater(new Runnable()
			{
				
				@Override
				public void run() 
				{
					JPanel p2= new JPanel();
					p2.setOpaque(false);
					p2.setLayout(new BorderLayout());
					p2.setBackground(new Color(0,0,0,0));
					JSplitPane jsp= new JSplitPane();
					jsp.setDividerSize(0);
					jsp.setBorder(BorderFactory.createLineBorder(new Color(0,0,15),2));
					jsp.setEnabled(false);
					jsp.setOrientation(JSplitPane.VERTICAL_SPLIT);
					jsp.setOpaque(false);
					jsp.setBackground(new Color(0,0,0,0));
					JPanel lp= new JPanel();
					lp.setOpaque(false);
					lp.setBackground(new Color(0,0,0,0));
					lp.setLayout(new GridLayout(1,2,5,5));
					JLabel tagn= new JLabel("*Top 10 Korean Movies This Week*");
					tagn.setForeground(Color.white);
					tagn.setFont(fn);
					lp.add(tagn);
					jsp.setLeftComponent(lp);
					JPanel rp= new JPanel();
					rp.setOpaque(false);
					rp.setBackground(new Color(0,0,0,0));
		           
					
					try 
					{
						Document doc= Jsoup.connect("https://www.netflix.com/tudum/top10/south-korea").get();
						Element t1=doc.selectFirst("#appMountPoint div div.lang-en div div.page.page-kind-TOP10.has-sub-nav section.medCard div div table tbody");
						Elements tr=t1.select("tr");
					    for(Element e1 :tr)
					    {
					    	Element imgtag=e1.selectFirst("td img");
					         String imgsrc=(String)imgtag.attr("src");
					         Element nametag=e1.selectFirst("td button");
					       String name=nametag.text();
					         
									ImageIcon im=null;
									try 
									{
										im = new ImageIcon(new URL(imdb,imgsrc));
									} 
									catch (MalformedURLException e) 
									{
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									Image img= im.getImage();
									JLabel jl= new JLabel(new ImageIcon(img.getScaledInstance(220, 300,Image.SCALE_SMOOTH)));
									jl.setText(name);
									jl.setForeground(new Color(0,0,0,0));
									jl.setPreferredSize(new Dimension(220,300));
									jl.setBorder(BorderFactory.createLineBorder(new Color(125,125,125),2));
									rp.add(jl);
									revalidate();
									repaint();
									jl.addMouseListener(new MouseAdapter()
									{
										@Override
										public void mouseClicked(MouseEvent me)
										{
											JLabel cl= (JLabel)me.getSource();
											cf.setVisible(false);
											new MediaDetail(cl.getText(),cf, email);
										}
									});
								
					    }
					    jsp.setRightComponent(rp);
					    wp.add(jsp);
					    revalidate();
					    repaint();
					   
					} 
					catch (Exception e)
					{
                          SwingUtilities.invokeLater(new Runnable() {
							
							@Override
							public void run() 
							{
								JOptionPane.showMessageDialog(cf, "Network Issue Please Try Again Later!");
								cf.dispose();
								f.setVisible(true);
								scheduer.shutdownNow();
								
							}
						});
						
					}
					
				}
			});
			
		}
	}, 1, TimeUnit.MICROSECONDS);
    
    sf5=scheduer.schedule(new Runnable() 
    {
		
		@Override
		public void run() 
		{
			SwingUtilities.invokeLater(new Runnable()
			{
				
				@Override
				public void run() 
				{
					JPanel p2= new JPanel();
					p2.setOpaque(false);
					p2.setLayout(new BorderLayout());
					p2.setBackground(new Color(0,0,0,0));
					JSplitPane jsp= new JSplitPane();
					jsp.setDividerSize(0);
					jsp.setBorder(BorderFactory.createLineBorder(new Color(0,0,15),2));
					jsp.setEnabled(false);
					jsp.setOrientation(JSplitPane.VERTICAL_SPLIT);
					jsp.setOpaque(false);
					jsp.setBackground(new Color(0,0,0,0));
					JPanel lp= new JPanel();
					lp.setOpaque(false);
					lp.setBackground(new Color(0,0,0,0));
					lp.setLayout(new GridLayout(1,2,5,5));
					JLabel tagn= new JLabel("*Top 10 Movies in Asia This Week*");
					tagn.setForeground(Color.white);
					tagn.setFont(fn);
					lp.add(tagn);
					jsp.setLeftComponent(lp);
					JPanel rp= new JPanel();
					rp.setOpaque(false);
					rp.setBackground(new Color(0,0,0,0));
		           
					
					try 
					{
						Document doc= Jsoup.connect("https://www.netflix.com/tudum/top10/philippines").get();
						Element t1=doc.selectFirst("#appMountPoint div div.lang-en div div.page.page-kind-TOP10.has-sub-nav section.medCard div div table tbody");
						Elements tr=t1.select("tr");
					    for(Element e1 :tr)
					    {
					    	Element imgtag=e1.selectFirst("td img");
					         String imgsrc=(String)imgtag.attr("src");
					         Element nametag=e1.selectFirst("td button");
					       String name=nametag.text();
					         
									ImageIcon im=null;
									try 
									{
										im = new ImageIcon(new URL(imdb,imgsrc));
									} 
									catch (MalformedURLException e) 
									{
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									Image img= im.getImage();
									JLabel jl= new JLabel(new ImageIcon(img.getScaledInstance(220, 300,Image.SCALE_SMOOTH)));
									jl.setText(name);
									jl.setForeground(new Color(0,0,0,0));
									jl.setPreferredSize(new Dimension(220,300));
									jl.setBorder(BorderFactory.createLineBorder(new Color(125,125,125),2));
									rp.add(jl);
									revalidate();
									repaint();
									jl.addMouseListener(new MouseAdapter()
									{
										@Override
										public void mouseClicked(MouseEvent me)
										{
											JLabel cl= (JLabel)me.getSource();
											cf.setVisible(false);
											new MediaDetail(cl.getText(),cf, email);
										}
									});
								
					    }
					    jsp.setRightComponent(rp);
					    wp.add(jsp);
					    revalidate();
					    repaint();
					   
					} 
					catch (Exception e)
					{
                          SwingUtilities.invokeLater(new Runnable() {
							
							@Override
							public void run() 
							{
								JOptionPane.showMessageDialog(cf, "Network Issue Please Try Again Later!");
								cf.dispose();
								f.setVisible(true);
								scheduer.shutdownNow();
								
							}
						});
						
					}
					
				}
			});
			
		}
	}, 1, TimeUnit.MICROSECONDS);
    
    Thread th= new Thread(this,"main");
    th.start();
    }
	@Override
	public void run() 
	{
			SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() 
				{
					
				    try
				    {
				    	
						sf1.get();
						sf2.get();
						sf3.get();
						
						if(em!=null && em.isDisplayable())
						{
							em.mediaPlayer().controls().stop();
							em.release();
						}
						loc.remove(em);
						revalidate();
						repaint();
						cf.setLayout(new BorderLayout());
						JScrollPane jwp= new JScrollPane(wp);
					    JScrollBar hs=jwp.getHorizontalScrollBar();
						hs.setUI(new ScrollBar(Color.LIGHT_GRAY,new Color(0,0,10)));
						JScrollBar vs=jwp.getVerticalScrollBar();
						vs.setUI(new ScrollBar(Color.LIGHT_GRAY,new Color(0,0,10)));
						jwp.setBackground(new Color(0,0,0,0));
						jwp.getViewport().setOpaque(false);
						jwp.setOpaque(false);
						cf.add(jwp);
						revalidate();
						repaint();
					} 
				    catch (Exception e) 
				    {
				    	e.printStackTrace();
						JOptionPane.showMessageDialog(cf, "Network Issue Please Try Again Later!");
						cf.dispose();
						f.setVisible(true);
						scheduer.shutdownNow();
					}
				    
				}
			});
		
	}
}
