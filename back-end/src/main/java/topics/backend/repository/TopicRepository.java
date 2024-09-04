package topics.backend.repository;

import topics.backend.model.Topic;
import topics.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TopicRepository extends JpaRepository<Topic, Long> {
  List<Topic> findAllByUser(User user);
  Boolean existsByNameAndUser(String name, User user);

  Optional<Topic> findByName(String name);

  Optional<Topic> findByIdAndUser(Long id, User user);
}
