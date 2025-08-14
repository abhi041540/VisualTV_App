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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.support.ui.WebDriverWait;

import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaListPlayerComponent;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;


public class ShowMediaPlayer extends JFrame implements Runnable {
    String name2,email,lenguage1;
    JPanel loc;
    long len,cpo;
    EmbeddedMediaPlayerComponent em,em1;
    boolean chack=false;
    int m = 0;
    Thread th;
    ScheduledExecutorService scheduler;
    JFrame f;
    int recseson;
    ShowMediaPlayer cf;
    String url = "";
    float playpoint;
    ScheduledFuture<Map<String, File>> sf;
    ScheduledFuture<?> epd;
    float dur = 0.0f;
    int seson,ep;
    Thread thu;
    Map<String,File> st=null;
    File tf;
    String name=null;
    Map<Integer,Integer>epdata=new HashMap<Integer, Integer>();
    List<String>lenguagedata=new ArrayList<String>();
    public ShowMediaPlayer(JFrame f, String name, String email, float durationp,int seson,int ep,String lenguage)
    {
        super("ShowName: "+name);
        cf = this;
        this.name=name;
        this.seson=seson;
        this.ep=ep;
        this.lenguage1=lenguage;
        this.f = f;
        recseson=cf.seson;
        playpoint = durationp;
        th = new Thread(this, "main");
        this.name2 = name;
        this.email = email;
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        scheduler = Executors.newScheduledThreadPool(11);
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
        if(this.seson==0)
        {
        	this.seson=1;
        }
        
        if(this.ep<=0)
        {
        	this.ep=1;
        }
        
//      System.out.println("startingep"+ep);
        
        sf = scheduler.schedule(new Callable<Map<String,File>>() {
            @Override
            public Map<String,File> call()
            {
            	
            	try {
					thu=new Thread(new Runnable()
					{
						

						@Override
						public void run() 
						{
							
							if(lenguage1==null)
							{
								lenguage1="Hindi";
							}
//							System.out.println("mediaplayer call:"+lenguage1);
							 st=ShowMediaUrl1.getUrl(name2.toUpperCase(), cf.seson, cf.ep, lenguage1);
//							 System.out.println("chack= "+st);
						}
					},"url thread");
					thu.start();
					thu.join();
				   } 
            	catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//            	System.out.println(st);
                return (st);
            }
        },-1, TimeUnit.MILLISECONDS); 
        epd=scheduler.schedule(new Runnable() {
			
			@Override
			public void run() 
			{
				showData2();
			}
			},0,TimeUnit.MILLISECONDS);
        
       
       
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
                            PreparedStatement ps1 = con.prepareStatement("select duration,season,ep,lenguage from usershowhistory where email=? and name=?");
                            ps1.setString(1, email);
                            ps1.setString(2, name);
                            ResultSet rs = ps1.executeQuery();
                            if (rs.next()) {
                                ps1 = con.prepareStatement("update usershowhistory set duration=?,season=?,ep=?,lenguage=? where email=? and name=?");
                                ps1.setFloat(1, dur); 
                                ps1.setInt(2, cf.seson);
                                ps1.setInt(3, cf.ep);
                                ps1.setString(4,cf.lenguage1);
                                ps1.setString(5, email);
                                ps1.setString(6, name);
                                ps1.executeUpdate();
                            } 
                            else {
                                PreparedStatement ps = con.prepareStatement("insert into usershowhistory(name,email,duration,season,ep,lenguage) values(?,?,?,?,?,?)");
                                ps.setString(1, name);
                                ps.setString(2, email);
                                ps.setFloat(3, dur);
                                ps.setInt(4,cf.seson);
                                ps.setInt(5, cf.ep);
                                ps.setString(6, cf.lenguage1);
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
//			        JPanel
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
			    	   Map<String,File> urm=sf.get();
			    	   for(Map.Entry<String, File> me:urm.entrySet())
			    	   {
			    		   url=me.getKey();
			    		   tf=me.getValue();
			    	   }
					epd.get();
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
									JOptionPane.showMessageDialog(cf, "Show Will Be Available Soon!");
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
					        	Thread.sleep(2000);
//							    tf=File.createTempFile("movie",".m3u8");
////								tf.createNewFile();
//								InputStream in=new URL(url).openStream();
//								Files.copy(in,tf.toPath(),StandardCopyOption.REPLACE_EXISTING);
//								System.out.println((tf!=null)?"not null":"is null");
						SwingUtilities.invokeLater(new Runnable() 
							{
								public void run() 
								{
									JPanel epdatapnl= new JPanel();
									epdatapnl.setBackground(Color.black);
									String sesonname[]=new String[epdata.size()];
									for(int i=0;i<=epdata.size()-1;i++)
									{
										sesonname[i]="Season-"+(i+1);
									}
									JComboBox<String>sesondata=new JComboBox<String>(sesonname);
									sesondata.setBackground(new Color(0,120,215));
									sesondata.setForeground(Color.white);
									sesondata.setFont(new Font("Arial",Font.BOLD,15));
									sesondata.setSelectedIndex(seson-1);
									String lenguagenm[]=new String[lenguagedata.size()];
									int sel=0;
									for(int i=0;i<=lenguagedata.size()-1;i++)
									{
										lenguagenm[i]=lenguagedata.get(i);
										if(lenguagenm[i].equalsIgnoreCase(lenguage1))
										{
											sel=i;
										}
									}
									if(sel==0)
									{
										lenguage1=lenguagedata.get(0);
									}
									JComboBox<String>lenguagecombo=new JComboBox<String>(lenguagenm);
									lenguagecombo.setBackground(new Color(0,120,215));
									lenguagecombo.setForeground(Color.white);
									lenguagecombo.setFont(new Font("Arial",Font.BOLD,15));
									lenguagecombo.setSelectedIndex(sel);
//									System.out.println("ep"+epdata.get(seson));
									Integer epnm[]=new Integer[epdata.get(seson)];
								
									for(int i=0;i<=(epdata.get(seson))-1;i++)
									{
										epnm[i]=i+1;
									}
									JComboBox<Integer>epcombo=new JComboBox<Integer>(epnm);
									epcombo.setBackground(new Color(0,120,215));
									epcombo.setForeground(Color.white);
									epcombo.setFont(new Font("Arial",Font.BOLD,15));
									epcombo.setSelectedIndex(ep-1);
									JButton epdataselect=new JButton("Select");
									epdataselect.setBackground(new Color(0,120,215));
									epdataselect.setForeground(Color.white);
									epdataselect.setFont(new Font("Arial",Font.BOLD,15));
									epdatapnl.add(sesondata);
									epdatapnl.add(epcombo);
									epdatapnl.add(lenguagecombo);
									epdatapnl.add(epdataselect);
									sesondata.setPreferredSize(new Dimension(cf.getWidth()/14,30));
									epcombo.setPreferredSize(new Dimension(cf.getWidth()/14,30));
									lenguagecombo.setPreferredSize(new Dimension(cf.getWidth()/14,30));
									
                                   sesondata.addActionListener(new ActionListener() {
									
									@Override
									public void actionPerformed(ActionEvent e)
									{
//										System.out.print("item");
										 int sesi=sesondata.getSelectedIndex()+1;
//										 System.out.println("select"+sesi+".."+cf.seson);
										 if(sesi!=recseson)
										 {
											 recseson=sesi;
//											 System.out.println("item1:-"+epdata.get(sesi));
											 Integer[]in=new Integer[epdata.get(sesi)];
											 for(int i=0;i<=epdata.get(sesi)-1;i++)
											 {
												 in[i]=i+1;
											 }
//											 System.out.print(in.length);
											 DefaultComboBoxModel<Integer> ni=new DefaultComboBoxModel<Integer>(in);
											 epcombo.setModel(ni);
											 revalidate();
											 repaint();
										 }
									}
								});
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
									cf.add(BorderLayout.NORTH,epdatapnl);
//									kkn
									revalidate();
									repaint();
									
									 epdataselect.addActionListener(new ActionListener()
									 {
											
											@Override
											public void actionPerformed(ActionEvent e) 
											{
												int sind=sesondata.getSelectedIndex();
												int sep=epcombo.getSelectedIndex();
												String leng=(String)lenguagecombo.getSelectedItem();
//												System.out.println("lenguage:"+leng);
												if(!(sep+1==cf.ep && sind+1==cf.seson && leng==cf.lenguage1))
												{
													new ShowMediaPlayer(f,cf.name2,email,0000,sind+1,sep+1,leng);
													if(em!=null && em.isDisplayable())
													{
														em.mediaPlayer().controls().stop();
								                        em.release();
								                        scheduler.shutdownNow();
													}
													cf.dispose();
												}
											}
										});
									 
				                    em.mediaPlayerFactory().media().newMedia(tf.getAbsolutePath(),":fastseek");
									em.mediaPlayer().media().play(tf.getAbsolutePath());
									revalidate();
									repaint();
									try
									{
										Thread.sleep(1000);
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
									 revalidate();
				  					  repaint();
				  					 
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
  private void showData1()
  {
	  int m1=0;
		
		for(;m1!=1;)
	   	{
	   		if(cf.name.indexOf(39)==-1)
	   	    {
	   	    	m1=1;
	   	    	break;
	   	    }
	   		int ind=cf.name.indexOf(39);
	   		cf.name=cf.name.substring(0,ind)+cf.name.substring(ind+1);
	    
	   	}
	   	if(cf.name.indexOf(58)!=-1)
	  	    {
			int ind=cf.name.indexOf(58);
			cf.name=cf.name.substring(0,ind)+cf.name.substring(ind+1);  
	  		}
			
		      	 
		 ChromeOptions co= new ChromeOptions();
	       co.setCapability("goog:loggingPrefs", Map.of(LogType.PERFORMANCE,java.util.logging.Level.ALL));
	       co.addArguments("--headless=new");
	       co.addArguments("window-size=1920,1080");
	       co.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
	      co.addArguments("user-agent=Mozilla/5.0 (Linux; Android 10; Mobile) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.79 Mobile Safari/537.36");
	       WebDriver wd=new ChromeDriver(co);
	       wd.get("https://pcmirror.cc/pv/");
			WebElement inputser= wd.findElement(By.cssSelector("#root div.fixed.left-0.top-0.w-full.bg-transparent div div.ml-auto.flex.items-center div.cursor-pointer.z-10.group.search.cursor-pointer.flex.aspect-square.items-center.justify-center.rounded-full.bg-transparent.transition-all.ease-in-out div"));
	      JavascriptExecutor jse=(JavascriptExecutor) wd;
	     jse.executeScript("arguments[0].click();",inputser);
	     WebElement input= wd.findElement(By.cssSelector("div.relative.flex.w-full.items-center.bg-zinc-700 input"));
	     input.sendKeys(cf.name);
	     try
	     {
			Thread.sleep(8000);
		} 
	     catch (InterruptedException e) 
	     {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     int chn=0;
	     WebElement resp=wd.findElement(By.cssSelector("div.grid.absolute.overflow-auto"));
	     List<WebElement> resa=resp.findElements(By.cssSelector("div.p-4.flex"));
//	     System.out.println(resa.size());
	     if(resa.size()==0)
	     {
	    	 SwingUtilities.invokeLater(new Runnable(){
      			
      			@Override 
      			public void run()
      			{
						JOptionPane.showMessageDialog(cf, "Series Will Be Available Soon!");
						scheduler.shutdownNow();
		                cf.dispose();
		                f.setVisible(true);
            }
      		});
	     }
	     else
	     {
	    	   for(WebElement re : resa)
	  	     {
	  	    	 WebElement im=re.findElement(By.cssSelector("img"));
	  	    	 String st=im.getAttribute("alt");
	  	    	 if(name2.toUpperCase().equalsIgnoreCase(st))
	  	    	 {
	  	    		 chn=1;
	  	    		 jse.executeScript("arguments[0].click();", re);
	  	    		 break;
	  	    	 }
	  	     }
	  	     if(chn==0)
	  	     {
	  	    	 for(WebElement re : resa)
	  		     {
	  		    	 WebElement im=re.findElement(By.cssSelector("img"));
	  		    	 String st=im.getAttribute("alt");
	  		    	 if(st.toUpperCase().contains(name2.toUpperCase()))
	  		    	 {
	  		    		 chn=1;
	  		    		 jse.executeScript("arguments[0].click();", re);
	  		    		 break;
	  		    	 }
	  		     }
	  	     }
	  	     if(chn==0)
	  	     {
	  	    	SwingUtilities.invokeLater(new Runnable(){
      			
      			@Override 
      			public void run()
      			{
						JOptionPane.showMessageDialog(cf, "Series Will Be Available Soon!");
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
					Thread.sleep(4000);
				} catch (InterruptedException e) 
	  	    	{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	  	    	WebElement wef=wd.findElement(By.cssSelector("div.flex.justify-end.relative.mx-auto p"));
	  	    	jse.executeScript("arguments[0].click();",wef);
	  	    	try 
	  	    	{
					Thread.sleep(2000);
				} 
	  	    	catch (InterruptedException e) 
	  	    	{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	  	    	List<WebElement> ln= wd.findElements(By.cssSelector("#root main div.absolute.top-0.left-0.w-full.h-full.z-50 div div div div .swiper-slide"));
//	  	    	System.out.println(ln.size());
	  	    	for(WebElement lnn : ln)
	  	    	{
	  	    		lenguagedata.add((String) (jse.executeScript("return(arguments[0].textContent);",lnn)));
//	  	    		System.out.println((String) (jse.executeScript("return(arguments[0].textContent);",lnn)));
	  	    	} 
	  	    	List<WebElement> ss=wd.findElements(By.cssSelector("div.right-0.backdrop-blur-md p"));
	    		int i=1;
	  	    	for(WebElement ssi:ss)
	    		{
	    			String snm=ssi.getText();
	    			snm=snm.substring(snm.indexOf('(')+1,snm.indexOf(')'));
	    			epdata.put(i,Integer.parseInt(snm));
	    			i+=1;
	    		}
	  	    	i=0;
	  	     }
//	  	     System.out.println(epdata+","+lenguagedata);
		      wd.close();
	          
	}
  }
  private void showData2()
  {
	  int m1=0;
		
	   	for(;m1!=1;)
	   	{
	   		if(cf.name.indexOf(39)==-1)
	   	    {
	   	    	m1=1;
	   	    	break;
	   	    }
	   		int ind=cf.name.indexOf(39);
	   		cf.name=cf.name.substring(0,ind)+cf.name.substring(ind+1);
	    
	   	}
	   	if(cf.name.indexOf(58)!=-1)
	  	    {
			int ind=cf.name.indexOf(58);
	  		cf.name=cf.name.substring(0,ind)+cf.name.substring(ind+1);  	
	  		}
			
		      	 
		       ChromeOptions co= new ChromeOptions();
		       co.setCapability("goog:loggingPrefs", Map.of(LogType.PERFORMANCE,java.util.logging.Level.ALL));
		       co.addArguments("--headless=new");
		       co.addArguments("window-size=1920,1080");
		       co.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
		      co.addArguments("user-agent=Mozilla/5.0 (Linux; Android 10; Mobile) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.79 Mobile Safari/537.36");
		     WebDriver wd=new ChromeDriver(co);
		     wd.get("https://hdmovie2.do");
				
				WebElement input= wd.findElement(By.cssSelector("#searchformpage #story"));
//		      System.out.println(input.getAttribute("placeholder"));
		      WebElement search= wd.findElement(By.cssSelector(".search_page_form #searchformpage button span"));
//		      System.out.println(input.getAttribute("type"));
		      JavascriptExecutor jse=(JavascriptExecutor) wd;
		      jse.executeScript("arguments[0].value='"+cf.name+"';",input);
		      String str=(String) jse.executeScript("return arguments[0].value;",input);
//		      System.out.println(str);
		      jse.executeScript("arguments[0].click();",search);
		      WebDriverWait wait= new WebDriverWait(wd,Duration.ofSeconds(15));
//		      wait.until(ExpectedConditions.urlContains("newPagePartOfUrl"));
		      WebElement item=wd.findElement(By.cssSelector("#contenedor div.module div.content.right.normal div"));
		      List<WebElement> art=item.findElements(By.cssSelector("article.item.movies"));
		      WebElement videourl;
//		      DevTools dtv=((ChromeDriver)wd).getDevTools();
//		         dtv.createSession();
//		         dtv.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
//		         dtv.addListener(Network.requestWillBeSent(),(request)->{
//		      	   Request req=request.getRequest();
////		      	   System.out.println(req.getUrl().toString());
//		      	   if(req.getUrl().contains(".mp4") || req.getUrl().contains(".m3u8"))
//		      	   {     
//		      		  System.out.println(req.getUrl().toString());
//		   	   }
//		         });
		      int cm=0;
		      for(WebElement ec : art)
		      {
		      	videourl=ec.findElement(By.cssSelector("div.data h3 a"));
		      	String nm=(String)jse.executeScript("return(arguments[0].textContent);",videourl);
		      	nm=nm.toUpperCase();
		      	if(nm.contains(cf.name.toUpperCase()))
		      	{
		      		
		      		jse.executeScript("arguments[0].click();",videourl);
			      	new WebDriverWait(wd,Duration.ofSeconds(10));
			      	
			      	cm=1;
			      	break;	
		      	}

		      	
		      }
//		      System.out.println(cm+" =cm value for "+cf.name);
		      if(cm==1)
		      {
		    	 
		      WebElement iframe= wd.findElement(By.tagName("iframe"));
		      wd.switchTo().frame(iframe);
		     
	         List<WebElement> ses=wd.findElements(By.cssSelector("hdvbplayer:nth-child(15) hdvbplayer hdvbplayer"));
	       int scc=0;
//	       System.out.println(ses.size()+"season");
	         for(WebElement si:ses)
	        {
	        	 jse.executeScript("arguments[0].click();",si);
	        	 try 
	        	 {
					Thread.sleep(2000);
				} 
	        	 catch (InterruptedException e)
	        	 {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	 List<WebElement> eps=wd.findElements(By.cssSelector("hdvbplayer:nth-child(14) hdvbplayer hdvbplayer"));
	 	       if(scc>=1)
	 	       {
	 	    	  epdata.put(scc, eps.size()-1);
//	 	    	 System.out.println(eps.size()+" epcount of season="+scc);
	 	       }
	 	       
	 	        scc+=1;
	        } 
	         List<WebElement> len=wd.findElements(By.cssSelector("hdvbplayer:nth-child(13) hdvbplayer hdvbplayer"));
	     
	         for(WebElement lnm:len)
	         {
	        	 
	        	 try
	        	 {
	        		 String nm1=(String)jse.executeScript("return(arguments[0].textContent);",lnm);
//	          	  System.out.println(nm1);
	             lenguagedata.add(nm1);
	        	 }
	        	 catch(Exception e)
	        	 {
	        		 break;
	        	 }
	        	
	         }
	         lenguagedata.remove(0);
		      
  }
//		      System.out.println(lenguagedata+","+epdata);
		   wd.close();    
  }
}
