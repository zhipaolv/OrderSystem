/**
 * Created by zhipao on 1/24/2017.
 */

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class OrderSystem {
    public static void main(String[] args) {

        if (args.length == 0){
            System.out.println("请输入订餐日期，格式如下");
            System.out.println("1--代表星期一");
            System.out.println("2--代表星期二");
            System.out.println("......");
            System.out.println("如此类推");
            System.out.println("最后写其他说明，已。号结尾");
        }else {
            String other_instr = "暂无";
            String order_instr = "";
            int[] num = {0,0,0,0,0,0};
            String[] day = {"星期一","星期二","星期三", "星期四", "星期五", "星期六"};

            //用num数组记录每天的订餐数
            for (int i = 0; i<args.length-1; i++){
                num[Integer.parseInt(args[i]) - 1]++;
            }

            //判断是否有其他说明

            if(args[args.length-1].endsWith("。")){
                other_instr = args[args.length-1];
            }else{
                num[Integer.parseInt(args[args.length-1]) - 1]++;
            }

            System.out.println("您的订餐信息如下：");
            for(int i = 0; i< 6; i++){
                System.out.println(day[i] + " " + num[i] + "份");
            }
            System.out.println("其他说明如下：" + other_instr);
            System.out.println("我将于周六中午12:00准时为您订餐，需要修改信息，只需要重新启动就可以~~");

            WebDriver driver = new ChromeDriver();
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            driver.get("https://docs.google.com/forms/d/e/1FAIpQLSd92aQ5fbZXtCuSWwpqtBQgZIsRD-e9dhkUuWRa6QGL3UFeRg/viewform");
            System.out.println("Page title is: " + driver.getTitle());

            //输入名字
            WebElement name = driver.findElement(By.name("entry.1504294810"));
            name.sendKeys("Chengmi Zhou");

            //勾选订餐时间
            List<WebElement> elements = driver.findElements(By.className("exportLabelWrapper"));
            for(int i = 0; i < 6; i++){
                if(num[i] != 0){
                    elements.get(i).click();
                    if (num[i] > 1){
                        order_instr += day[i] + "订" + num[i] + "份，";
                    }
                }
            }

            //输入订单详情
            WebElement order_detail = driver.findElement(By.name("entry.505082569"));
            order_detail.sendKeys(order_instr);

            //输入联系邮箱
            WebElement email = driver.findElement(By.name("entry.1040155748"));
            email.sendKeys("chengmizhou3701@gmail.com ");

            //其他说明
            WebElement instr = driver.findElement(By.name("entry.1439080666"));
            instr.sendKeys(other_instr);

            //点击提交
            WebElement submit = driver.findElement(By.className("quantumWizButtonPaperbuttonRipple"));
            submit.submit();
            driver.quit();
            System.out.println("订餐成功~");

        }
    }
}
