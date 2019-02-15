package com.andy.maven.boot.service;

import org.springframework.core.io.Resource;
import org.thymeleaf.context.Context;

/**
 * @projectName: springstudy  com.andy.maven.boot.service
 * @desc:
 * @author: xiangdan
 * @time : 2019-02-15 Friday 14:10
 */
public interface MailService {

	/**
	 * 发送简单邮件
	 * @param to 发送人
	 * @param subject 发送主题
	 * @param content 发送内容
	 */
	void sendSimpleMail(String to,String subject,String content);

	/**
	 * 发送简单邮件
	 * @param to 发送人
	 * @param subject 发送主题
	 * @param content 发送内容
	 */
	void sendSimpleMail(String[] to,String subject,String content);


	/**
	 * 发送HTML邮件
	 * @param to
	 * @param subject
	 * @param content
	 */
	void sendHtmlMail(String to,String subject,String content);

	/**
	 * 发送附件
	 * @param to
	 * @param subject
	 * @param content
	 * @param resources
	 */
	void sendAttachMail(String to, String subject, String content, Resource[] resources);



	/**
	 * 发送图片
	 * @param to
	 * @param subject
	 * @param content
	 * @param resources
	 */
	void sendImagesMail(String to, String subject, String content,String[] rcids, Resource[] resources);



	/**
	 * 发送模板
	 * @param to
	 * @param subject
	 * @param context
	 */
	void sendTemplateMail(String to, String subject, Context context);


}
