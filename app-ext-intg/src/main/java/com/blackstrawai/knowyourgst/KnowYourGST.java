package com.blackstrawai.knowyourgst;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.blackstrawai.ApplicationException;

public class KnowYourGST {
	
	private static KnowYourGST knowYourGST;
	
	private Logger logger = Logger.getLogger(KnowYourGST.class);

	
	private KnowYourGST(){
		
	}
	
	public static KnowYourGST getInstance(){
		if(knowYourGST==null){
			knowYourGST=new KnowYourGST();
		}
		return knowYourGST;
	}
	
	/*public static void main(String args[])throws ApplicationException{
		KnowYourGST.getInstance().getGST("AADCS0472N");
	}
	*/
	public List<String> getGST(String pan) throws ApplicationException {
		List<String>data=new ArrayList<String>();
		 ChromeDriver driver =null;
		 logger.info("In DAO");
		try
		{
		     String osName= System.getProperty("os.name"); 
		    
		     if(osName!=null && osName.startsWith("Windows")){
					System.setProperty("webdriver.chrome.driver", "/chromedriver/chromedriver.exe");					
					driver = new ChromeDriver();
					 logger.info("In DAO-Windows");
					
		     }else{
					System.setProperty("webdriver.chrome.driver", "/chromedriver/chromedriver");
					ChromeOptions chromeOptions = new ChromeOptions();
		            chromeOptions.addArguments("--no-sandbox");
		            chromeOptions.addArguments("--headless");
		            chromeOptions.addArguments("disable-gpu");
		            chromeOptions.addArguments("window-size=1400,2100"); // Linux should be activate		            
					driver = new ChromeDriver(chromeOptions);
					 logger.info("In DAO-Non Windows");
		     }      
	

		//	driver.manage().timeouts().implicitlyWait(3000, TimeUnit.SECONDS);		
			driver.get("https://www.knowyourgst.com/gst-number-search/by-name-pan/");
			driver.findElement(By.id("gstnumber")).sendKeys(pan);
			Thread.sleep(1000);
			driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[1]/div[1]/form/div[2]/input")).click();
			List<WebElement> links = driver.findElements(By.xpath("//*[@id=\"searchresult\"]"));
			System.out.println(links.size());
			for (WebElement link:links)
			{
				WebElement dataWebElement = link.findElement(By.tagName("span"));
				String data_lc=dataWebElement.getText();
				if(data_lc!=null){
					data_lc=data_lc.substring(data_lc.lastIndexOf(",")+1,data_lc.length());
					System.out.println(data_lc.trim());
					data.add(data_lc.trim());					
				}				
			}
			 logger.info("Complete");
			System.out.println("complete");
		}catch(Exception e){
			logger.info("Exception:"+e.getMessage());
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			String sStackTrace = sw.toString(); // stack trace as a string
			System.out.println(sStackTrace);
			e.printStackTrace();
			logger.info("Full Exception Message:"+sStackTrace);
			return data;
		}finally{			
			if(driver!=null){
				//driver.close();
				driver.quit();
			}
		}
		return data;
		
	}
}



        
        
	

