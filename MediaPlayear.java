package ott_app;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.openqa.selenium.chrome.ChromeDriver;

import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaListPlayerComponent;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;


public class MediaPlayear extends JFrame implements Runnable {
    String name, email;
    JPanel loc;
    long len,cpo;
    EmbeddedMediaPlayerComponent em,em1;
    boolean chack=false;
    int m = 0;
    Thread th;
    ScheduledExecutorService scheduler;
    JFrame cf, f;
    String url = "";
    float playpoint;
    ScheduledFuture<String> sf;
    float dur = 0.0f;
    String st="";
    File tf;
//    Thread mdut;
    public MediaPlayear(JFrame f, String name, String email, float durationp)
    {
        super(name);
        cf = this;
      
        this.f = f;
        playpoint = durationp;
        th = new Thread(this, "main");
        this.name = name;
        this.email = email;
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        scheduler = Executors.newScheduledThreadPool(10);
        loc = new JPanel();
        loc.setLayout(new BorderLayout());
        setLayout(null);
        loc.setBounds((getWidth()/2)-35, (getHeight()/2)-35, 70, 70);
        add(loc);
        em1 = new EmbeddedMediaPlayerComponent();
        loc.add(em1);
        cf.getContentPane().setBackground(new Color(0, 0, 10));
        cf.setIconImage(Image_Box.LOGO);
        cf.setVisible(true);
        em1.mediaPlayer().media().play("C:\\Users\\DELL\\Downloads\\loading.mp4");
        
        sf = scheduler.schedule(new Callable<String>() {
            @Override
            public String call()
            {
            	
            	try {
            		st=MediaUrl1.getUrl((String)name.toUpperCase());
            		  } 
            	catch (Exception e) 
            	{	
					e.printStackTrace();
				}
                return (st);
            }
        },-1, TimeUnit.MILLISECONDS); 
//        mdut= new Thread("Media Thread") {
//        	@Override
//        	public void run()
//        	{
//        		try {
//            	     url=MediaUrl1.getUrl(name.toUpperCase());
//				   } 
//            	catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//        	}
//        };
//            mdut.start();
        getContentPane().setBackground(new Color(0, 0, 10));
        setVisible(true);
        setLayout(new BorderLayout());
        revalidate();
        repaint();
                cf.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent we) 
                    {
                    	if(em1.isDisplayable() && em1!=null)
                        {
                        	 em1.mediaPlayer().controls().stop();
                             em1.release();	
                        }
                        if(em.isDisplayable() && em!=null)
                        {
                        	 em.mediaPlayer().controls().stop();
                             em.release();	
                        }
                       
                    	cf.dispose();
                    	 f.setVisible(true);
                        Connection con = null;
                        try {
                            con = DataBase.getConnection();
                            con.setAutoCommit(false);
                            PreparedStatement ps1 = con.prepareStatement("select duration from userhistory where email=? and name=?");
                            ps1.setString(1, email);
                            ps1.setString(2, name);
                            ResultSet rs = ps1.executeQuery();
                            if (rs.next()) {
                                ps1 = con.prepareStatement("update userhistory set duration=? where email=? and name=?");
                                ps1.setFloat(1, dur); 
                                ps1.setString(2, email);
                                ps1.setString(3, name);
                                ps1.executeUpdate();
                            } 
                            else {
                                PreparedStatement ps = con.prepareStatement("insert into userhistory(name,email,duration) values(?,?,?)");
                                ps.setString(1, name);
                                ps.setString(2, email);
                                ps.setFloat(3, dur);
                                ps.executeUpdate();
                                ps.close();
                            }
                            con.commit();
                            con.close();
                            System.out.println("commit");
                         } 
                        catch (SQLException e) {
                            try {
                                con.rollback();
                                con.close();
                            } catch (SQLException e1) {
                                System.out.println("rollback");
                                e1.printStackTrace();
                            }
                            e.printStackTrace();
                        }
                        
                        scheduler.shutdownNow();
                        
                        
                    }
                });
                em1.mediaPlayer().events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
                    @Override
                    public void finished(MediaPlayer me) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() 
                            {
                                
                                    me.media().play("C:\\Users\\DELL\\Downloads\\loading.mp4");
                                    revalidate();
                                    repaint();
                                    
                                
                            }
                        });
                    }
                });
               
                th.start();
    }

    @Override
    public void run() {
        
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                	try
                	{
                    em = new EmbeddedMediaPlayerComponent();
                    em.mediaPlayer().video().setAspectRatio("16:9");
                    em.mediaPlayer().fullScreen().set(true);
                    
                    JPanel cantrol = new JPanel();
                    cantrol.setOpaque(false);
                    cantrol.setBackground(new Color(0, 0, 0, 0));
                    JButton pp = new JButton("pause");
                    pp.setForeground(new Color(0, 0, 0, 0));
                    pp.setOpaque(false);
                    pp.setBackground(new Color(0, 0, 0, 0));
                    JButton bw = new JButton("pause");
                    bw.setForeground(new Color(0, 0, 0, 0));
                    bw.setOpaque(false);
                    bw.setBackground(new Color(0, 0, 0, 0));
                    JButton fw = new JButton("pause");
                    fw.setForeground(new Color(0, 0, 0, 0));
                    fw.setOpaque(false);
                    fw.setBackground(new Color(0, 0, 0, 0));
                    ImageIcon play, pause, forward, backward;
                    play = new ImageIcon(Image_Box.PLAY.getScaledInstance(40, 40, Image.SCALE_SMOOTH));
                    pause = new ImageIcon(Image_Box.PAUSE.getScaledInstance(40, 40, Image.SCALE_SMOOTH));
                    backward = new ImageIcon(Image_Box.BACKW.getScaledInstance(40, 40, Image.SCALE_SMOOTH));
                    forward = new ImageIcon(Image_Box.FORW.getScaledInstance(40, 40, Image.SCALE_SMOOTH));
                    fw.setIcon(forward);
                    fw.setPreferredSize(new Dimension(50, 40));
                    bw.setIcon(backward);
                    bw.setPreferredSize(new Dimension(50, 40));
                    pp.setIcon(pause);
                    pp.setPreferredSize(new Dimension(50, 40));
                    pp.addActionListener(new ActionListener()
                    { 
						@Override
						public void actionPerformed(ActionEvent e) 
						{
					       JButton b=(JButton)e.getSource();
					       if(b.getText().equalsIgnoreCase("pause"))
					       {
					    	   em.mediaPlayer().controls().pause();
					    	   b.setText("play");
					    	   b.setIcon(play);
					       }
					       else
					       {
					    	   SwingUtilities.invokeLater(new Runnable(){
				        			@Override 
				        			public void run()
				        			{

								    	   em.mediaPlayer().controls().play();
								    	   b.setText("pause");
								    	   b.setIcon(pause);                 
				        			}
				        		});
					       }
							
						}
					});
			        fw.addActionListener(new ActionListener() 
			        {
						@Override
						public void actionPerformed(ActionEvent e) 
						{
                              SwingUtilities.invokeLater(new Runnable(){
			        			@Override 
			        			public void run()
			        			{
			        				if(em.mediaPlayer().status().isPlaying())
			        				em.mediaPlayer().controls().setTime((em.mediaPlayer().status().time() + 10000));
			        				
			        			}
			        		});
							
						}
					});
			        bw.addActionListener(new ActionListener() 
			        {
						@Override
						public void actionPerformed(ActionEvent e) 
						{
							SwingUtilities.invokeLater(new Runnable(){	
			        			@Override 
			        			public void run()
			        			{
			        				if(em.mediaPlayer().status().isPlaying())
				        				em.mediaPlayer().controls().setTime((em.mediaPlayer().status().time() - 10000));
			        			}
			        			});
						}
					});
			        JSlider slider= new JSlider();
			        slider.setUI(new Positioner(slider));
		            slider.setPreferredSize(new Dimension(cf.getWidth(), 15));
		             slider.setOpaque(false);
		             slider.setMinimum(0);
		             slider.setValue(0);
		            JLabel developer= new JLabel("Developed by:Abhishek Jain");
		             developer.setFont(new Font("Arial",Font.BOLD,10));
		             developer.setForeground(Color.white);
		             JPanel pbp= new JPanel();
		             cantrol.setLayout(new BorderLayout());
		             pbp.setBackground(new Color(0,0,0,0));
		             pbp.setOpaque(false);
		             pbp.add(bw);
			        pbp.add(pp);
			        pbp.add(fw);
			        pbp.add(developer);
			        developer.setVerticalAlignment(SwingConstants.BOTTOM);
			        JLabel sd= new JLabel("00:00:00");
			        sd.setForeground(new Color(125,125,125));
			        sd.setFont(new Font("Aria",Font.BOLD,15));
			        JLabel ed= new JLabel("00:00:00");
			        ed.setForeground(new Color(125,125,125));
			        ed.setFont(new Font("Aria",Font.BOLD,15));
			        cantrol.add(BorderLayout.NORTH,slider);
			        cantrol.add(BorderLayout.CENTER,pbp);
			        cantrol.add(BorderLayout.EAST,ed);
			        cantrol.add(BorderLayout.WEST,sd);
			       bw.setBorderPainted(false);
			       pp.setBorderPainted(false);
			       fw.setBorderPainted(false);
			        em.mediaPlayer().events().addMediaPlayerEventListener(new MediaPlayerEventAdapter()
			        {
			        	@Override
			        	public void error(MediaPlayer mp)
			        	{
			        		mp.media().play(tf.getAbsolutePath());
			        		try 
			        		{
								Thread.sleep(1000);
							} 
			        		catch (InterruptedException e)
			        		{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			        		mp.controls().setPosition((float)dur);
			        		System.out.println("error");
			        	}
			        	@Override
			        	public void finished(MediaPlayer mp)
			        	{
			        		SwingUtilities.invokeLater(new Runnable(){
			        			
			        			@Override 
			        			public void run()
			        			{
					        		JOptionPane.showMessageDialog(cf, "Thanks For Watching");
                                }
			        		});
			        	}
			        });
			        
			       try
			       {
					url=sf.get();
//			    	   mdut.join();
			       } 
			       catch (Exception e1)
			       {
					System.out.println("yes render");
					e1.printStackTrace();
				    } 
			       
					if(url.equalsIgnoreCase("not found"))
					{
						if(em1!=null && em1.isDisplayable())
						{
							em1.mediaPlayer().controls().stop();
	                        em1.release();
	                        loc.remove(em1);
						}
						
						 cf.getContentPane().remove(loc);
					        revalidate();
					        repaint();
					        
						 System.out.println("yes render1");

			        		SwingUtilities.invokeLater(new Runnable(){
			        			
			        			@Override 
			        			public void run()
			        			{
									JOptionPane.showMessageDialog(cf, "Movie Will Be Available Soon!");
									scheduler.shutdownNow();
					                cf.dispose();
					                f.setVisible(true);
                               }
			        		});
						
		               
					}
					else if(url.equalsIgnoreCase("network issue"))
					{
						if(em1!=null && em1.isDisplayable())
						{
							em1.mediaPlayer().controls().stop();
	                        em1.release();
	                        loc.remove(em1);
						}
						
						 cf.getContentPane().remove(loc);
					        revalidate();
					        repaint();
					        
						 System.out.println("yes render2");
						 
						 SwingUtilities.invokeLater(new Runnable(){
			        			
			        			@Override
			        			public void run()
			        			{
			        				JOptionPane.showMessageDialog(cf, "Network Issue Please Try Again!");
			            		scheduler.shutdownNow();
				                cf.dispose();
				                f.setVisible(true);
                              }
			        		});
						
		                
					}
					else
					{
					        try
					        {
//					        	int id=url.indexOf("/index.m3u8");
//					        	if(id!=-1)
//					        	{
//					        		int upd=url.substring(id-5,id).indexOf('/');
//					        		if(upd!=-1)
//					        		{
//					        		upd=url.length()-((4-upd)+11);
//					        		url=url.substring(0,upd)+"/720/index.m3u8";
//					        		
//					        		}
//					        		else
//					        		{
//					        			url=url.substring(0,id)+"/720/index.m3u8";
//					        		}
//					        		System.out.println(url);
//					        	}
//					            tf= new File("C:\\Users\\abhis\\Downloads\\"+name+".m3u8");
					        	
							    tf=File.createTempFile("movie",".m3u8");
//								f.createNewFile();
								
								InputStream in=new URL(url).openStream();
//								Files.copy(in,f.toPath(),StandardCopyOption.REPLACE_EXISTING);
								
								Files.copy(in,tf.toPath(),StandardCopyOption.REPLACE_EXISTING);
								
						SwingUtilities.invokeLater(new Runnable() 
							{
								public void run() 
								{
									if(em1!=null && em1.isDisplayable())
									{
										em1.mediaPlayer().controls().stop();
				                        em1.release();
				                        loc.remove(em1);
									}
									
										 cf.getContentPane().remove(loc);
									        revalidate();
									        repaint();
									        cf.setLayout(new BorderLayout());
									        revalidate();
									        repaint();
								        System.out.println(url);
								        cf.add(BorderLayout.CENTER,em);
									cf.add(BorderLayout.SOUTH,cantrol);
									revalidate();
									repaint();
				                    em.mediaPlayerFactory().media().newMedia(tf.getAbsolutePath(),":fastseek");
									em.mediaPlayer().media().play(tf.getAbsolutePath());
									revalidate();
									repaint();
									try
									{
										Thread.sleep(10000);
									} catch (InterruptedException e) 
									{
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									 len=em.mediaPlayer().media().info().duration();
									 for(;(len==0);)
									 {
										 try 
										 {
											Thread.sleep(2000);
										} 
										 catch (InterruptedException e)
										 {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										 len=em.mediaPlayer().media().info().duration();
											if(len!=0)
											{
												break;
											}
										 
									 }
//									 System.out.println("dur."+len);
									 slider.setMaximum((int)len);
									    long hou=(len/(1000*60*60));
									    long min=((len/(1000*60))%60);
									    long sec=(len/1000)%60;
								    ed.setText(hou+":"+min+":"+sec);
								    revalidate();
								    repaint();
									if((long)playpoint*len==0)
									{
										em.mediaPlayerFactory().media().newMedia(tf.getAbsolutePath(),":network-caching=15000");
									}
									else
									{
										em.mediaPlayerFactory().media().newMedia(tf.getAbsolutePath(),":network-caching="+(long)playpoint*len);
									}
									try
									{
										Thread.sleep(1000);
									} 
									catch (InterruptedException e) 
									{
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
			        				em.mediaPlayer().controls().setTime((long)Math.ceil(playpoint*len));
			        				
								}
							});
							chack=false;
						slider.addChangeListener(new ChangeListener()
						{
							
							@Override
							public void stateChanged(ChangeEvent e)
							{
								
									SwingUtilities.invokeLater(new Runnable()
									{
										
										@Override
										public void run() 
										{
											JSlider js= (JSlider)(e.getSource());
											int sv=js.getValue();
											slider.setValue(sv);
											long hou2=(sv/(1000*60*60));
										    long min2=((sv/(1000*60))%60);
										    long sec2=(sv/1000)%60;
										    sd.setText(hou2+":"+min2+":"+sec2);
										}
									});
								
							}
						});
						slider.addMouseListener(new MouseAdapter()
						{
							@Override
							public void mouseClicked(MouseEvent me)
							{
								
								em.mediaPlayer().controls().setTime(slider.getValue());
                                
							}
							@Override
							public void mouseReleased(MouseEvent e)
							{
								
								em.mediaPlayer().controls().setTime(slider.getValue());
								chack=false;
							}
						});
						slider.addMouseMotionListener(new MouseMotionAdapter()
						{
							@Override
							public void mouseDragged(MouseEvent e)
							{
								chack=true;
							
							}
						});
                      scheduler.scheduleWithFixedDelay(new Runnable() 
                      {
						
						@Override
						public void run()
						{
							try {
								SwingUtilities.invokeAndWait(new Runnable() 
								{
									
									@Override
									public void run() 
									{
										if(chack==false)
										{
											if(em.mediaPlayer().status().isPlaying())
											{
												dur=em.mediaPlayer().status().position();
												cpo=(long) Math.ceil(dur*len);
									     		long hou1=(cpo/(1000*60*60));
											    long min1=((cpo/(1000*60))%60);
											    long sec1=(cpo/1000)%60;
											    sd.setText(hou1+":"+min1+":"+sec1);
											    slider.setValue((int)cpo);
//										       System.out.println(hou1+":"+min1+":"+sec1);
						     				    revalidate();
				     						    repaint();
							    			}	
										}
										
								   }
								});
							} 
							catch (Exception e) 
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							// TODO Auto-generated method stub
							
						}
					}, 0, 1,TimeUnit.SECONDS);
                      
                      revalidate();
  					  repaint();
  					
                      
					        }
					 catch (Exception e1)
					        {
								// TODO Auto-generated catch block
					        	System.out.println("filesaving issue");
								e1.printStackTrace();
								if(em1!=null && em1.isDisplayable())
								{
									em1.mediaPlayer().controls().stop();
			                        em1.release();
			                        loc.remove(em1);
								}
								
								 cf.getContentPane().remove(loc);
							        revalidate();
							        repaint();
							        
								 System.out.println("yes render2");
								 
								 SwingUtilities.invokeLater(new Runnable(){
					        			
					        			@Override
					        			public void run()
					        			{
					        				JOptionPane.showMessageDialog(cf, "Movie Will Be Available Soon Server Issue!");
					            		scheduler.shutdownNow();
						                cf.dispose();
						                f.setVisible(true);
		                              }
					        		});
								
							}
					}
                	 }
                    catch(Exception e)
                    {
                    	e.printStackTrace();
                    }   
			}
           
               
		});
	
	
}
}
