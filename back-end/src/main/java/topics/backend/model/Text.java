package topics.backend.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "texts")
public class Text {
  @Id
  @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
  @Getter
  private Long id;

  @Column(columnDefinition = "TEXT", nullable = false)
  @Getter
  private String content;

  @ManyToOne
  @JoinColumn(name = "topic_name", nullable = false)
  private Topic topic;

  @Getter
  private String wordsCount;

  public Text(String content, Topic topic) {
    this.content = content;
    this.topic = topic;
    this.wordsCount = String.valueOf(content.split("\\s+").length);
  }
}
