package ott_app;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.LoggingPermission;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaListPlayerComponent;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;

public class MediaDetail extends JFrame implements Runnable
{
	JFrame cf,f;
	int m=0;
	URL imdb;
	JPanel loc;
	 JLabel tilo;
	 float sdu=0f;
	 int sseson=0,sep=0;
	 
	String email,name,director,writer,casts,trailerurl,description,sleng=null;
	static String name1;
	EmbeddedMediaPlayerComponent em=null;
	String poser;
	ImageIcon imag;
	MovieObject moviedata;
	ScheduledExecutorService scheduler;
	Thread th;
	boolean isshow=true;
    public MediaDetail(String name,JFrame f,String email)
    {
    	super(name);
    	this.name=name;
    	getContentPane().setBackground(new Color(0,0,10));
    	repaint();
    	this.email=email;
    	this.f=f;
    	setLayout(null);
    	loc=new JPanel();
    	loc.setLayout(new BorderLayout());
    	cf=this;
    	cf.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we)
            {
            	    m=1;
            	
            		em.mediaPlayer().controls().stop();
            		em.release();
            		scheduler.shutdownNow();
                cf.dispose();
                f.setVisible(true);
            }
            
    	});
		
		
    	th= new Thread(this,"main");
    	try 
    	{
			imdb= new URL("https://www.imdb.com");
		} 
    	catch (MalformedURLException e)
    	{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	scheduler= Executors.newScheduledThreadPool(5);
    	scheduler.schedule(new Runnable()
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
							cf.setLayout(null);
							cf.setIconImage(Image_Box.LOGO);
							cf.setSize(Toolkit.getDefaultToolkit().getScreenSize());
							cf.addMouseListener(new MouseAdapter() 
							{
							     @Override
							     public void mouseClicked(MouseEvent e)
							     {
							    	 System.out.println(e.getX()+","+e.getY());
							     }
							});
							
						   
							EmbeddedMediaListPlayerComponent em= new EmbeddedMediaListPlayerComponent();
						  loc.add(em);
						  loc.setBounds((cf.getWidth()/2)-35,(cf.getHeight()/2)-35,70, 70);
				             cf.add(loc);
							cf.getContentPane().setBackground(new Color(0,0,10));
							ImageIcon logo = new ImageIcon(Image_Box.LOGO.getScaledInstance(90, 65, Image.SCALE_SMOOTH));
			                JLabel logoi = new JLabel(logo);
			                logoi.setBounds(10, 10, 90, 65);
			                cf.add(logoi);
							cf.setLayout(null);
							cf.setVisible(true);
			                em.mediaPlayer().media().play("C:\\Users\\DELL\\Downloads\\loading.mp4");
			                em.mediaPlayer().events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
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
				catch (Exception e) 
				{
					JOptionPane.showMessageDialog(cf,"Somthing Went Wrong Please Try Again!");
					dispose();
					f.setVisible(true);
					e.printStackTrace();
				} 
			}
		}
    	, 0, TimeUnit.MICROSECONDS);
    ScheduledFuture<MovieObject>sf=scheduler.schedule(new Callable<MovieObject>()
    	{
    		public MovieObject call()
    		{
    			try 
    			{
    				cf.getContentPane().setBackground(new Color(0,0,10));
    		    	repaint();
				Connection con=	DataBase.getConnection();
					CallableStatement cl=con.prepareCall("call movieavailiblity(?)");
					cl.setString(1,name);
					ResultSet rs=cl.executeQuery();
					if(rs.next())
					{
						ByteArrayInputStream bi;
						byte[]data=rs.getBytes("data");
						bi= new ByteArrayInputStream(data);
						ObjectInputStream oi= new ObjectInputStream(bi);
						moviedata=(MovieObject)oi.readObject();
						director=moviedata.getDirector();
						casts=moviedata.getCasts();
						writer=moviedata.getWriter();
						description=moviedata.getDescription();
						trailerurl=moviedata.getTrailer();
						poser=moviedata.getImage();
						isshow=moviedata.getShow();
						th.start();
						System.out.println(isshow+"abhi");
					}
					else
					{
						rs.close();
						int m1=0;
						
						name1=name.substring(0);
				    	for(;m1!=1;)
				    	{
				    		if(name1.indexOf(39)==-1)
				    	    {
				    	    	m1=1;
				    	    	break;
				    	    }
				    		int ind=name1.indexOf(39);
				    		name1=name.substring(0,ind)+name.substring(ind+1);
				    		
				    	    
				    	}
				    	name1=name1.toUpperCase();
				    	int ind;
				    	if((ind=name1.indexOf(58))!=-1)
				    	{
				    		name1=name.substring(ind+1);

				    	}
						try
						{
							   ChromeOptions co= new ChromeOptions();
							   co.addArguments("--headless=new");
						       co.setCapability("goog:loggingPrefs", Map.of(LogType.PERFORMANCE,java.util.logging.Level.ALL));
						       
						       co.addArguments("window-size=1920,1080");
						       co.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
						      
							  WebDriver wd= new ChromeDriver(co);
							  wd.get("https://www.imdb.com");
							  new WebDriverWait(wd,Duration.ofSeconds(10)).until((driver)->(driver.findElement(By.id("suggestion-search")).isDisplayed()));
//							  w1.until(ExpectedConditions.visibilityOfElementLocated(By.id("suggestion-search")));
//							  File screenshot = ((TakesScreenshot) wd).getScreenshotAs(OutputType.FILE);
//							  Path destination = Paths.get("C:\\Users\\abhis\\OneDrive\\Pictures\\screen.PNG");
//							  Files.copy(screenshot.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);
//							  WebElement input = w1.until(ExpectedConditions.visibilityOfElementLocated(By.id("suggestion-search")));
							  WebElement input=wd.findElement(By.id("suggestion-search"));
//							  FluentWait<WebDriver> fw=new FluentWait<WebDriver>(wd).withTimeout(Duration.ofSeconds(30)).pollingEvery(Duration.ofSeconds(5)).ignoring(NoSuchElementException.class);
//    
//							  WebElement input=fw.until(new Function<WebDriver,WebElement>()
//							  {
//								  @Override
//								  public WebElement apply(WebDriver wd)
//								  {
//									  return(wd.findElement(By.id("suggestion-search")));
//									  
//								  }
//							});
							  JavascriptExecutor js= (JavascriptExecutor)wd;
							  js.executeScript("arguments[0].value='"+name1+"';",input);
							  
							  WebElement searchbtn=wd.findElement(By.id("suggestion-search-button"));
							  js.executeScript("arguments[0].click();",searchbtn);
							  new WebDriverWait(wd,Duration.ofSeconds(5));
							  
							  WebElement select2= wd.findElement(By.cssSelector("#__next main div.ipc-page-content-container.ipc-page-content-container--full div.ipc-page-content-container.ipc-page-content-container--center section div div.ipc-page-grid__item.ipc-page-grid__item--span-2 section:nth-child(4) div ul"));
							  List<WebElement> select1=select2.findElements(By.cssSelector("li.ipc-metadata-list-summary-item.ipc-metadata-list-summary-item--click.find-result-item.find-title-result"));
							  int ch=0;
							  for(WebElement sel:select1)
							  {
							   WebElement select=sel.findElement(By.cssSelector("div.ipc-metadata-list-summary-item__c div a"));
//								  WebElement nm=select.findElement(By.cssSelector("a"));
								  String titl=(String)js.executeScript("return arguments[0].textContent;",select);
								  System.out.println(titl);
								  if(titl.equalsIgnoreCase(name))
								  {
									  js.executeScript("arguments[0].click();",select);
								      new WebDriverWait(wd,Duration.ofSeconds(8));
									  ch=1;
									  break;
								  }
							  }
							  if(ch==0)
							  {
								  WebElement select=wd.findElement(By.cssSelector("#__next main div.ipc-page-content-container.ipc-page-content-container--full div.ipc-page-content-container.ipc-page-content-container--center section div div.ipc-page-grid__item.ipc-page-grid__item--span-2 section:nth-child(4) div ul li:nth-child(1) div.ipc-metadata-list-summary-item__c div a"));
								  js.executeScript("arguments[0].click();",select);
							      new WebDriverWait(wd,Duration.ofSeconds(8));
							  }
							 
							  WebElement trailer;
							  try
							  {
								  trailer=wd.findElement(By.cssSelector("div.jw-wrapper.jw-reset div.jw-media.jw-reset video"));

							  }
							  catch(Exception e)
							  {
//								  e.printStackTrace();
								  try
								  {
									  trailer=wd.findElement(By.cssSelector("#imdbnext-vp-jw-inline div.jw-wrapper.jw-reset div.jw-media.jw-reset video"));  
									
								  }
								  catch(Exception e1)
								  {
									
									  try
									  {
										  trailer=wd.findElement(By.cssSelector("div.jw-media.jw-reset video")); 
										 
										  
									  }
									  catch(Exception e2)
									  {
										 
										  try
										  {
											  trailer=wd.findElement(By.cssSelector("div.jw-wrapper div.jw-media video.jw-video")); 
											
										  }
										  catch(Exception e3)
										  {
											 
											  try
											  {
												  trailer=wd.findElement(By.cssSelector("div.jw-reset div.jw-reset video")); 
											  }
											  catch(Exception e4)
											  {
												  try
												  {
													trailer=wd.findElement(By.cssSelector("div div video")); 
												  }
												  catch(Exception e5)
												  {
													  try {
														  trailer=wd.findElement(By.cssSelector("video")); 
													  }
													  catch (Exception e6)
													  {
														  trailer=null;
														  System.out.println("no video component");
														  e5.printStackTrace();
													  }
													  
												  }
											  }
										  }
									  }
								  }
								

							  }
							  String traler="";
							  if(trailer!=null)
							  {
								  traler=(String)js.executeScript("return(arguments[0].getAttribute('src'));",trailer);
							  }
							  else
							  {
								  traler="C:\\Users\\DELL\\Downloads\\nodata.mp4";
							  }
							  trailerurl=traler;
							  WebElement ul=wd.findElement(By.cssSelector("#__next main div section.ipc-page-background.ipc-page-background--base section div:nth-child(5) section section div div div section div div ul"));
							 List<WebElement> lis=ul.findElements(By.tagName("li"));
							 int cou=1;
							 String directorl="",writerl="",stars="";
							 for(WebElement li:lis)
							 {
								 String datas="";
								 List<WebElement> liin= li.findElements(By.cssSelector("div ul li a"));
								 for(WebElement lia:liin) 
								 {
									datas=(String) js.executeScript("return (arguments[0].textContent);", lia); 
								 }
								 if(cou==1)
								 {
									directorl=datas; 
								 }
								 else if(cou==2)
								 {
									 writerl=datas;
								 }
								 else
								 {
									 stars=datas;
								 }
								 cou+=1;
							 }
							director=directorl;
							 writer=writerl;
							 casts=stars;
							WebElement des=wd.findElement(By.cssSelector("#__next main div section.ipc-page-background.ipc-page-background--base section div:nth-child(5) section section div div div section p span"));
							String content=(String)js.executeScript("return(arguments[0].textContent);", des);
							description=content;
							try
							{
								WebElement showc=wd.findElement(By.cssSelector("#__next main div section.ipc-page-background.ipc-page-background--base section div:nth-child(5) section section div div div:nth-child(1) a span.episode-guide-text"));
							}
							catch(Exception e)
							{
								System.out.println("not a show");
								isshow=false;
							}
							System.out.println(isshow+"abhi yes");
							WebElement lin=null;
							try
							{
								lin= wd.findElement(By.cssSelector("#__next main div section.ipc-page-background.ipc-page-background--base section div:nth-child(5) section section div div div div.ipc-poster a"));

							}
							catch(Exception e)
							{
								lin= wd.findElement(By.cssSelector("#__next main div section.ipc-page-background.ipc-page-background--base section div:nth-child(5) section section div div div div.ipc-poster a"));
								e.printStackTrace();
							}
							    js.executeScript("arguments[0].click();",lin);
							    new WebDriverWait(wd,Duration.ofSeconds(3));
							    WebElement eep=null;
							    try
							    {
							    	eep=wd.findElement(By.cssSelector( ".ipc-page-content-container img"));	
							    }
							    catch(Exception e)
							    {
							    	eep=wd.findElement(By.cssSelector( "#__next main div.ipc-page-content-container.ipc-page-content-container--full div.kTCNvh.media-viewer div img"));		  
							    }
							      poser=(String)js.executeScript("return(arguments[0].getAttribute('src'));",eep);
									
							th.start();
							con.setAutoCommit(false);
							
							MovieObject mov=new MovieObject(poser, trailerurl, description, writer, director, casts,isshow);
							ByteArrayOutputStream bo=new ByteArrayOutputStream();
							ObjectOutputStream oe= new ObjectOutputStream(bo);
							oe.writeObject(mov);
							byte[]bm=bo.toByteArray();
							PreparedStatement ps= con.prepareStatement("insert into moviedata(name,data) values(?,?)");
							ps.setString(1,name);
							ps.setBytes(2, bm);
							ps.executeUpdate();
							con.commit();
						    wd.close();
						    
							  
						} 
						catch (Exception e) 
						{
							
							JOptionPane.showMessageDialog(cf, "Somthing Went Wrong With Server Connectivity Please Try");

							cf.dispose();
							f.setVisible(true);
							e.printStackTrace();
						} 
						
					}
					
					con.close();
					System.out.println("abhi");
				}
    			catch (Exception e) 
    			{
					
					JOptionPane.showMessageDialog(cf, "Somthing Went Wrong With Server Connectivity Please Try After Sometime");

					cf.dispose();
					f.setVisible(true);
					e.printStackTrace();
				}
    			return moviedata;
    		}
    		
		},-10,TimeUnit.MICROSECONDS);
    }
	@Override
	public void run() 
	{
		ScheduledFuture<?> sf5= scheduler.schedule(new Callable<Void>()
		{
			@Override
			public Void call()
			{
				if(isshow)
				{
					
					boolean ch;
					try 
					{
						Connection con=DataBase.getConnection();
						con.setAutoCommit(false);
						CallableStatement cal=con.prepareCall("call chackshowhistory(?,?)");
						cal.setString(1,name);
						cal.setString(2, email);
					    ch=cal.execute();
					    if(ch)
					    {
					    	ResultSet rs=cal.getResultSet();
					    	if(!rs.next())
					    	{
					    		sdu=0.0f;
					    	}
					    	else
					    	{
					    		sdu=rs.getFloat("duration");
					    		sep=rs.getInt("ep");
					    		sseson=rs.getInt("season");
					    		sleng=rs.getString("lenguage");
					    	}
					    	rs.close();
					    }
					    con.commit();
					    con.close();
					    
					} 
					catch (SQLException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				return null;
			}
		},1,TimeUnit.MICROSECONDS);
		
		ScheduledFuture<Float> sf1= scheduler.schedule(new Callable<Float>()
		{
			@Override
			public Float call()
			{
				Float l=null;
				boolean ch;
				try 
				{
					Connection con=DataBase.getConnection();
					con.setAutoCommit(false);
					CallableStatement cal=con.prepareCall("call chackhistory(?,?)");
					cal.setString(1,email);
					cal.setString(2, name);
				    ch=cal.execute();
				    if(ch)
				    {
				    	ResultSet rs=cal.getResultSet();
				    	if(!rs.next())
				    	{
				    		l=0.0f;
				    	}
				    	else
				    	{
				    		l=rs.getFloat("duration");
				    	}
				    	rs.close();
				    }
				    con.commit();
				    con.close();
				    
				} 
				catch (SQLException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return l;
			}
		},1,TimeUnit.MICROSECONDS);
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				
				@Override
				public void run()
				{
					 Font fn= new Font("Arial",Font.BOLD,18);
					JPanel jp= new JPanel();
					jp.setLayout(new BorderLayout());
					 em= new EmbeddedMediaPlayerComponent();
	     			jp.setBounds((cf.getWidth()/3), 8,cf.getWidth()/3, 350);
	     			String tit =name.substring(0);
	     			if(tit.length()<30)
	     			{
	     				tit=">>>>>>>>"+name+"<<<<<<<<<";
						if(tit.indexOf(58)!=-1)
						{
							tit=tit.substring(tit.indexOf(58)+1);
						}
						if(tit.length()<30)
						{
							if((30-tit.length())==1)
									{
								tit=">"+tit;
									}
							else if((30-tit.length())%2!=0)
							{
								int less=(30-tit.length());
								for(int i=1;i<=(less/2);i++)
								{
									tit=">"+tit+"<";
								}
								tit=tit+"<";
								
							}
							else if((30-tit.length())%2==0)
							{
								for(int i=1;i<=((30-tit.length())/2);i++)
								{
									tit=">"+tit+"<";
								}
							}
						}
						else
						{
							if((tit.length()-30)==1)
							{
						       tit=tit.substring(1);
							}
					else if((tit.length()-30)%2!=0)
					{
						int less=(tit.length()-30);
						for(int i=0;i<=(less/2);i++)
						{
							tit=tit.substring(0,tit.length()-2);
						}
						for(int i=1;i<=(less/2);i++)
						{
							tit=tit.substring(1);
						}
					}
					else if((30-tit.length())%2==0)
					{
						for(int i=1;i<=((tit.length()-30)/2);i++)
						{
							tit=tit.substring(1);
							tit=tit.substring(0,tit.length()-2);
						}
					}
						}
	     			}
					
			        JLabel title= new JLabel(tit);
			        JButton pp= new JButton("pause");
			        pp.setForeground(new Color(0,0,0,0));
			        pp.setOpaque(false);
			        pp.setBackground(new Color(0,0,0,0));
			        ImageIcon play,pause;
			        play=new ImageIcon(Image_Box.PLAY.getScaledInstance(50,40,Image.SCALE_SMOOTH));
			        pause=new ImageIcon(Image_Box.PAUSE.getScaledInstance(50,40,Image.SCALE_SMOOTH));
			        pp.setIcon(pause);
			        pp.setBorderPainted(false);
			        pp.setPreferredSize(new Dimension(60,50));
			        JPanel botp= new JPanel();
			        JLabel tr= new JLabel("Trailer Story : ");
			        tr.setFont(fn);
			        tr.setForeground(new Color(192,192,192));
			        botp.add(tr);
			        botp.setBackground(new Color(0,0,10));
			        botp.add(pp);
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
					    	   em.mediaPlayer().controls().play();
					    	   b.setText("pause");
					    	   b.setIcon(pause);
					       }
							
						}
					});
			       
			        ImageIcon im1;
					try 
					{
						im1 = new ImageIcon(new URL(poser));
						Image im=im1.getImage();
				        imag= new ImageIcon(im.getScaledInstance(240,300,Image.SCALE_SMOOTH));
				        tilo=new JLabel(imag);
					} 
					catch (MalformedURLException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    	
			        JTextArea des= new JTextArea(description);
			        des.setOpaque(false);
			        des.setBackground(new Color(0,0,0,0));
			        des.setLineWrap(true);
			        des.setWrapStyleWord(true);
			        des.setBounds(288,(cf.getHeight()/2)+10,cf.getWidth()-360, 60);
			        des.setForeground(new Color(192,192,192));
			        des.setFont(new Font("Arial",Font.BOLD,22));
			        des.setEditable(false);
			       
			        JPanel detl= new JPanel();
			        detl.setOpaque(false);
			        detl.setBackground(new Color(0,0,0,0));
			        detl.setLayout(new GridLayout(3,2,5,5));
			        detl.setBounds(288,(cf.getHeight()/2)+80,400,100);
			        JLabel dir= new JLabel("Director :");
			        dir.setFont(fn);
			        dir.setForeground(new Color(192,192,192));
			        detl.add(dir);
			        JLabel dirn= new JLabel(director);
			        dirn.setFont(fn);
			        dirn.setForeground(new Color(192,192,192));
			        detl.add(dirn);
			        JLabel wri= new JLabel("Writer :");
			        wri.setFont(fn);
			        wri.setForeground(new Color(192,192,192));
			        detl.add(wri);
			        JLabel wrin= new JLabel(writer);
			        wrin.setFont(fn);
			        wrin.setForeground(new Color(192,192,192));
			        detl.add(wrin);
			        JLabel cas= new JLabel("Casts :");
			        cas.setFont(fn);
			        cas.setForeground(new Color(192,192,192));
			        detl.add(cas);
			        JLabel casn= new JLabel(casts);
			        casn.setFont(fn);
			        casn.setForeground(new Color(192,192,192));
			        detl.add(casn);
			        JButton start,resume;
			        start= new JButton("Watch Now");
		             start.setForeground(Color.white);
		             start.setBackground(new Color(0,0,30));
		             resume= new JButton("Continue Watching");
		             resume.setForeground(Color.white);
		             resume.setBackground(new Color(0,0,30));
		             resume.setFont(fn);
		             start.setFont(fn);
		            JPanel btnp= new JPanel();
			        btnp.setLayout(new GridLayout(2,1,6,6));
			        btnp.setOpaque(false);
			        btnp.setBackground(new Color(0,0,0,0));
			        btnp.setBounds(288,(cf.getHeight()/2)+210,300,80);
			        btnp.add(start);
			       start.addActionListener(new ActionListener() 
			       {
					
					@Override
					public void actionPerformed(ActionEvent e) 
					{
						if(isshow)
						{
							new ShowMediaPlayer(f,name,email,0000,1,1,sleng);
						}
						else
						{
							new MediaPlayear(f, name, email,0000);	
						}
	            	    m=1;
	            		em.mediaPlayer().controls().stop();
	            		em.release();
	            		scheduler.shutdownNow();
	                    cf.dispose();
					}
				   });
			       resume.addActionListener(new ActionListener() 
			       {
					
					@Override
					public void actionPerformed(ActionEvent e) 
					{
						float l=0.0f;
						try 
						{
							l = sf1.get();
							sf5.get();
						} 
						catch (Exception e1)
						{
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} 
						if(isshow)
						{
							new ShowMediaPlayer(f,name,email,sdu,sseson,sep,sleng);
						}
						else
						{
							new MediaPlayear(f, name, email,l);	
						}
	            		em.mediaPlayer().controls().stop();
	            		em.release();
	            		scheduler.shutdownNow();
	                    cf.dispose();
					}
				   });
			        tilo.setBounds(15,(cf.getHeight()/2),240,300);
			        tilo.setBorder(BorderFactory.createLineBorder(new Color(192,192,192),2));
			        title.setForeground(Color.white);
			        title.setFont(new Font("Arial",Font.CENTER_BASELINE,32));
			        title.setBounds((cf.getWidth()/2)-260,(cf.getHeight()/3)+60,680,80);
			      JLabel  developer= new JLabel("Developed by:Abhishek Jain");
		             developer.setFont(new Font("Arial",Font.BOLD,10));
		             developer.setForeground(Color.white);
		             developer.setBounds(cf.getWidth()-(cf.getWidth()/5), 2, 300, 15);
					em.mediaPlayer().events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() 
					{
						@Override
						public void finished(MediaPlayer mp)
						{
							SwingUtilities.invokeLater(new Runnable()
							{
								
								@Override
								public void run()
								{
									mp.media().play(trailerurl);
									
								}
							});
						}
					});
					loc.remove(em);
					cf.getContentPane().remove(loc);
					revalidate();
					repaint();
					em.mediaPlayer().video().setAspectRatio("16:9");
					jp.setPreferredSize(new Dimension(665,300));
					try
					{
						float du=sf1.get();
						sf5.get();
						if(du!=0.0f || sseson!=0)
						{
							btnp.add(resume);
							revalidate();
							repaint();
						}
					} catch (Exception e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					m=1;
					
					cf.add(developer);
					jp.add(em);
					jp.add(BorderLayout.SOUTH,botp);
					cf.add(jp);
				    cf.add(tilo);
				    cf.add(des);
				    cf.add(detl);
					cf.add(title);
					cf.add(btnp);
					em.mediaPlayer().media().play(trailerurl);
					revalidate();
					repaint();
				}
			});
		} 
		catch (Exception e) 
		{
			JOptionPane.showMessageDialog(cf, "Somthing Went Wrong With Server Connectivity Please Try After Sometime");
			cf.dispose();
			f.setVisible(true);
			e.printStackTrace();
		}
		
	}
}
