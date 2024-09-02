package topics.backend.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import topics.backend.dto.UserDTO;
import topics.backend.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController{

  private final UserService userService;

  @Autowired
  public AuthController(UserService userService){
    this.userService = userService;
  }

  @PostMapping("/register")
  public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO){
    UserDTO createdUser = userService.createUser(userDTO);
    return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
  }

  @PostMapping("/login")
  public ResponseEntity<?> loginUser(@RequestBody UserDTO userDTO){
    if(userService.authenticateUser(userDTO)){
      return new ResponseEntity<>(HttpStatus.OK);
    }
    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
  }
}
