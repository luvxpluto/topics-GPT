package topics.backend.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "texts")
public class Text {
  @Id
  @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
  private Long id;

  @Column(columnDefinition = "TEXT", nullable = false)
  private String content;

  @ManyToOne
  @JoinColumn(name = "topic_name", nullable = false)
  private Topic topic;

  @Column(nullable = false)
  private int wordsCount;

  public int countWords() {
    return content.split("\\s+").length;
  }
}
