package ee.tafkin.account;

import lombok.experimental.UtilityClass;

import static org.apache.commons.lang3.StringUtils.isBlank;

@UtilityClass
public class AccountUtil {
  public static String sanitizePhone(String phone) {
    if (isBlank(phone)) {
      return null;
    }
    if (phone.startsWith("00")) {
      phone = phone.replaceFirst("00", "+");
    }
    if (!phone.startsWith("+")) {
      phone = "+372" + phone;
    }
    return phone;
  }
}
