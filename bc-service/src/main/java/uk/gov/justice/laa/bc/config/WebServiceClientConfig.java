package uk.gov.justice.laa.bc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import uk.gov.justice.laa.bc.client.DwpClient;

@Configuration
public class WebServiceClientConfig {

  @Bean
  public Jaxb2Marshaller marshaller() {
    Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
    marshaller.setContextPath("uk.gov.dwp.common.cis.getbenefitstatusext.service._3");
    return marshaller;
  }
  @Bean
  public DwpClient dwpClient(Jaxb2Marshaller marshaller) {
    DwpClient client = new DwpClient();
    client.setDefaultUri("http://localhost:8080/ws");
    client.setMarshaller(marshaller);
    client.setUnmarshaller(marshaller);
    return client;
  }
}
