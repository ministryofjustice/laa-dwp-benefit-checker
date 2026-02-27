package uk.gov.justice.laa.bc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import uk.gov.justice.laa.bc.controller.BcEndpoint;

import static uk.gov.justice.laa.bc.controller.BcEndpoint.LOCAL_PART;

/**
 * Setup config for SOAP service
 */
@Configuration(proxyBeanMethods = false)
public class WebServiceConfig {

  /**
   * Ensure the WSDL is returned correctly
   * @param benefitCheckerWs
   * @return
   */
  @Bean
  public DefaultWsdl11Definition countries(SimpleXsdSchema benefitCheckerWs) {
    DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
    wsdl11Definition.setPortTypeName(LOCAL_PART);
    wsdl11Definition.setLocationUri("/services");
    wsdl11Definition.setTargetNamespace(BcEndpoint.NAMESPACE_URI);
    wsdl11Definition.setSchema(benefitCheckerWs);
    return wsdl11Definition;
  }

}
