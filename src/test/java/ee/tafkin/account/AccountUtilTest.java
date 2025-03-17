package ee.tafkin.account;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static ee.tafkin.account.AccountUtil.sanitizePhone;
import static org.assertj.core.api.Assertions.assertThat;

class AccountUtilTest {

  @ParameterizedTest
  @CsvSource({"5123123","003725123123","+3725123123"})
  public void sanitizePhone_createsEstonianPhone(String phone) {
    assertThat(sanitizePhone(phone)).isEqualTo("+3725123123");
  }

  @Test
  public void sanitizePhone_doesNothingWhenForeignNumber() {
    assertThat(sanitizePhone("+3585123123")).isEqualTo("+3585123123");
  }

  @Test
  public void sanitizePhone_returnsNullWhenPhoneIsNull() {
    assertThat(sanitizePhone(null)).isNull();
  }

}
