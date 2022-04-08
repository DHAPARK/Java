
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class CrawlingTest2_brol {
	public static String id = "webdriver.chrome.driver";
	public static String path = "D:\\eclipse_lastest_v\\resource\\chromedriver.exe";
	public static void main(String[] args) {
		System.setProperty(id, path);
		ChromeOptions options = new ChromeOptions();
		String url = "https://brawlstats.com/";
		options.setCapability("ignoreProtectedModeSettings", true);
		WebDriver driver = new ChromeDriver(options);
	// ------------------------------------------------------세팅끝
		driver.get(url);
		//url을 요청을 하는것 즉 네이버로 접속을 하는 것
		try {Thread.sleep(2000);} catch (InterruptedException e) {}
		//naver 페이지의 검색창 객체 찾기		By.id("아이디") : 아이디로 요소 검색
		WebElement searchInputBefore = driver.findElement(By.className("_1dbKh-B4ezR5_8cpPEWTKg")); // 요소 선택
		WebElement searchInput = searchInputBefore.findElement(By.tagName("input"));
		searchInput.click(); //찾았으면 그걸 클릭
		
		searchInput.sendKeys("91231428");//검색창에 글이 써질것
		WebElement searchBtnBefore = driver.findElement(By.className("_2NpUhJSWFcqj2aSOXTk_1K"));
		WebElement searchBtn = searchBtnBefore.findElement(By.className("_2CjoDiA0bn4mAbC62jqJGt"));
		searchBtn.click();
		
		//searchInput.getText(); 이게 요소위의 글자를 검색해옴
		
		try {Thread.sleep(2000);} catch (InterruptedException e) {}
		
		/*
		WebElement productList =  driver.findElement(By.className("shop_guide_group"));
		List<WebElement> listElements = productList.findElement(By.tagName("ul")).findElements(By.className("box"));
		
		for (WebElement element : listElements) {
			//리스트에 담겨있는 요소들을 꺼내오며 "title" 클래스를 갖고있는 요소의 텍스트 받아오기
			String prodname = element.findElement(By.className("title")).getText();
			
			String price = element.findElement(By.className("price")).findElement(By.tagName("strong")).getText();
			System.out.println(prodname+ " : " + price);
		}
		*/
	}
}