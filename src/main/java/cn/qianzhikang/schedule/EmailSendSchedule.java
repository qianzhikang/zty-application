package cn.qianzhikang.schedule;
import cn.hutool.extra.mail.MailUtil;


/**
 * 邮件发送定时任务
 * @author qianzhikang
 */
public class EmailSendSchedule {
    public void sendEmailSchedule() {
        System.out.println("邮件任务执行发送");
//        MailUtil.send("lmxxmlsq@163.com", "测试", "邮件来自Hutool测试", false);
    }
}
