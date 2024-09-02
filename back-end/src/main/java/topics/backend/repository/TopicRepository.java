package topics.backend.repository;

import topics.backend.model.Topic;
import topics.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicRepository extends JpaRepository<Topic, Long> {
  List<Topic> findAllByUser(User user);
  Topic existsByNameAndUser(String name, User user);
}
