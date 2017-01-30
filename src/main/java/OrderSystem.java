
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import java.util.List;
import java.util.concurrent.TimeUnit;

import java.util.Date;

import org.quartz.*;
import org.quartz.helpers.TriggerUtils;

public class OrderSystem implements Job {

    //该方法实现需要执行的任务
    public void execute(JobExecutionContext arg0) throws JobExecutionException {

        System.out.println("Generating report - "
                + arg0.getJobDetail().getFullName() + ", type ="
                + arg0.getJobDetail().getJobDataMap().get("type"));
        System.out.println(new Date().toString());

        int[] num = new int[6];
        String[] day = {"星期一","星期二","星期三", "星期四", "星期五", "星期六"};
        String other_instr;
        for (int i = 0; i < 6; i++)
            num[i] = Integer.parseInt(arg0.getJobDetail().getJobDataMap().get("num" + i).toString());
        other_instr = arg0.getJobDetail().getJobDataMap().get("other_instr").toString();


        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        driver.get("https://docs.google.com/forms/d/e/1FAIpQLSd92aQ5fbZXtCuSWwpqtBQgZIsRD-e9dhkUuWRa6QGL3UFeRg/viewform");
        System.out.println("Page title is: " + driver.getTitle());
        String order_instr = "";

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



            try {
                // 创建一个Scheduler
                SchedulerFactory schedFact =
                        new org.quartz.impl.StdSchedulerFactory();
                Scheduler sched = schedFact.getScheduler();
                sched.start();
                // 创建一个JobDetail，指明name，groupname，以及具体的Job类名，
                //该Job负责定义需要执行任务
                JobDetail jobDetail = new JobDetail("myJob", "myJobGroup",
                        OrderSystem.class);
                jobDetail.getJobDataMap().put("type", "FULL");
                for (int i = 0; i < 6; i++)
                    jobDetail.getJobDataMap().put("num" + i, num[i]);
                jobDetail.getJobDataMap().put("other_instr", other_instr);



                // 创建一个每周触发的Trigger，指明星期几几点几分执行
                Trigger trigger = TriggerUtils.makeWeeklyTrigger(7, 12, 00);
                trigger.setGroup("myTriggerGroup");
                // 从当前时间的下一秒开始执行
                trigger.setStartTime(TriggerUtils.getEvenSecondDate(new Date()));
                // 指明trigger的name
                trigger.setName("myTrigger");

                // 用scheduler将JobDetail与Trigger关联在一起，开始调度任务
                sched.scheduleJob(jobDetail, trigger);

                System.out.println("您的订餐信息如下：");
                for(int i = 0; i< 6; i++){
                    System.out.println(day[i] + " " + num[i] + "份");
                }
                System.out.println("其他说明如下：" + other_instr);
                System.out.println("我将于周六中午12:00准时为您订餐，需要修改信息，只需要重新启动就可以~~");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}