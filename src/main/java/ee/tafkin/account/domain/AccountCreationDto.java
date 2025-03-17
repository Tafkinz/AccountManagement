package ee.tafkin.account.domain;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountCreationDto {
  @NotBlank
  @Size(min = 5, max = 20)
  private String name;
  @Column(name = "phone_nr")
  @Pattern(regexp = "^[\\+|00]?[0-9]{0,3}[0-9]{7,8}")
  private String phoneNr;
}
