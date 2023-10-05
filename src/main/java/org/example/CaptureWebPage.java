package org.example;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CaptureWebPage {
    public static void main(String[] args) throws IOException {
        // ChromeDriver 경로 설정
        System.setProperty("webdriver.chrome.driver", "C:/Users/jkj02/IdeaProjects/selenium/chromedriver-win64/chromedriver.exe");

        // 헤드리스 모드로 브라우저 실행
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");

        WebDriver driver = new ChromeDriver(options);

        try {
            // 새 탭 생성
            ((JavascriptExecutor) driver).executeScript("window.open()");
            ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
            driver.switchTo().window(tabs.get(1));  // 새 탭으로 전환

            // 선택 사항 - HTML or URL
            // 데이터 문자열로 HTML 페이지를 생성
            // String dataURL = "data:text/html;charset=utf-8," + "<h1>HI!</h1>";
            // url로 페이지 로드
            String dataURL = "https://www.naver.com";

            // 생성된 페이지로 이동
            driver.get(dataURL);

            // 페이지 스크린샷 캡쳐
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            // 지정된 영역을 스크린샷으로 캡쳐
            BufferedImage fullImg = ImageIO.read(screenshot);
            // 해당 좌표, 범위를 지정하여 원하는 부분을 캡쳐
            BufferedImage eleScreenshot= fullImg.getSubimage(0, 0, 100, 100);
            ImageIO.write(eleScreenshot, "png", screenshot);

            screenshot.renameTo(new File(getNextFileName()));
        } finally {
            driver.quit();
        }
    }

    // 캡쳐 파일 이름 생성 -> capPro_01 . . .
    private static String getNextFileName() {
        int maxIndex = 0;
        File dir = new File(".");
        for (File file : dir.listFiles()) {
            String name = file.getName();
            if (name.startsWith("capPro_") && name.endsWith(".png")) {
                try {
                    int index = Integer.parseInt(name.substring(7, name.length() - 4));  // "capPro_01.png"에서 "01"을 추출
                    if (index > maxIndex) {
                        maxIndex = index;
                    }
                } catch (NumberFormatException e) {
                    // 파일 이름에 숫자가 아닌 다른 문자가 포함되어 있을 경우 무시
                }
            }
        }
        return String.format("capPro_%02d.png", maxIndex + 1);
    }
}
