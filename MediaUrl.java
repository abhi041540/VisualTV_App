package ott_app;

import java.net.URL;
import java.time.Duration;
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

public class MediaUrl
{
	public static String getUrl(String name)
	{
       String videour="";
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
	      
	       WebDriver wd=new ChromeDriver(co);
	     wd.get("https://hdmovie2.mn");
			URL url= new  URL("https://hdmovie2.mn/");
			WebElement input= wd.findElement(By.cssSelector("#searchformpage #story"));
//	      System.out.println(input.getAttribute("placeholder"));
	      WebElement search= wd.findElement(By.cssSelector(".search_page_form #searchformpage button span"));
//	      System.out.println(input.getAttribute("type"));
	      JavascriptExecutor jse=(JavascriptExecutor) wd;
	      jse.executeScript("arguments[0].value='"+name+"';",input);
	      String str=(String) jse.executeScript("return arguments[0].value;",input);
//	      System.out.println(str);
	      jse.executeScript("arguments[0].click();",search);
	      WebDriverWait wait= new WebDriverWait(wd,Duration.ofSeconds(15));
//	      wait.until(ExpectedConditions.urlContains("newPagePartOfUrl"));
	      WebElement item=wd.findElement(By.cssSelector("#contenedor div.module div.content.right.normal div"));
	      List<WebElement> art=item.findElements(By.cssSelector("article.item.movies"));
	      WebElement videourl;
//	      DevTools dtv=((ChromeDriver)wd).getDevTools();
//	         dtv.createSession();
//	         dtv.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
//	         dtv.addListener(Network.requestWillBeSent(),(request)->{
//	      	   Request req=request.getRequest();
////	      	   System.out.println(req.getUrl().toString());
//	      	   if(req.getUrl().contains(".mp4") || req.getUrl().contains(".m3u8"))
//	      	   {     
//	      		  System.out.println(req.getUrl().toString());
//	   	   }
//	         });
	      int cm=0;
	      for(WebElement ec : art)
	      {
	      	videourl=ec.findElement(By.cssSelector("div.data h3 a"));
	      	String nm=(String)jse.executeScript("return(arguments[0].textContent);",videourl);
	      	nm=nm.toUpperCase();
	      	if(nm.contains(name1))
	      	{
	      		jse.executeScript("arguments[0].click();",videourl);
		      	new WebDriverWait(wd,Duration.ofSeconds(10));
		      	cm=1;
		      	break;	
	      	}

	      	
	      }
	      if(cm==1)
	      {
	    
	      WebElement iframe= wd.findElement(By.tagName("iframe"));
	      wd.switchTo().frame(iframe);
	      WebElement vid=wd.findElement(By.cssSelector("hdvbplayer:nth-child(3) video"));
	      String script = "(function() {" +
	    		    "window.m3u8Urls = [];" +
	    		    "function logM3U8Url(url) {" +
	    		    "if (url.endsWith('.m3u8')) {" +
	    		    "var absoluteUrl = new URL(url, window.location.href).href;" +
	    		    "console.log('Video URL:', absoluteUrl);" +
	    		    "window.m3u8Urls.push(absoluteUrl);" +
	    		    "}" +
	    		    "}" +
	    		    "const observer = new PerformanceObserver((list) => {" +
	    		    "list.getEntries().forEach((entry) => {" +
	    		    "if ((entry.initiatorType === 'xmlhttprequest' || entry.initiatorType === 'fetch') && entry.name.endsWith('.m3u8')) {" +
	    		    "fetch(entry.name).then(response => {" +
	    		    "if (response.status === 200) {" +
	    		    "logM3U8Url(response.url);" +
	    		    "}" +
	    		    "});" +
	    		    "}" +
	    		    "});" +
	    		    "});" +
	    		    "observer.observe({ entryTypes: ['resource'] });" +
	    		    "const originalFetch = window.fetch;" +
	    		    "window.fetch = function() {" +
	    		    "return originalFetch.apply(this, arguments).then(response => {" +
	    		    "if (response.url.endsWith('.m3u8') && response.status === 200) {" +
	    		    "logM3U8Url(response.url);" +
	    		    "}" +
	    		    "return response;" +
	    		    "});" +
	    		    "};" +
	    		    "const originalOpen = XMLHttpRequest.prototype.open;" +
	    		    "XMLHttpRequest.prototype.open = function() {" +
	    		    "this.addEventListener('load', function() {" +
	    		    "if (this.responseURL.endsWith('.m3u8') && this.status === 200) {" +
	    		    "logM3U8Url(this.responseURL);" +
	    		    "}" +
	    		    "});" +
	    		    "originalOpen.apply(this, arguments);" +
	    		    "};" +
	    		    "})();";

	    		jse.executeScript(script);
	    		jse.executeScript("arguments[0].click();", vid);
	    		Thread.sleep(20000); // Wait for 20 seconds to ensure all URLs are captured
	    		List<String> m3u8 = (List<String>) jse.executeScript("return window.m3u8Urls;");
//	    		System.out.println(m3u8.size());
//	    		for (String ur : m3u8) {
//	    		    System.out.println(ur);
//	    		}

	    		videour = m3u8.get(m3u8.size() - 1);
	    		
	    		wd.close();
	    		if(videour.indexOf("/index.m3u8")!=-1)
	    		{
	    			videour=videour.substring(0,videour.indexOf("/index.m3u8"))+"/720/index.m3u8";
	    		}
	    		
	    		} 
	      else
	      {
	    	  videour="not found";
	      }
	      }
	     catch (Exception e) 
	     {
	    	 videour="network issue";
			e.printStackTrace();
		}
	  
		 return(videour);

	}
	public static void main(String args[])
	{
		System.out.println(getUrl("HI NANNA"));
	}
}
