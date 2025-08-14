package ott_app;


import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MediaUrl1 
{
	public static String ur=null;
    private	static WebDriver wd=null;
    static String  name;
	public static String getUrl(String name1) throws InterruptedException
	{
		name=name1;
//		System.out.println("abhi" +name);
       int m1=0;
		
   	for(;m1!=1;)
   	{
   		if(name.indexOf(39)==-1)
   	    {
   	    	m1=1;
   	    	break;
   	    }
   		int ind=name.indexOf(39);
   		if(ind!=-1)
   		name=name.substring(0,ind)+name.substring(ind+1);
    
   	}
		 
	      	 
	       ChromeOptions co= new ChromeOptions();
	       co.setCapability("goog:loggingPrefs", Map.of(LogType.PERFORMANCE,java.util.logging.Level.ALL));
//	       co.addArguments("--headless=new");
	       co.addArguments("window-size=1920,1080");
	       co.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
//	       System.out.println("exic");	
	      wd=new ChromeDriver(co);
	     wd.get("https://pcmirror.cc/pv/");
			WebElement inputser= wd.findElement(By.cssSelector("#root div.fixed.left-0.top-0.w-full.bg-transparent div div.ml-auto.flex.items-center div.cursor-pointer.z-10.group.search.cursor-pointer.flex.aspect-square.items-center.justify-center.rounded-full.bg-transparent.transition-all.ease-in-out div"));
	      JavascriptExecutor jse=(JavascriptExecutor) wd;
	     jse.executeScript("arguments[0].click();",inputser);
	     WebElement input= wd.findElement(By.cssSelector("div.relative.flex.w-full.items-center.bg-zinc-700 input"));
	     input.sendKeys(name);
	     Thread.sleep(8000);
//	     System.out.println("exic");	     
	     WebElement resp=wd.findElement(By.cssSelector("div.grid.absolute.overflow-auto"));
	     List<WebElement> resa=resp.findElements(By.cssSelector("div.p-4.flex"));
//	     System.out.println(resa.size());
	     if(resa.size()==0)
	     {
	    	 ur= "not found";
	     }
	     else
	     {
//	    	 System.out.println("exic");	
//	    	 System.out.println("exic result"+resa.size());
	    	   for(WebElement re : resa)
	  	     {
//	    		   System.out.println("exic result"+resa.size());
	  	    	 WebElement im=re.findElement(By.cssSelector("img"));
	  	    	 String st=im.getAttribute("alt").toUpperCase();
	  	    	char gch[]= st.toCharArray();
	  	    	char hch[]=st.toCharArray();
//	  	    	System.out.println((gch[0]==hch[0])?1:0);
	  	    	int val=0;
	  	    	for(int i=0;i<gch.length;i++)
	  	    	{
	  	    	 if(gch[0]==hch[0])
	  	    	 {
	  	    		 val+=1;
	  	    	 }
	  	    	}
	  	    	if(val==gch.length)
	  	    	{
//	  	    		System.out.println("exic result name..."+st+","+name);
	  	    		 ur=im.getAttribute("src");
	  	    		 ur=ur.substring(ur.lastIndexOf('/')+1,ur.length()-4);
		  	    	 ur="https://pcmirror.cc/pv/hls/"+ur+".m3u8?in=unknown::ni";
//		  	    	System.out.println("exic1"+ur);	
	  	    		 break;
	  	    	}
//	  	    	 if(name.equalsIgnoreCase(st))
//	  	    	 {
//	  	    		System.out.println("exic result name..."+st+","+name);
//	  	    		 ur=im.getAttribute("src");
//	  	    		 ur=ur.substring(ur.lastIndexOf('/')+1,ur.length()-4);
//		  	    	 ur="https://pcmirror.cc/pv/hls/"+ur+".m3u8?in=unknown::ni";
//		  	    	System.out.println("exic1"+ur);	
//	  	    		 break;
//	  	    	 }
//	  	    	
	  	     }
//	    	   System.out.println("exic");	
	  	     if(ur==null || ur.isEmpty() || ur.length()<8)
	  	     {
	  	    	
	  	    	 
	  	    	 for(WebElement re : resa)
	  		     {
	  		    	 WebElement im=re.findElement(By.cssSelector("img"));
	  		    	 String st=im.getAttribute("alt");
	  		    	 if(st.toUpperCase().indexOf(name)!=-1)
	  		    	 {
	  		    		 ur=im.getAttribute("src");
	  		    		 ur=ur.substring(ur.lastIndexOf('/')+1,ur.length()-4);
	  		  	    	 ur="https://pcmirror.cc/pv/hls/"+ur+".m3u8?in=unknown::ni";
	  		  	    	 System.out.println("exic2"+ur);	
	  		    		 break;
	  		    	 }
	  		     }
	  	     }
//	  	   System.out.println("exic");	
	  	     if(ur==null || ur.isEmpty() || ur.length()<8)
	  	     {
	  	    	 ur= "not found";
	  	    	 wd.close();
	  	     }
	     }
//	     System.out.println("exic end");	
		 
			 if(wd!=null)
			 {
				 wd.close();
			 }
//			 System.out.println(ur);
//			 System.out.println("exic end-l");	
		 return(ur);
	}

	public static void main(String[] args)
	{
	    try {
			MediaUrl1.getUrl("THAPPAD");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
