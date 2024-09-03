package topics.backend.dto;

import lombok.Data;

@Data
public class TextDTO {
  private Long id;
  private String content;
  private int wordsCount;
  private String topicName;
}
