package topics.backend.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import topics.backend.dto.LoginUserDTO;
import topics.backend.dto.RegisterUserDTO;
import topics.backend.model.User;
import topics.backend.repository.UserRepository;

@Service
public class AuthenticationService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;

  public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                               AuthenticationManager authenticationManager){
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.authenticationManager = authenticationManager;
  }

  public User signup(RegisterUserDTO registerUserDTO) {
    User user = new User();
    user.setEmail(registerUserDTO.getEmail());
    user.setName(registerUserDTO.getName());
    user.setPassword(passwordEncoder.encode(registerUserDTO.getPassword()));
    return userRepository.save(user);
  }

  public User authenticate(LoginUserDTO loginUserDTO) {
    authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginUserDTO.getEmail(), loginUserDTO.getPassword()));
    return userRepository.findByEmail(loginUserDTO.getEmail())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
  }
}
