package ee.tafkin.account;

import ee.tafkin.account.domain.Account;
import jakarta.persistence.LockModeType;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

  boolean existsByPhoneNr(@Pattern(regexp = "^[\\+|00]?[0-9]{0,3}[0-9]{7,8}") String phoneNr);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  Account findForUpdateById(Long id);
}
