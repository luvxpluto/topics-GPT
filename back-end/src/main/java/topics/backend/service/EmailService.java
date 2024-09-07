package topics.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

@Service
public class EmailService {

  private final JavaMailSender emailSender;

  @Autowired
  public EmailService(JavaMailSender emailSender) {
    this.emailSender = emailSender;
  }

  public void sendEmailWithMultipleAttachments(String to, String subject, String text,
                                               String fileName, byte[] pdfContent, byte[] mdContent)
          throws MessagingException {
    // Crear un nuevo MimeMessage
    MimeMessage message = emailSender.createMimeMessage();

    // Construir el correo con MimeMessageHelper
    MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());

    helper.setTo(to);
    helper.setSubject(subject);
    helper.setText(text, true); // Permitir contenido HTML

    // Adjuntar archivo PDF si el contenido está disponible
    if (pdfContent != null && pdfContent.length > 0) {
      ByteArrayResource pdfResource = new ByteArrayResource(pdfContent);
      helper.addAttachment(fileName + ".pdf", pdfResource, "application/pdf");
    }

    // Adjuntar archivo Markdown si el contenido está disponible
    if (mdContent != null && mdContent.length > 0) {
      ByteArrayResource mdResource = new ByteArrayResource(mdContent);
      helper.addAttachment(fileName + ".md", mdResource, "text/markdown");
    }

    // Enviar el correo
    emailSender.send(message);
  }
}