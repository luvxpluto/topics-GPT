package topics.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import topics.backend.dto.TopicDTO;
import topics.backend.model.User;
import topics.backend.service.TopicService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

@RestController
@RequestMapping("/api/topics")
public class TopicController {
  private final TopicService topicService;

  public TopicController(TopicService topicService){
    this.topicService = topicService;
  }

  @PostMapping
  public ResponseEntity<TopicDTO> createTopic(@RequestBody TopicDTO topicDTO, @AuthenticationPrincipal User currentUser){
    TopicDTO createdTopic = topicService.createTopic(topicDTO, currentUser);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdTopic);
  }

  @PostMapping("/summary/{id}")
  public ResponseEntity<TopicDTO> createResumen(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
    try {
      topicService.generateSummary(id, currentUser);
      return ResponseEntity.ok().build();
    } catch (ResponseStatusException e) {
      // This will handle the NOT_FOUND and INTERNAL_SERVER_ERROR cases
      throw e;
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", e);
    }
  }

  @PostMapping("/qa/{id}")
  public ResponseEntity<TopicDTO> createQA(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
    try {
      topicService.generateQA(id, currentUser);
      return ResponseEntity.ok().build();
    } catch (ResponseStatusException e) {
      // This will handle the NOT_FOUND and INTERNAL_SERVER_ERROR cases
      throw e;
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", e);
    }
  }

  @GetMapping
  public ResponseEntity<List<TopicDTO>> getAllTopics(@AuthenticationPrincipal User currentUser){
    List<TopicDTO> topics = topicService.getAllTopicsByUser(currentUser);
    return ResponseEntity.ok(topics);
  }

  @GetMapping("/{id}")
  public ResponseEntity<TopicDTO> getTopicById(@PathVariable Long id, @AuthenticationPrincipal User currentUser){
    TopicDTO topic = topicService.getTopicById(id, currentUser);
    return ResponseEntity.ok(topic);
  }

  @PutMapping("/{id}")
  public ResponseEntity<TopicDTO> updateTopic(@PathVariable Long id, @RequestBody TopicDTO topicDTO,
                                              @AuthenticationPrincipal User currentUser){
    topicDTO.setId(id);
    TopicDTO updatedTopic = topicService.updateTopic(topicDTO, currentUser);
    return ResponseEntity.ok(updatedTopic);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTopic(@PathVariable Long id, @AuthenticationPrincipal User currentUser){
      topicService.deleteTopic(id, currentUser);
      return ResponseEntity.ok().build();
  }
}
