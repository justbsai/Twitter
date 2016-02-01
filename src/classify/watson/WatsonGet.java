package classify.watson;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WatsonGet extends Thread{
	WebDriver driver = new FirefoxDriver();
	HashMap <String,Integer> insights=new LinkedHashMap<String,Integer>();
	String text;
	public HashMap<String, Integer> getParam(){
		return insights;	
	}
	public void run(){
		try{
			driver.get("https://watson-pi-demo.mybluemix.net/");
			driver.findElement(By.xpath("/html/body/div[2]/div/div/ul/li[2]/a")).click();
			WebElement element = driver.findElement(By.id("inputText"));
			((JavascriptExecutor)driver).executeScript("arguments[0].value = arguments[1];", element,this.text);
			driver.findElement(By.xpath("id('text-panel')/form/div[2]/button")).click();
			WebDriverWait wait = new WebDriverWait(driver, 10);
			WebElement jsonbut = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/div/div/div[3]/span/button")));
			jsonbut.click();
			String json=driver.findElement(By.xpath("/html/body/div[3]/div/div/div[3]/div[3]/pre/code")).getText();
			int a=1;
			JSONArray arr =  new JSONObject(json).getJSONObject("tree").getJSONArray("children");
			for (int i = 0; i < arr.length(); i++)
			{
				JSONArray arr1=arr.getJSONObject(i).getJSONArray("children");
				for(int j=0;j<arr1.length();j++){
					JSONArray arr2=arr1.getJSONObject(j).getJSONArray("children");
						for(int k=0;k<arr2.length();k++){
								if(a<=5){
									a++;
									JSONArray arr3=arr2.getJSONObject(k).getJSONArray("children");	
									insights.put(arr2.getJSONObject(k).getString("id"), (int)Math.round(arr2.getJSONObject(k).getDouble("percentage")*100));
									for(int l=0;l<arr3.length();l++){
										insights.put(arr3.getJSONObject(l).getString("id"),(int)Math.round(arr3.getJSONObject(l).getDouble("percentage")*100));
									}
								}else{
									insights.put(arr2.getJSONObject(k).getString("id"),(int)Math.round(arr2.getJSONObject(k).getDouble("percentage")*100));	
								}				
							}	
				
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			driver.close();
            driver.quit();
		}
	}
	public WatsonGet(String text){
	    this.text=text;
	}

}
