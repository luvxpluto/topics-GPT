package topics.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import topics.backend.dto.TextDTO;
import topics.backend.model.Topic;
import topics.backend.repository.TopicRepository;
import topics.backend.service.TextService;

import java.util.List;

@RestController
@RequestMapping("/text")
public class TextController {
  private final TextService textService;
  private final TopicRepository topicRepository;

  public TextController(TextService textService, TopicRepository topicRepository) {
    this.textService = textService;
    this.topicRepository = topicRepository;
  }

  @PostMapping("/create")
  public ResponseEntity<TextDTO> createText(TextDTO textDTO) {
    Topic topic = topicRepository.findByName(textDTO.getTopicName())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Topic not found"));

    TextDTO createdText = textService.createText(textDTO, topic);
    return ResponseEntity.ok(createdText);
  }

  @GetMapping("/all")
  public ResponseEntity<List<TextDTO>> getAllTextsByTopic(String topicName) {
    Topic topic = topicRepository.findByName(topicName)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Topic not found"));

    List<TextDTO> texts = textService.getAllTextsByTopic(topic);
    return ResponseEntity.ok(texts);
  }
}
