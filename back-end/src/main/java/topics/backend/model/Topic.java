package topics.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "topics", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "created_by"})
})
public class Topic {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String name;

  @Column(nullable = false)
  private String description;

  @ManyToOne
  @JoinColumn(name = "created_by", nullable = false)
  private User user;

  @OneToMany(mappedBy = "topic", cascade = CascadeType.REMOVE, orphanRemoval = true)
  private List<Text> texts;
}
