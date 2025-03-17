package ee.tafkin.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.SpecVersion;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@OpenAPIDefinition
public class SwaggerConfig {

  @Bean
  public OpenAPI springShopOpenAPI() {
    List<Server> servers = new ArrayList<>();
    servers.add(new Server().url("/").description("Default server URL"));

    Info info = this.info("Documentation for LHV Test task. System name: AccountManagementService");

    ExternalDocumentation externalDocs = new ExternalDocumentation()
      .description("AccountManagementService documentation")
      .url("https://github.com/to-be-done?");

    return new OpenAPI(SpecVersion.V31)
      .servers(servers)
      .info(info)
      .externalDocs(externalDocs);
  }

  private Info info(String description) {
    return new Info()
      .title("AccountManagementService")
      .description(description)
      .contact(new Contact()
        .name("Taavi Kivimaa")
        .email("tafkinz@gmail.com")
      );
  }
}
