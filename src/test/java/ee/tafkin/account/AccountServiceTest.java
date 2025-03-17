package ee.tafkin.account;

import ee.tafkin.account.domain.Account;
import ee.tafkin.account.domain.AccountCreationDto;
import ee.tafkin.errorhandling.BadRequestException;
import ee.tafkin.errorhandling.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
  @Mock
  private AccountRepository accountRepository;
  @Spy
  @InjectMocks
  private AccountService accountService;

  @Test
  public void createAccount_returnsAccount() {
    AccountCreationDto accountCreationDto = new AccountCreationDto("Test", "5123123");
    when(accountRepository.save(Account.fromDto(accountCreationDto))).thenReturn(Account.builder().id(1L).build());

    Long id = accountService.createAccount(accountCreationDto);

    assertThat(id).isEqualTo(1L);
    verify(accountService).validatePhoneIsUnique(accountCreationDto.getPhoneNr());
  }

  @Test
  public void findById_returnsAccount() {
    when(accountRepository.findById(1L)).thenReturn(Optional.of(Account.builder().id(1L).build()));

    assertThat(accountService.getAccountById(1L).getId()).isEqualTo(1L);
  }

  @Test
  public void findById_throwsNotFoundWhenMissing() {
    when(accountRepository.findById(1L)).thenReturn(Optional.empty());

    assertThatExceptionOfType(ResourceNotFoundException.class)
      .isThrownBy(() -> accountService.getAccountById(1L).getId())
      .withMessage("account with id 1 not found");
  }

  @Test
  public void deleteAccountById_callsRepository() {
    doReturn(null).when(accountService).getAccountById(1L);

    accountService.deleteAccountById(1L);
    verify(accountRepository).deleteById(1L);
  }

  @Test
  public void updateAccount_throwsWhenNotFound() {
    when(accountRepository.findForUpdateById(1L)).thenReturn(null);

    assertThatExceptionOfType(ResourceNotFoundException.class)
      .isThrownBy(() -> accountService.updateAccount(new AccountCreationDto("name", null), 1L))
      .withMessage("account with id 1 not found");
  }

  @Test
  public void updateAccount_updatesValuesAndCallsRepository() {
    when(accountRepository.findForUpdateById(1L)).thenReturn(Account.builder().id(1L).build());
    doNothing().when(accountService).validatePhoneIsUnique("5123123");

    accountService.updateAccount(new AccountCreationDto("name", "5123123"), 1L);

    verify(accountRepository).findForUpdateById(1L);
    verify(accountRepository).save(Account.builder().id(1L).name("name").phoneNr("+3725123123").build());
    verify(accountService).validatePhoneIsUnique("5123123");
  }

  @Test
  public void validatePhoneIsUnique_okWhenNull() {
    assertThatNoException().isThrownBy(() -> accountService.validatePhoneIsUnique(null));
  }

  @Test
  public void validatePhoneIsUnique_okWhenNoMatch() {
    when(accountRepository.existsByPhoneNr("+3725123123")).thenReturn(false);

    assertThatNoException().isThrownBy(() -> accountService.validatePhoneIsUnique("5123123"));
  }

  @Test
  public void validatePhoneIsUnique_throwsWhenPhoneExists() {
    when(accountRepository.existsByPhoneNr("+3725123123")).thenReturn(true);

    assertThatExceptionOfType(BadRequestException.class)
      .isThrownBy(() -> accountService.validatePhoneIsUnique("5123123"))
      .withMessage("phone number 5123123 already in use");
  }
}
