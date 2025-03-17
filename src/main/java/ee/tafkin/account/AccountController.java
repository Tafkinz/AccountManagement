package ee.tafkin.account;

import ee.tafkin.account.domain.Account;
import ee.tafkin.account.domain.AccountCreationDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

  private final AccountService accountService;

  @GetMapping("/{id}")
  @Operation(summary = "Get account by id")
  public ResponseEntity<Account> getAccountById(@Parameter(description = "id of account to get") @PathVariable Long id) {
    return ok(accountService.getAccountById(id));
  }

  @PostMapping
  @Operation(summary = "Create a new account, returning id")
  public ResponseEntity<Long> createAccount(@Valid @RequestBody AccountCreationDto account) {
    Long createdId = accountService.createAccount(account);
    return new ResponseEntity<>(createdId, CREATED);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update account properties by ID")
  public ResponseEntity<Void> updateAccount(
    @Parameter(description = "id of account to update") @PathVariable Long id,
    @Valid @RequestBody AccountCreationDto account) {
    accountService.updateAccount(account, id);
    return noContent().build();
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete account by ID")
  public ResponseEntity<Void> deleteAccount(@Parameter(description = "id of account to delete") @PathVariable Long id) {
    accountService.deleteAccountById(id);
    return noContent().build();
  }
}
