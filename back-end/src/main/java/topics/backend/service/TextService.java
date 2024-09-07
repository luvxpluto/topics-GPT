package topics.backend.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import topics.backend.dto.TextDTO;
import topics.backend.model.Text;
import topics.backend.model.Topic;
import topics.backend.repository.TextRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TextService {
  private final TextRepository textRepository;

  public TextService(TextRepository textRepository) {
    this.textRepository = textRepository;
  }

  @Transactional
  public TextDTO createText(TextDTO textDTO, Topic topic) {
    Text text = new Text();
    text.setContent(textDTO.getContent());
    text.setWordsCount(text.countWords());
    text.setTopic(topic);
    Text savedText = textRepository.save(text);
    return convertToDTO(savedText);
  }

  public List<TextDTO> getAllTextsByTopic(Topic topic) {
    List<Text> texts = textRepository.findAllByTopic(topic);
    return texts.stream().map(this::convertToDTO).collect(Collectors.toList());
  }

  public TextDTO getTextById(Long id) {
    Text text = textRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Text not found"));
    return convertToDTO(text);
  }

  @Transactional
  public TextDTO updateText(TextDTO textDTO, Topic topic) {
    Text text = textRepository.findById(textDTO.getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Text not found"));
    text.setTopic(topic);
    text.setContent(textDTO.getContent());
    text.setWordsCount(text.countWords());
    Text updatedText = textRepository.save(text);
    return convertToDTO(updatedText);
  }

  @Transactional
  public void deleteText(Long id) {
    Text text = textRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Text not found"));
    textRepository.delete(text);
  }


  private TextDTO convertToDTO(Text text) {
    TextDTO textDTO = new TextDTO();
    textDTO.setId(text.getId());
    textDTO.setContent(text.getContent());
    textDTO.setWordsCount(text.getWordsCount());
    textDTO.setTopicName(text.getTopic().getName());
    return textDTO;
  }
}
