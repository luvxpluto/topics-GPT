package topics.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    text.setWordsCount(textDTO.getWordsCount());
    text.setTopic(topic);
    Text savedText = textRepository.save(text);
    return convertToDTO(savedText);
  }

  public List<TextDTO> getAllTextsByTopic(Topic topic) {
    List<Text> texts = textRepository.findAllByTopic(topic);
    return texts.stream().map(this::convertToDTO).collect(Collectors.toList());
  }

  private TextDTO convertToDTO(Text text) {
    TextDTO textDTO = new TextDTO();
    textDTO.setId(text.getId());
    textDTO.setContent(text.getContent());
    textDTO.setWordsCount(text.getWordsCount());
    return textDTO;
  }
}
