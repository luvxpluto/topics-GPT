package topics.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import topics.backend.dto.TopicDTO;
import topics.backend.model.User;
import topics.backend.service.TopicService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@RestController
@RequestMapping("/topics")
public class TopicController {
  private final TopicService topicService;

  public TopicController(TopicService topicService){
    this.topicService = topicService;
  }

  @PostMapping("/create")
  public ResponseEntity<TopicDTO> createTopic(@RequestBody TopicDTO topicDTO){
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User currentUser = (User) authentication.getPrincipal();
    TopicDTO createdTopic = topicService.createTopic(topicDTO, currentUser);
    return ResponseEntity.ok(createdTopic);
  }

  @GetMapping("/all")
  public ResponseEntity<List<TopicDTO>> getAllTopics(){
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User currentUser = (User) authentication.getPrincipal();
    List<TopicDTO> topics = topicService.getAllTopicsByUser(currentUser);
    return ResponseEntity.ok(topics);
  }
}
