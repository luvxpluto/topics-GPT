package topics.backend.service;

import com.lowagie.text.DocumentException;
import jakarta.mail.MessagingException;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;
import topics.backend.exceptions.ContentGenerationException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class ContentService {

  private final ChatGPTService chatGPTService;
  private final EmailService emailService;

  @Autowired
  public ContentService(ChatGPTService chatGPTService, EmailService emailService) {
    this.chatGPTService = chatGPTService;
    this.emailService = emailService;
  }

  public void generateAndSendContent(String prompt, String contentType, String to) {
    try {
      String content = chatGPTService.getResponse(prompt);
      String subject = getSubject(contentType);
      String emailBody = getEmailBody(contentType);
      System.out.println(content);

      byte[] pdfContent = generatePDF(content);
      byte[] mdContent = content.getBytes(StandardCharsets.UTF_8);

      emailService.sendEmailWithMultipleAttachments(to, subject, emailBody, contentType, pdfContent, mdContent);
    } catch (MessagingException e) {
      throw new ContentGenerationException("Error sending email", e);
      } catch (IOException e) {
      throw new ContentGenerationException("Error generating PDF", e);
    } catch (Exception e) {
      throw new ContentGenerationException("Unexpected error during content generation and sending", e);
    }
  }

  private byte[] generatePDF(String markdownContent) throws IOException {
    // MD -> HTML
    Parser parser = Parser.builder().build();
    HtmlRenderer renderer = HtmlRenderer.builder().build();
    String htmlContent = "<html><head><title>Document</title></head><body>"
            + renderer.render(parser.parse(markdownContent))
            + "</body></html>";

    // HTML -> PDF
    try (ByteArrayOutputStream pdfStream = new ByteArrayOutputStream()) {
      ITextRenderer rendererPDF = new ITextRenderer();
      rendererPDF.setDocumentFromString(htmlContent);
      rendererPDF.layout();
      rendererPDF.createPDF(pdfStream);
      return pdfStream.toByteArray();
    } catch (DocumentException e) {
      throw new RuntimeException(e);
    }
  }

  private String getSubject(String contentType) {
    return "summary".equalsIgnoreCase(contentType) ? "Your Structured Summary" : "Your Questions and Answers";
  }

  private String getEmailBody(String contentType) {
    String contentTypeString = "summary".equalsIgnoreCase(contentType) ? "summary" : "Q&A";
    return String.format("<h1>Your %s</h1><p>Please find attached your %s in PDF and Markdown formats.</p>",
            contentTypeString, contentTypeString);
  }

}
