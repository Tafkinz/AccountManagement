package ee.tafkin.account;

import ee.tafkin.account.domain.Account;
import ee.tafkin.account.domain.AccountCreationDto;
import ee.tafkin.errorhandling.BadRequestException;
import ee.tafkin.errorhandling.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static ee.tafkin.account.AccountUtil.sanitizePhone;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {

  private final AccountRepository accountRepository;

  public Account getAccountById(Long id) {
    return accountRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("account with id %d not found".formatted(id)));
  }

  public Long createAccount(AccountCreationDto account) {
    validatePhoneIsUnique(account.getPhoneNr());
    return accountRepository.save(Account.fromDto(account)).getId();
  }

  public void deleteAccountById(Long id) {
    getAccountById(id);
    accountRepository.deleteById(id);
  }

  public void updateAccount(AccountCreationDto account, Long id) {
    Account accountToUpdate = accountRepository.findForUpdateById(id);
    if (accountToUpdate == null) {
      throw new ResourceNotFoundException("account with id %d not found".formatted(id));
    }
    if (!Objects.equals(sanitizePhone(account.getPhoneNr()), accountToUpdate.getPhoneNr())) {
      validatePhoneIsUnique(account.getPhoneNr());
    }
    accountToUpdate.setName(account.getName());
    accountToUpdate.setPhoneNr(sanitizePhone(account.getPhoneNr()));
    accountRepository.save(accountToUpdate);
  }

  void validatePhoneIsUnique(String phoneNr) {
    if (phoneNr != null && accountRepository.existsByPhoneNr(sanitizePhone(phoneNr))) {
      throw new BadRequestException("phone number %s already in use".formatted(phoneNr));
    }
  }
}
