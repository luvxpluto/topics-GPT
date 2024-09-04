package topics.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import topics.backend.dto.TextDTO;
import topics.backend.model.Topic;
import topics.backend.model.User;
import topics.backend.service.TextService;
import topics.backend.service.TopicService;

import java.util.List;

@RestController
@RequestMapping("/api/text")
public class TextController {
  private final TextService textService;
  private final TopicService topicService;

  public TextController(TextService textService, TopicService topicService) {
    this.textService = textService;
    this.topicService = topicService;
  }

  @PostMapping
  public ResponseEntity<TextDTO> createText(TextDTO textDTO, @AuthenticationPrincipal User currentUser) {
    Topic topic = topicService.getTopicByName(textDTO.getTopicName(), currentUser);
    TextDTO createdText = textService.createText(textDTO, topic);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdText);
  }

  @GetMapping
  public ResponseEntity<List<TextDTO>> getAllTextsByTopic(String topicName, @AuthenticationPrincipal User currentUser) {
    Topic topic = topicService.getTopicByName(topicName, currentUser);
    List<TextDTO> texts = textService.getAllTextsByTopic(topic);
    return ResponseEntity.ok(texts);
  }

  @GetMapping("/{id}")
  public ResponseEntity<TextDTO> getTextById(@PathVariable Long id) {
    TextDTO text = textService.getTextById(id);
    return ResponseEntity.ok(text);
  }

  @PutMapping("/{id}")
  public ResponseEntity<TextDTO> updateText(@PathVariable Long id, TextDTO textDTO) {
    textDTO.setId(id);
    TextDTO updatedText = textService.updateText(textDTO);
    return ResponseEntity.ok(updatedText);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteText(@PathVariable Long id) {
    textService.deleteText(id);
    return ResponseEntity.noContent().build();
  }

}
