package com.andy.maven.boot;


import cn.hutool.system.SystemUtil;
import com.andy.maven.boot.service.MailService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Properties;

/**
 * @projectName: springstudy  com.andy.maven.boot
 * @desc:
 * @author: xiangdan
 * @time : 2019-02-15 Friday 14:41
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {BootMailApplication.class})
public class BootMailApplicationTest {

	private static final String to="604484715@qq.com";
	@Autowired
	MailService mailService;

	private String systemInfo;

	@Before
	public void init(){
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		SystemUtil.dumpSystemInfo(pw);

		systemInfo = sw.toString();
		try {
			sw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		pw.close();
	}

	// 测试简单邮件发送
	@Test
	public void test1(){
		String subject="测试简单邮件发送"+LocalDateTime.now().toString();;

		String content="测试简单邮件发送内容 \n";

		content +=systemInfo;

		mailService.sendSimpleMail(to,subject,content);
	}

	@Test//测试HTML邮件发送
	public void test2(){
		String subject="测试HTML邮件发送"+LocalDateTime.now().toString();;

		StringBuffer sb = new StringBuffer();
		sb.append("<html>");
		sb.append("<head>");
		sb.append("</head>");
		sb.append("<body>");
		sb.append("<h1>这是HTML邮件发送</h1>");
		sb.append("<pre>"+systemInfo+"</pre>");
		sb.append("</body>");
		sb.append("</html>");


		mailService.sendHtmlMail(to,subject,sb.toString());
	}


	@Test//测试附件邮件发送
	public void test3(){
		String subject="测试附件邮件发送"+LocalDateTime.now().toString();;

		StringBuffer sb = new StringBuffer();
		sb.append("<html>");
		sb.append("<head>");
		sb.append("</head>");
		sb.append("<body>");
		sb.append("<h1>这是附件邮件发送</h1>");
		sb.append("<pre>"+systemInfo+"</pre>");
		sb.append("</body>");
		sb.append("</html>");

		Resource[] resources = new Resource[]{
				new ClassPathResource("application.yml"),
		};


		mailService.sendAttachMail(to,subject,sb.toString(),resources);
	}


	@Test//测试图片邮件发送
	public void test4(){
		String subject="测试图片邮件发送"+LocalDateTime.now().toString();;

		String[] rcids = {"rcid1","rcid2"};

		StringBuffer sb = new StringBuffer();
		sb.append("<html>");
		sb.append("<head>");
		sb.append("</head>");
		sb.append("<body>");
		sb.append("<h1>这是图片邮件发送</h1>");
		sb.append("图片1：<img src='cid:"+rcids[0]+"' ></img>");
		sb.append("图片2：<img src='cid:"+rcids[1]+"' ></img>");
		sb.append("</body>");
		sb.append("</html>");

		Resource[] resources = new Resource[]{
				new ClassPathResource("img/abc1.jpg"),
				new ClassPathResource("img/abc2.png"),
		};


		mailService.sendImagesMail(to,subject,sb.toString(),rcids,resources);
	}

	//邮件模板发送邮件
	@Test
	public void test5(){
		String subject="测试thymeleaf邮件模板发送"+LocalDateTime.now().toString();;

		Context context = new Context();
		context.setVariable("id",1);
		context.setVariable("date",new Date());
		context.setVariable("curDate",LocalDateTime.now().toString());
		context.setVariable("systemInfo",systemInfo);

		mailService.sendTemplateMail(to,subject,context);
	}



}
