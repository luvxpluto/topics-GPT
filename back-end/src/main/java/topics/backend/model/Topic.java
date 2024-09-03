package topics.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Table;
import lombok.*;


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
}
