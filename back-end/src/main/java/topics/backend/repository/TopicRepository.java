package topics.backend.repository;

import topics.backend.model.Topic;
import topics.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topic, Long> {
  Topic findAllByUser(User user);
}
