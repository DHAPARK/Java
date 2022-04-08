
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class CrawlingTest {
	public static String id = "webdriver.chrome.driver";
	public static String path = "chromedriver";
	public static void main(String[] args) {
		System.setProperty(id, path);
		ChromeOptions options = new ChromeOptions();
		String url = "https://www.naver.com";
		options.setCapability("ignoreProtectedModeSettings", true);
		WebDriver driver = new ChromeDriver(options);
	// ------------------------------------------------------세팅끝
		driver.get(url);
		//url을 요청을 하는것 즉 네이버로 접속을 하는 것
		try {Thread.sleep(2000);} catch (InterruptedException e) {}
		//naver 페이지의 검색창 객체 찾기		By.id("아이디") : 아이디로 요소 검색
		WebElement searchInput = driver.findElement(By.id("query")); // 요소 선택
		searchInput.click(); //찾았으면 그걸 클릭
		
		searchInput.sendKeys("마스크");//검색창에 글이 써질것
		WebElement searchBtn = driver.findElement(By.id("search_btn"));
		searchBtn.click();
		
		//searchInput.getText(); 이게 요소위의 글자를 검색해옴
		
		try {Thread.sleep(2000);} catch (InterruptedException e) {}
		
		
		WebElement productList =  driver.findElement(By.className("shop_guide_group"));
		List<WebElement> listElements = productList.findElement(By.tagName("ul")).findElements(By.className("box"));
		
		for (WebElement element : listElements) {
			//리스트에 담겨있는 요소들을 꺼내오며 "title" 클래스를 갖고있는 요소의 텍스트 받아오기
			String prodname = element.findElement(By.className("title")).getText();
			
			String price = element.findElement(By.className("price")).findElement(By.tagName("strong")).getText();
			System.out.println(prodname+ " : " + price);
		}
	}
}