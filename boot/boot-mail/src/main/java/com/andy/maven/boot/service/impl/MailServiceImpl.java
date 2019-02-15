package com.andy.maven.boot.service.impl;

import com.andy.maven.boot.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @projectName: springstudy  com.andy.maven.boot.service
 * @desc:
 * @author: xiangdan
 * @time : 2019-02-15 Friday 14:10
 */
@Service
public class MailServiceImpl implements MailService {
	private static Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);
	@Autowired
	private JavaMailSender mailSender;

	@Value("${spring.mail.username}")
	private String from;

	@Autowired
	private TemplateEngine templateEngine;

	//发送简单邮件
	@Override
	public void sendSimpleMail(String to, String subject, String content) {
		SimpleMailMessage smm = getSimpleMailMessage(subject, content,to);
		mailSender.send(smm);
		logger.info("发送简单邮件成功");
	}

	@Override
	public void sendSimpleMail(String[] to, String subject, String content) {
		SimpleMailMessage smm = getSimpleMailMessage(subject, content,to);
		mailSender.send(smm);
		logger.info("发送简单邮件成功");
	}
	private SimpleMailMessage getSimpleMailMessage(String subject, String content,String ... to) {
		SimpleMailMessage smm = new SimpleMailMessage();
		smm.setFrom(from);
		smm.setTo(to);
		smm.setSubject(subject);
		smm.setText(content);
		return smm;
	}

	//发送HTML邮件
	@Override
	public void sendHtmlMail(String to, String subject, String content) {
		MimeMessage message = getMimeMessage(to, subject, content);
		mailSender.send(message);
		logger.info("发送HTML邮件成功");

	}

	private MimeMessage getMimeMessage(String to, String subject, String content) {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(message);
		try {
			messageHelper.setFrom(from);
			messageHelper.setTo(to);
			messageHelper.setSubject(subject);
			messageHelper.setText(content,true);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return message;
	}

	@Override
	public void sendAttachMail(String to, String subject, String content, Resource[] resources) {


		MimeMessage message = mailSender.createMimeMessage();
		try {
			MimeMessageHelper messageHelper = new MimeMessageHelper(message,true);
			messageHelper.setFrom(from);
			messageHelper.setTo(to);
			messageHelper.setSubject(subject);
			messageHelper.setText(content,true);
			for(Resource res:resources){
				messageHelper.addAttachment(res.getFilename(),res);
			}
		} catch (MessagingException e) {
			e.printStackTrace();
		}


		mailSender.send(message);
		logger.info("发送附件邮件成功");
	}

	@Override
	public void sendImagesMail(String to, String subject, String content, String[] rcids, Resource[] resources) {
		MimeMessage message = mailSender.createMimeMessage();
		try {
			MimeMessageHelper messageHelper = new MimeMessageHelper(message,true);
			messageHelper.setTo(to);
			messageHelper.setFrom(from);
			messageHelper.setSubject(subject);
			messageHelper.setText(content,true);
			for (int i = 0; i < rcids.length; i++) {
				messageHelper.addInline(rcids[i],resources[i]);
			}
		} catch (MessagingException e) {
			e.printStackTrace();
		}

		mailSender.send(message);
		logger.info("发送图片邮件成功");
	}

	@Override
	public void sendTemplateMail(String to, String subject, Context context) {
		MimeMessage message = mailSender.createMimeMessage();
		try {
			MimeMessageHelper messageHelper = new MimeMessageHelper(message,true);
			messageHelper.setFrom(from);
			messageHelper.setTo(to);
			messageHelper.setSubject(subject);
			String content = templateEngine.process("email",context);


			messageHelper.setText(content,true);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		mailSender.send(message);
		logger.info("发送模板邮件成功");
	}


}
