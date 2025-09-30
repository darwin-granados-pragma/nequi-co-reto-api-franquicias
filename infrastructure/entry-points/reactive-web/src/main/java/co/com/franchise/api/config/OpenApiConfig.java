package co.com.franchise.api.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Pragma MS-FRANCHISE API",
    version = "1.0.0",
    description = "API for managing franchise information",
    license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0"
    )
), servers = @Server(url = "localhost:8080", description = "dev")
)
public class OpenApiConfig {

}
