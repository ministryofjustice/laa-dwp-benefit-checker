package uk.gov.justice.laa.bc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import uk.gov.justice.laa.bc.client.DwpClient;

/**
 * WebServiceClientConfig.
 */
@Configuration
public class WebServiceClientConfig {

  /**
   * marshaller.
   *
   * @return Jaxb2Marshaller
   */
  @Bean
  public Jaxb2Marshaller marshaller() {
    Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
//    marshaller.setContextPath("uk.gov.dwp.common.cis.getbenefitstatusext.service._3");
//    marshaller.setPackagesToScan("uk.gov.dwp.common.cis.getbenefitstatusext");

    marshaller.setPackagesToScan(
        "uk.gov.dwp.common.cis.getbenefitstatusext.data._3",
        "uk.gov.dwp.common.cis.getbenefitstatusext.service"
    );

    return marshaller;
  }

  /**
   * dwpClient.
   *
   * @param marshaller Jaxb2Marshaller
   * @return DwpClient
   */
  @Bean
  public DwpClient dwpClient(Jaxb2Marshaller marshaller) {
    DwpClient client = new DwpClient();
    //client.setDefaultUri("http://localhost:8080/ws");
    client.setDefaultUri("http://localhost:8080/axis/services/CorporateCISGetBenefitStatusExtWS03?wsdl");

    client.setMarshaller(marshaller);
    client.setUnmarshaller(marshaller);
    return client;
  }
}
