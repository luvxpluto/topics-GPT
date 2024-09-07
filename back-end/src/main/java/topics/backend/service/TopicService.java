package topics.backend.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import topics.backend.exceptions.ContentGenerationException;
import topics.backend.model.Text;
import topics.backend.model.Topic;
import topics.backend.model.User;
import topics.backend.repository.TopicRepository;
import topics.backend.dto.TopicDTO;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TopicService {
  private final TopicRepository topicRepository;
  private final ContentService contentService;

  public TopicService(TopicRepository topicRepository, ContentService contentService) {
    this.topicRepository = topicRepository;
    this.contentService = contentService;
  }

  @Transactional
  public TopicDTO createTopic(TopicDTO topicDTO, User currentUser) {
    if (topicRepository.existsByNameAndUser(topicDTO.getName(), currentUser)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Topic already exists");
    }

    Topic topic = new Topic();
    topic.setName(topicDTO.getName());
    topic.setDescription(topicDTO.getDescription());
    topic.setUser(currentUser);

    Topic savedTopic = topicRepository.save(topic);
    return convertToDTO(savedTopic);
  }

  public List<TopicDTO> getAllTopicsByUser(User currentUser) {
    List<Topic> topics = topicRepository.findAllByUser(currentUser);
    return topics.stream().map(this::convertToDTO).collect(Collectors.toList());
  }

  public TopicDTO getTopicById(Long id, User currentUser) {
    Topic topic = topicRepository.findByIdAndUser(id, currentUser)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Topic not found"));
    return convertToDTO(topic);
  }

  public Topic getTopicByName(String name, User currentUser) {
    return topicRepository.findByNameAndUser(name, currentUser)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Topic not found"));
  }

  @Transactional
  public TopicDTO updateTopic(TopicDTO topicDTO, User currentUser) {
    Topic topic = topicRepository.findByIdAndUser(topicDTO.getId(), currentUser)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Topic not found"));


    if (!topic.getName().equals(topicDTO.getName()) &&
            topicRepository.existsByNameAndUser(topicDTO.getName(), currentUser)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Topic already exists");
    }

    topic.setName(topicDTO.getName());
    topic.setDescription(topicDTO.getDescription());
    Topic savedTopic = topicRepository.save(topic);
    return convertToDTO(savedTopic);
  }

  @Transactional
  public void deleteTopic(Long id, User currentUser) {
    Topic topic = topicRepository.findByIdAndUser(id, currentUser)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Topic not found"));

    topicRepository.delete(topic);
  }

  public void generateSummary(Long id, User currentUser) {
    Topic topic = topicRepository.findByIdAndUser(id, currentUser)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Topic not found"));

    List<Text> texts = topic.getTexts();
    String content = texts.stream().map(Text::getContent).collect(Collectors.joining("\n"));
    String prompt = "Please summarize the following text in markdown format: " + content;

    try {
      contentService.generateAndSendContent(prompt,"summary",currentUser.getEmail());
    } catch (ContentGenerationException e) {
      // Optionally rethrow as a different exception if needed
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error generating summary", e);
    }
  }

  public void generateQA(Long id, User currentUser) {
    Topic topic = topicRepository.findByIdAndUser(id, currentUser)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Topic not found"));

    List<Text> texts = topic.getTexts();
    String content = texts.stream().map(Text::getContent).collect(Collectors.joining("\n"));
    String prompt = "Please generate questions and answers of the following text in markdown format: " + content;

    try {
      contentService.generateAndSendContent(prompt,"Q&A",currentUser.getEmail());
    } catch (ContentGenerationException e) {
      // Optionally rethrow as a different exception if needed
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error generating summary", e);
    }
  }

  private TopicDTO convertToDTO(Topic topic) {
    TopicDTO topicDTO = new TopicDTO();
    topicDTO.setId(topic.getId());
    topicDTO.setName(topic.getName());
    topicDTO.setDescription(topic.getDescription());
    return topicDTO;
  }
}

