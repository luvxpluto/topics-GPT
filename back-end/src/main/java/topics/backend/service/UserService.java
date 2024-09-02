package topics.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import topics.backend.dto.UserDTO;
import topics.backend.model.User;
import topics.backend.repository.UserRepository;

@Service
public class UserService {
  private final UserRepository userRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.userRepository = userRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  @Transactional
  public UserDTO createUser(UserDTO userDTO) {
    if(userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
      throw new ResponseStatusException(HttpStatus.CONFLICT,"User already exists");
    }
    User user = new User();
    user.setEmail(userDTO.getEmail());
    user.setName(userDTO.getName());
    user.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
    userRepository.save(user);

    User savedUser = userRepository.save(user);
    return convertToDTO(savedUser);
  }

  public boolean authenticateUser(UserDTO userDTO) {
    User user = userRepository.findByEmail(userDTO.getEmail()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    return bCryptPasswordEncoder.matches(userDTO.getPassword(), user.getPassword());
  }


  private  UserDTO convertToDTO(User user) {
    UserDTO userDTO = new UserDTO();
    userDTO.setId(user.getId());
    userDTO.setEmail(user.getEmail());
    userDTO.setName(user.getName());
    return userDTO;
  }

}
