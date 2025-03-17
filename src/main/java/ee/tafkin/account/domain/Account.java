package ee.tafkin.account.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

import static ee.tafkin.account.AccountUtil.sanitizePhone;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Account {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_generator")
  @SequenceGenerator(name = "account_generator", sequenceName = "ACCOUNT_SEQ", allocationSize = 3)
  private Long id;
  private String name;
  @Column(name = "phone_nr")
  private String phoneNr;
  @JsonIgnore
  @Column(name = "created_dtime")
  private OffsetDateTime createdDtime;
  @JsonIgnore
  @Column(name = "modified_dtime")
  private OffsetDateTime modifiedDtime;

  public static Account fromDto(AccountCreationDto account) {
    return Account.builder()
      .name(account.getName())
      .phoneNr(sanitizePhone(account.getPhoneNr()))
      .build();
  }
}
