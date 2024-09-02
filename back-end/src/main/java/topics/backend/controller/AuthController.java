package topics.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import topics.backend.config.JwtService;
import topics.backend.dto.LoginUserDTO;
import topics.backend.dto.RegisterUserDTO;
import topics.backend.model.User;
import topics.backend.service.AuthenticationService;
import topics.backend.response.LoginResponse;

@RestController
@RequestMapping("/auth")
public class AuthController{
  private final JwtService jwtService;
  private final AuthenticationService authenticationService;

  public AuthController(JwtService jwtService, AuthenticationService authenticationService){
    this.jwtService = jwtService;
    this.authenticationService = authenticationService;
  }

  @PostMapping("/signup")
  public ResponseEntity<User> register(@RequestBody RegisterUserDTO registerUserDTO){
    User registeredUser = authenticationService.signup(registerUserDTO);
    return ResponseEntity.ok(registeredUser);
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDTO loginUserDTO){
    User authenticatedUser = authenticationService.authenticate(loginUserDTO);

    String jwtToken = jwtService.generateToken(authenticatedUser);

    LoginResponse loginResponse = new LoginResponse();
    loginResponse.setToken(jwtToken);
    loginResponse.setExpiresIn(jwtService.getExpirationTime());

    return ResponseEntity.ok(loginResponse);
  }
}
