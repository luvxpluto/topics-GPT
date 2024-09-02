package topics.backend.dto;

import lombok.Data;

@Data
public class RegisterUserDTO {
  private String email;
  private String name;
  private String password;
}
