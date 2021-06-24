package biz.mercue.impactweb.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import biz.mercue.impactweb.model.Admin;






@Component
public class MailSender {
	
	private Logger log = Logger.getLogger(this.getClass().getName());
	
	final String username = Constants.MAIL_ACCOUNT;
	final String password =  Constants.MAIL_PASSWORD;
	
	private List<String> receivers = null;
	private String subject = null;
	private String content = null;
	private Properties props = null;
	private Session session = null;
	
	private String htmlContent = null;
	
	
	private Date sendDate =null;
	
	int condition =  -1;
	
public static void main(String[] args){
		

		
	}
	public MailSender(){
		props = new Properties();
		props.put("mail.smtp.host",  "smtp.gmail.com");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.port", "587");
		
		
		
		session = Session.getInstance(props,
				  new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				  });
	}
	
	
	public void setReceiver(List<String> receiverList){
		this.receivers = receiverList;
	}
	
	public void setSubject(String strSubject){
		this.subject = strSubject;
	}
	
	public void setContent(String strContent){
		this.content = strContent;
	}
	
	public void setHtmlContent(String htmlContent){
		this.htmlContent = htmlContent;
	}
	
	public void setSendDate(Date sendDate){
		this.sendDate = sendDate;
	}
	
	public void setMailContent(String strSubject,String strContent){
		this.subject = strSubject;
		this.content = strContent;
	}
	
	public void sendSimpleMail(List<String> receiverList,String strSubject,String strContent){
		sendSimpleMail(receiverList,strSubject,strContent,null);
	}
	
	public void sendSimpleMail(List<String> receiverList,String strSubject,String strContent,Date sendDate){
		this.receivers = receiverList;
		this.subject = strSubject;
		this.content = strContent;
		this.sendDate = sendDate;
		sendMail();
	}
	
	public void sendHTMLMail(List<String> receiverList,String strSubject,String htmlContent){
		sendHTMLMail(receiverList,strSubject,htmlContent,null);
	}
	
	public void sendHTMLMail(List<String> receiverList,String strSubject,String htmlContent,Date sendDate){
		this.receivers = receiverList;
		this.subject = strSubject;
		this.htmlContent = htmlContent;
		this.sendDate = sendDate;
		sendMail();
	}
	
	
	
	public void sendMail(){
		new Thread(){
	            public void run() {
				try {
					log.info("before message");	
					Message message = new MimeMessage(session);
					message.setFrom(new InternetAddress("contact@mercue.biz"));
					InternetAddress[] iAdressArray = new InternetAddress[receivers.size()];
					for(int i = 0 ; i< receivers.size() ; i ++) {
						iAdressArray[i] = new InternetAddress(receivers.get(i));
					}

					
					message.setRecipients(Message.RecipientType.TO,iAdressArray);
					message.setSubject(subject);
					if(!StringUtils.isNULL(content)) {
						message.setText(content);
					}
					
					if(!StringUtils.isNULL(htmlContent)) {
						message.setContent(htmlContent,"text/html;charset=UTF-8");
					}
					
					
					if(sendDate !=null) {
						message.setSentDate(sendDate);
					}
					
					log.info("before send");	
					Transport.send(message);
					log.info("Done");
				} catch (MessagingException e) {
					log.error("MessagingException :"+e.getMessage());
					
				}
	       }
		
		}.start();
	}
	
}
