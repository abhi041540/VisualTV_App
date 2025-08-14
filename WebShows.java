package ott_app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
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
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.support.ui.WebDriverWait;

import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;

public class WebShows extends JFrame implements ActionListener
{
	JFrame cf=this,f;
	String email;
	Map<String,String>homedata;
	ScheduledExecutorService schedular;
	EmbeddedMediaPlayerComponent em;
	JPanel loc;
	JSplitPane jsp;
	JTextField t1;
	 String nm;
	 int m=0;
	 JScrollPane rps;
	 public WebShows(String str, String email, JFrame f) {
		    super(str);
		    this.email = email;
		    jsp = new JSplitPane();
		    schedular = Executors.newScheduledThreadPool(5);
		    loc = new JPanel();
		    cf.getContentPane().setBackground(new Color(0, 0, 10));
		    cf.setVisible(true);
            revalidate();
            repaint(); 
		    addWindowListener(new WindowAdapter() {
		        @Override
		        public void windowClosing(WindowEvent we) {
		            f.setVisible(true);
		            schedular.shutdownNow();
		            cf.dispose();
		        }
		    });

		    ScheduledFuture<Map<String, String>> sf1 = schedular.schedule(new Callable<Map<String, String>>() {
		        @Override
		        public Map<String, String> call() throws Exception {
		           
		            Map<String, String> data = new LinkedHashMap<>();
		            try {
		            	Document doc = Jsoup.connect("https://editorial.rottentomatoes.com/guide/popular-tv-shows/").get();
		                Elements art = doc.select("#article_main_body div.panel-rt.panel-box.article_body div.articleContentBody div.countdown-item");
		             
		                for (Element el : art) {
		                    Element maindata = el.selectFirst("div a div img");
		                    String image = maindata.attr("src");
		                    String name = el.selectFirst("div div div h2 a").text();
//		                    System.out.println(name);
		                    data.put(name, image);
		                }
		            } catch (Exception e) {
		                e.printStackTrace();
		            }
		            return data;
		        }
		    }, 10, TimeUnit.MICROSECONDS);

		    ScheduledFuture<?> sf2 = schedular.schedule(new Runnable() {
		        @Override
		        public void run() {
		            SwingUtilities.invokeLater(() -> {
		                cf.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		                cf.setIconImage(Image_Box.LOGO);
		                cf.setLayout(null);
		                loc.setLayout(new BorderLayout());
		                em = new EmbeddedMediaPlayerComponent();
		                loc.add(em);
		                loc.setBounds((cf.getWidth() / 2) - 35, (cf.getHeight() / 2) - 35, 70, 70);
		                cf.add(loc);
		                cf.setVisible(true);
		                cf.getContentPane().setBackground(new Color(0, 0, 10));
		                em.mediaPlayer().media().play("C:\\Users\\DELL\\Downloads\\loading.mp4");
		                em.mediaPlayer().events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
		                    @Override
		                    public void finished(MediaPlayer me) {
		                        SwingUtilities.invokeLater(() -> me.media().play("C:\\Users\\DELL\\Downloads\\loading.mp4"));
		                    }
		                });
		                revalidate();
		                repaint();
		            });
		        }
		    }, 0, TimeUnit.MICROSECONDS);

		    try {
		        homedata = sf1.get();
		    } catch (InterruptedException | ExecutionException e) {
		        SwingUtilities.invokeLater(() -> {
		            if (em.isDisplayable()) {
		                em.mediaPlayer().controls().stop();
		                em.release();
		                schedular.shutdownNow();
		            }
		            JOptionPane.showMessageDialog(cf, "Unstable Server Connection Please Try Again!");
		            cf.dispose();
		            f.setVisible(true);
		        });
		        e.printStackTrace();
		    }

		    SwingUtilities.invokeLater(() -> {
		        jsp.setDividerLocation(cf.getWidth() / 14);
		        jsp.setEnabled(false);
		        jsp.setOrientation(JSplitPane.VERTICAL_SPLIT);
		        jsp.setDividerSize(0);
		        Font fn = new Font("Arial", Font.BOLD, 20);
		        JPanel lp = new JPanel();
		        lp.setLayout(new BorderLayout());
		        lp.setBackground(new Color(0, 0, 10));
		        JLabel logo = new JLabel(new ImageIcon(Image_Box.LOGO.getScaledInstance(90, 65, Image.SCALE_SMOOTH)));
		        logo.setText("vstv");
		        logo.setForeground(new Color(0, 0, 0, 0));
		        logo.setHorizontalTextPosition(SwingConstants.LEFT);
		        lp.add(BorderLayout.WEST, logo);
		        JLabel developer = new JLabel("Developed by AbhishekJain");
		        developer.setPreferredSize(new Dimension(280, 60));
		        developer.setFont(new Font("Arial", Font.BOLD, 14));
		        developer.setForeground(new Color(125, 125, 125));
		        lp.add(BorderLayout.EAST, developer);
		        JPanel search = new JPanel();
		        search.setBackground(new Color(0, 0, 10));
		        t1 = new JTextField(20);
		        t1.setFont(fn);
		        search.add(t1);
		        JButton scr = new JButton(new ImageIcon(Image_Box.SEARCH.getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
		        scr.setPreferredSize(new Dimension(50, 50));
		        search.add(scr);
		        scr.setBackground(new Color(0, 0, 10));
		        search.setPreferredSize(new Dimension((cf.getWidth() / 3), cf.getWidth() / 14));
		        lp.add(BorderLayout.CENTER, search);
		        jsp.setLeftComponent(lp);
		        scr.addActionListener(this);
		        scr.setBorderPainted(false);
		        JPanel rp = new JPanel();
		        rp.setBackground(new Color(0, 0, 10));
		        rp.setLayout(new GridLayout(5, 5, 15, 15));
		        for (Map.Entry<String, String> dv : homedata.entrySet()) {
		            try {
		                ImageIcon img = new ImageIcon(new URL(dv.getValue()));
		                JLabel moviel = new JLabel(new ImageIcon(img.getImage().getScaledInstance(240, 315, Image.SCALE_SMOOTH)));
		                moviel.setPreferredSize(new Dimension(240, 315));
		                moviel.setText(dv.getKey());
		                moviel.setHorizontalTextPosition(SwingConstants.CENTER);
		                moviel.setForeground(new Color(0, 0, 0, 0));
		                moviel.setBorder(BorderFactory.createLineBorder(new Color(125, 125, 125), 2));
		                rp.add(moviel);
		                revalidate();
		                repaint();
		                moviel.addMouseListener(new MouseAdapter() {
		                    @Override
		                    public void mouseClicked(MouseEvent me) {
		                        JLabel l = (JLabel) me.getSource();
		                        new MediaDetail(l.getText(), cf, email);
		                        cf.setVisible(false);
		                    }
		                });
		            } catch (Exception e) {
		                e.printStackTrace();
		            }
		        }
		        rps = new JScrollPane(rp);
		        JScrollBar hs = rps.getHorizontalScrollBar();
		        hs.setUI(new ScrollBar(Color.LIGHT_GRAY, new Color(0, 0, 10)));
		        JScrollBar vs = rps.getVerticalScrollBar();
		        vs.setUI(new ScrollBar(Color.LIGHT_GRAY, new Color(0, 0, 10)));
		        rps.getViewport().setBackground(new Color(0, 0, 10));
		        jsp.setRightComponent(rps);
		        if (em.isDisplayable()) {
		            em.mediaPlayer().controls().stop();
		            em.release();
		        }
		        cf.getContentPane().remove(loc);
		        cf.setLayout(new BorderLayout());
		        revalidate();
		        repaint();
		        cf.add(jsp);
		    });
		}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
	  m=0;
      nm =t1.getText();
      nm=nm.toUpperCase();
      nm=nm.trim();
      if(nm==null || nm.isEmpty() ||nm.equalsIgnoreCase(""))
      {
    	  JOptionPane.showMessageDialog(cf,"Enter Somthing To Search");
    	  t1.setText(null);
      }
      else
      {
    	  t1.setText(null);
    	  em=new EmbeddedMediaPlayerComponent();
    	  loc=new JPanel();
    	  loc.setLayout(new BorderLayout());
    	  loc.setBackground(Color.white);
    	  loc.add(em);
    	  schedular.schedule(()->{
    		  JPanel tp=new JPanel();
    		  tp.setBackground(new Color(0,0,10));
    		  tp.setLayout(null);
    		  jsp.setRightComponent(null);
    		  jsp.setRightComponent(tp);
    		  revalidate();
			  repaint();
    		  loc.setBounds((cf.getWidth()/2)-35, (cf.getHeight()/2)-125,70,70);
    		  tp.add(loc);
    		   revalidate();
				repaint();
				em.mediaPlayer().media().play("C:\\Users\\DELL\\Downloads\\loading.mp4");
				em.mediaPlayer().events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
					@Override
					public void finished(MediaPlayer me)
					{
						SwingUtilities.invokeLater(()->{
							me.media().play("C:\\Users\\DELL\\Downloads\\loading.mp4");
						});
					}
				});
    		  
    	  },0,TimeUnit.MICROSECONDS);
    	  
    	  
    	  SwingUtilities.invokeLater(()->{
    		  ScheduledFuture<Map<String,String>>sf=schedular.schedule(new Callable<Map<String,String>>() 
    		  {
    			  @Override
    			  public Map<String,String> call()
    			  {
    				  Map<String,String> data;
    				data=searchdata2();
    				  return data;
    			  }
    			  
			},3,TimeUnit.MICROSECONDS);
    		  
    		  
    		  JPanel rp= new JPanel();
      		rp.setBackground(new Color(0,0,10));
      		rp.setLayout(new GridLayout(5,5,15,15));
      	  JScrollPane rps1= new JScrollPane(rp);
    	    JScrollBar hs=rps1.getHorizontalScrollBar();
			hs.setUI(new ScrollBar(Color.LIGHT_GRAY,new Color(0,0,10)));
			JScrollBar vs=rps1.getVerticalScrollBar();
			vs.setUI(new ScrollBar(Color.LIGHT_GRAY,new Color(0,0,10)));
    	    rps1.getViewport().setBackground(new Color(0,0,10));
    	    
    	    
           Map<String,String> evd=null;
           try
           {
			evd=sf.get();
		   } 
           catch (Exception e1) 
           {
        	   SwingUtilities.invokeLater(new Runnable() {
   				
   				@Override
   				public void run() 
   				{
   					if(em.isDisplayable())
   					{
   						em.mediaPlayer().controls().stop();
   						em.release();
   						
   					}
   					m=1;
   					JOptionPane.showMessageDialog(cf, "Unstable Server Connection Please Try Again!");
   					jsp.setRightComponent(rps);
   					revalidate();
   					repaint();
   				
   				}
   			});
   			e1.printStackTrace();
		   }
           if(m==0 && evd!=null && evd.size()>0)
           {
        	  
      	    for(Map.Entry<String, String>dv : evd.entrySet())
      	    {
      	    	try
      	    	{
  					ImageIcon img=new ImageIcon(new URL(dv.getValue()));
  					JLabel moviel=new JLabel(new ImageIcon(img.getImage().getScaledInstance(240,315,Image.SCALE_SMOOTH)));
  					moviel.setPreferredSize(new Dimension(240,315));
  					moviel.setText(dv.getKey());
  					moviel.setHorizontalTextPosition(SwingConstants.CENTER);
  					moviel.setForeground(new Color(0,0,0,0));
  					moviel.setBorder(BorderFactory.createLineBorder(new Color(125,125,125),2));
  					rp.add(moviel);
  					revalidate();
  					repaint();
  					moviel.addMouseListener(new MouseAdapter()
  					{
  						@Override
  						public void mouseClicked(MouseEvent me)
  						{
  							JLabel l=(JLabel)me.getSource();
  							new MediaDetail(l.getText(), cf, email);
  							cf.setVisible(false);
  						}
  					});
  				}
      	    	catch (Exception e1) 
      	    	{
  					// TODO Auto-generated catch block
  					e1.printStackTrace();
  				}
      	    	
      	    }
      	  if(em.isDisplayable())
				{
					em.mediaPlayer().controls().stop();
					em.release();
				}
    	    jsp.setRightComponent(rps1);   
    		 revalidate();
    		 repaint();
           }
           else
           {
        	   if(em.isDisplayable())
					{
						em.mediaPlayer().controls().stop();
						em.release();
						
					}
					m=1;
					JOptionPane.showMessageDialog(cf, "No Data Found!");
					jsp.setRightComponent(rps);
					revalidate();
					repaint();
				
           }
    	  });
    	   
      }
	}
	private Map<String, String> searchdata1()
	{
		Map<String, String> mp1=new LinkedHashMap<String, String>();
		 ChromeOptions co= new ChromeOptions();
	       co.setCapability("goog:loggingPrefs", Map.of(LogType.PERFORMANCE,java.util.logging.Level.ALL));
	       co.addArguments("--headless=new");
	       co.addArguments("window-size=1920,1080");
	       co.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
	      
	       WebDriver wd=new ChromeDriver(co);
	     wd.get("https://pcmirror.cc/pv/");
			WebElement inputs= wd.findElement(By.cssSelector("#root div.fixed.left-0.top-0.w-full.bg-transparent div div.ml-auto.flex.items-center div.cursor-pointer.z-10.group.search.cursor-pointer.flex.aspect-square.items-center.justify-center.rounded-full.bg-transparent.transition-all.ease-in-out div"));
        JavascriptExecutor jse=(JavascriptExecutor)wd;
	      jse.executeScript("arguments[0].click();",inputs);
	     WebElement sf= wd.findElement(By.cssSelector("div.relative.flex.w-full.items-center.bg-zinc-700 input"));
	     sf.sendKeys(nm);
	     try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     
	     WebElement resp=wd.findElement(By.cssSelector("div.grid.absolute.overflow-auto"));
	     List<WebElement> el=resp.findElements(By.cssSelector("div.p-4.flex"));
//	      System.out.println(el.size()+"abhi");
	      for(WebElement wel:el)
	      {
	    	  WebElement me=wel.findElement(By.cssSelector("img"));
	    	  String image=(String)me.getAttribute("src");
	    	  String name=(String)me.getAttribute("alt");
		         mp1.put(name,image);
	      }
	      wd.close();
	      return mp1;
	}
	private Map<String, String> searchdata2()
	{
		Map<String, String> mp1=new LinkedHashMap<String, String>();
		int m1=0;
		String name=nm;
	   	for(;m1!=1;)
	   	{
	   		if(name.indexOf(39)==-1)
	   	    {
	   	    	m1=1;
	   	    	break;
	   	    }
	   		int ind=name.indexOf(39);
	   		name=name.substring(0,ind)+name.substring(ind+1);
	    
	   	}
	   	if(name.indexOf(58)!=-1)
	  	    {
			int ind=name.indexOf(58);
	  		name=name.substring(0,ind)+name.substring(ind+1);  	    }
			
		      	 
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
		      jse.executeScript("arguments[0].value='"+name+"';",input);
		      String str=(String) jse.executeScript("return arguments[0].value;",input);
//		      System.out.println(str);
		      jse.executeScript("arguments[0].click();",search);
		      WebDriverWait wait= new WebDriverWait(wd,Duration.ofSeconds(15));
//		      wait.until(ExpectedConditions.urlContains("newPagePartOfUrl"));
		      WebElement item=wd.findElement(By.cssSelector("#contenedor div.module div.content.right.normal div"));
		      List<WebElement> art=item.findElements(By.cssSelector("article.item.movies"));
		      for(WebElement ec : art)
		      {
		      	WebElement videourl=ec.findElement(By.cssSelector("div.data h3 a"));
		      	String nm1=(String)jse.executeScript("return(arguments[0].textContent);",videourl);
		      	WebElement img1=ec.findElement(By.cssSelector(".poster img"));
		      	String imgSrc = (String) jse.executeScript("return arguments[0].getAttribute('src');", img1);
		        mp1.put(nm1,imgSrc);
		      }
		      wd.close();
		      return mp1;
	}
    
    
}
