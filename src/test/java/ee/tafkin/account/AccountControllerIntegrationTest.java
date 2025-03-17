package ee.tafkin.account;

import ee.tafkin.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AccountControllerIntegrationTest extends BaseIntegrationTest {

  @Test
  @Sql(scripts = "/account/accounts.sql")
  public void createAccount_returnsId() throws Exception {
    MvcResult result = mvc.perform(post("/accounts")
        .contentType(APPLICATION_JSON)
        .content("""
          {
          "name": "person",
          "phoneNr": "5112233"
          }
          """)).andExpect(status().isCreated())
      .andReturn();

    assertThat(Long.parseLong(result.getResponse().getContentAsString())).isNotNull();
  }

  @Test
  @Sql(scripts = "/account/accounts.sql")
  public void updateAccount_returnsNoContent() throws Exception {
    mvc.perform(put("/accounts/1")
      .contentType(APPLICATION_JSON)
      .content("""
        {
        "name": "Isik-uus",
        "phoneNr": "+3725123125"
        }
        """)).andExpect(status().isNoContent());
  }

  @Test
  @Sql(scripts = "/account/accounts.sql")
  public void getAccountById_returnsAccount() throws Exception {
    mvc.perform(get("/accounts/1"))
      .andExpect(status().isOk())
      .andExpect(content().json("""
        {"id":1,"name":"Isik","phoneNr":"+3725123123"}"""));
  }

  @Test
  @Sql(scripts = "/account/accounts.sql")
  public void deleteAccountById_returnsNoContent() throws Exception {
    mvc.perform(delete("/accounts/2"))
      .andExpect(status().isNoContent());
  }
}
