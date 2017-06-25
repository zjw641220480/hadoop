package cn.itcast.tom.stormlog.logMonitor.mail;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

public class MessageSender {
	private static Logger logger = Logger.getLogger(MessageSender.class);

	// 发送邮件,邮件内容为文本格式,
	public static boolean sendMail(MailInfo mailInfo) {
		try {
			// 转化为合乎规范的Message,
			Message mailMessage = generateBaseInfo(mailInfo);
			mailMessage.setText(mailInfo.getMailContent());// 设置邮件消息的主要内容
			// 调用javax.mail包中的相关类,将此对象发送出去
			Transport.send(mailMessage); // 发送邮件
			logger.info("【 TEXT 邮件发送完毕，成功时间： " + System.currentTimeMillis() + " 】");
			return true;
		} catch (UnsupportedEncodingException | MessagingException e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * 
	 * @MethodName:sendHtmlMail
	 * @Description:邮件内容为html格式
	 * @param mailInfo
	 * @return
	 * @throws MessagingException
	 * @Time: 2017年6月25日 下午3:42:48
	 * @author: TOM
	 */
	public static boolean sendHtmlMail(MailInfo mailInfo) throws MessagingException {
		try {
			Message mailMessage = generateBaseInfo(mailInfo);
			Multipart multipart = new MimeMultipart();
			BodyPart html = new MimeBodyPart();
			// 设置HTML内容
			html.setContent(mailInfo.getMailContent(),"text/html; charset=utf-8");
			multipart.addBodyPart(html);
			
			mailMessage.setContent(multipart);// 将MiniMultipart对象设置为邮件内容
			Transport.send(mailMessage);// 发送邮件
            logger.info("【 HTML 邮件发送完毕，成功时间： " + System.currentTimeMillis() + " 】");
            System.out.println("send ok!");
            return true;
		} catch (UnsupportedEncodingException | MessagingException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * @MethodName:generateBaseInfo
	 * @Description:根据上送的邮件相关信息,组装成javax.mail包中规定的Message
	 * @param mailInfo
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws MessagingException
	 * @Time: 2017年6月25日 下午3:35:32
	 * @author: TOM
	 */
	private static Message generateBaseInfo(MailInfo mailInfo) throws UnsupportedEncodingException, MessagingException {
		// 判断是否需要身份验证
		MailAuthenticator authenticator = null;
		Properties properties = mailInfo.getProperties();
		// 如果需要身份认证,则创建一个密码验证器
		if (mailInfo.isAuthValidate()) {
			// 对发送方邮箱的账号进行用户名密码验证
			authenticator = new MailAuthenticator(mailInfo.getUserName(), mailInfo.getUserPassword());
		}
		Session sendMailSession = Session.getDefaultInstance(properties, authenticator);
		Message mailMessage = new MimeMessage(sendMailSession);
		Address from = new InternetAddress(mailInfo.getFromAddress(), mailInfo.getFromUserName());
		// 设置邮件消息的发送者
		mailMessage.setFrom(from);
		if (mailInfo.getToAddress() != null && mailInfo.getToAddress().contains(",")) {
			// Message.RecipientType.TO属性表示接收者的类型为TO
			// 这里表示,可以直接发送给多位接收者;InternetAddress这个类中的parse方法会进行拆分
			mailMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailInfo.getToAddress()));
		} else {
			// 只有一个接收者
			// 创建邮件的接收者地址，并设置到邮件消息中
			Address cc = new InternetAddress(mailInfo.getToAddress());
			mailMessage.setRecipient(Message.RecipientType.CC, cc);
		}
		// 设置消息发送主题
		mailMessage.setSubject(mailInfo.getMailSubject());
		// 设置邮件消息发送时间
		mailMessage.setSentDate(new Date());
		return mailMessage;
	}
}
