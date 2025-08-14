package ott_app;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ShowMediaUrl1 {

	private static WebDriver wd=null;
	private static String ur=null;
	private static File f=null;
	public static Map<String,File> getUrl(String name,int seson,int ep,String lenguage)
	{
       int m1=0;
		
		String name1=name;
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
		 try 
	       {
	      	 
			 ChromeOptions co= new ChromeOptions();
		       co.setCapability("goog:loggingPrefs", Map.of(LogType.PERFORMANCE,java.util.logging.Level.ALL));
		       co.addArguments("--headless=new");
		       co.addArguments("window-size=1920,1080");
		       co.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
		      
		      wd=new ChromeDriver(co);
		     wd.get("https://pcmirror.cc/pv/");
				WebElement inputser= wd.findElement(By.cssSelector("#root div.fixed.left-0.top-0.w-full.bg-transparent div div.ml-auto.flex.items-center div.cursor-pointer.z-10.group.search.cursor-pointer.flex.aspect-square.items-center.justify-center.rounded-full.bg-transparent.transition-all.ease-in-out div"));
		      JavascriptExecutor jse=(JavascriptExecutor) wd;
		     jse.executeScript("arguments[0].click();",inputser);
		     WebElement input= wd.findElement(By.cssSelector("div.relative.flex.w-full.items-center.bg-zinc-700 input"));
		     input.sendKeys(name);
		     Thread.sleep(8000);
		     int chn=0;
		     WebElement resp=wd.findElement(By.cssSelector("div.grid.absolute.overflow-auto"));
		     List<WebElement> resa=resp.findElements(By.cssSelector("div.p-4.flex"));
//		     System.out.println(resa.size());
		     if(resa.size()==0)
		     {
		    	 ur= "not found";
		     }
		     else
		     {
		    	   for(WebElement re : resa)
		  	     {
		  	    	 WebElement im=re.findElement(By.cssSelector("img"));
		  	    	 String st=im.getAttribute("alt");
		  	    	 if(name.toUpperCase().equalsIgnoreCase(st))
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
		  		    	 if(st.toUpperCase().contains(name.toUpperCase()))
		  		    	 {
		  		    		 chn=1;
		  		    		 jse.executeScript("arguments[0].click();", re);
		  		    		 break;
		  		    	 }
		  		     }
		  	     }
		  	     if(chn==0)
		  	     {
		  	    	 ur= "not found";
		  	    	 wd.close();
		  	     }
		  	     else
		  	     {
		  	    	Thread.sleep(4000);
		  	    	WebElement wef=wd.findElement(By.cssSelector("div.flex.justify-end.relative.mx-auto p"));
		  	    	jse.executeScript("arguments[0].click();",wef);
		  	    	Thread.sleep(2000);
//		  	    	List<WebElement> ln= wd.findElements(By.cssSelector("#root main div.absolute.top-0.left-0.w-full.h-full.z-50 div div div div .swiper-slide"));
////		  	    	System.out.println(ln.size());
//		  	    	
		  	    	WebElement ss=wd.findElement(By.cssSelector("div.right-0.backdrop-blur-md p:nth-child("+seson+")"));
 		    		 jse.executeScript("arguments[0].click();", ss);
 		    		 Thread.sleep(2000);
 		    		jse.executeScript("setInterval(() => {\r\n"
 		    				+ "    window.scrollBy(0, 1000);\r\n"
 		    				+ "}, 100);\r\n");
 		    		new WebDriverWait(wd, Duration.ofSeconds(4000)).until((x)->{
 		    			if((x.findElement(By.cssSelector("div.pe-16.flex.flex-col.gap-8.mx-auto div:nth-child("+ep+") div div div div img"))).isDisplayed())
 		    			{
 		    				return true;
 		    			}
 		    			return false;
 		    		});
 		    		WebElement epd=wd.findElement(By.cssSelector("div.pe-16.flex.flex-col.gap-8.mx-auto div:nth-child("+ep+") div div div div span img"));
 		    	    ur=epd.getAttribute("src");
 		    	    jse.executeScript("arguments[0].click();",epd);
 		    	   new WebDriverWait(wd, Duration.ofSeconds(10000)).until((x)->{
		    			if((x.findElement(By.cssSelector("#player div.jw-reset.jw-icon-settings.jw-settings-submenu-button"))).isDisplayed())
		    			{
		    				return true;
		    			}
		    			return false;
		    		});	
 		    	   WebElement setb=wd.findElement(By.cssSelector("#player div.jw-reset.jw-icon-settings.jw-settings-submenu-button"));
 		    	   jse.executeScript("arguments[0].click();",setb);
 		    	   WebElement lens=wd.findElement(By.cssSelector("#jw-player-settings-menu div.jw-settings-topbar-buttons div.jw-settings-audioTracks.jw-submenu-audioTracks"));
 		    	  jse.executeScript("arguments[0].click();",lens);
 		    	  List<WebElement> lb=wd.findElements(By.cssSelector("#jw-player-settings-submenu-audioTracks div button"));
 		    	 int foul=0;
		  	    	for(WebElement ll : lb)
		  	    	{
		  	    		String lnm=ll.getText();
		  	    		if(lnm.equalsIgnoreCase(lenguage))
		  	    		{
		  	    			 Actions actions = new Actions(wd);
                             actions.moveToElement(ll).click().perform();
		  	    			foul=1;
		  	    			break;
		  	    		}
		  	    	}
		  	    	if(foul==0)
		  	    	{
		  	    		for(WebElement ll : lb)
			  	    	{
			  	    		String lnm=ll.getText();
			  	    		if(lnm.equalsIgnoreCase("english"))
			  	    		{

                               Actions actions = new Actions(wd);
                               actions.moveToElement(ll).click().perform();
			  	    			foul=1;
			  	    			break;
			  	    		}
			  	    	}
		  	    	}
				 Thread.sleep(5000);
 		    	    ur=ur.substring(ur.lastIndexOf('/')+1,ur.length()-4);
 		  	    	ur="https://pcmirror.cc/pv/hls/"+ur+".m3u8?in=unknown::ni";
 		  	    	f=File.createTempFile("show",".m3u8");
 		  	    	
 		  	    	URL shour=new URL(ur);
 		  	    	InputStream is= shour.openStream();
 		  	    	Files.copy(is,f.toPath(),StandardCopyOption.REPLACE_EXISTING);
 		  	    	
		  	     }
		     }
	       }
		 catch(Exception e)
		 {
			 ur="network issue";
			  wd.close();
			 e.printStackTrace();
		 }
		 Map<String,File>oum=new HashMap<String, File>();
		 oum.put(ur, f);
//		 System.out.println("abhi  "+oum);
    	 wd.close();
			return (oum);
	}
	public static void main(String[] args)
	{
	     ShowMediaUrl1.getUrl("the wheel",1, 5,"hindi");

	}

}
