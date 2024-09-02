package topics.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import topics.backend.model.Text;
import topics.backend.model.Topic;
import java.util.List;

public interface TextRepository  extends JpaRepository<Text, Long>{
  List<Text> findAllByTopic(Topic topic);
}