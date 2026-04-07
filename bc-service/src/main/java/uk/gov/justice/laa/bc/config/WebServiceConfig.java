package uk.gov.justice.laa.bc.config;

import static uk.gov.justice.laa.bc.endpoint.BcEndpoint.LOCAL_PART;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;
import uk.gov.justice.laa.bc.endpoint.BcEndpoint;


/**
 * Setup config for SOAP service.
 */
@EnableWs
@ComponentScan(basePackages = "uk.gov.justice.laa.bc.endpoint")
@Configuration(proxyBeanMethods = false)
public class WebServiceConfig {

  /**
   * webServiceServlet bean.
   *
   * @param context context
   * @return ServletRegistrationBean
   */
  @Bean
  public ServletRegistrationBean<MessageDispatcherServlet> webServiceServlet(
          ApplicationContext context) {
    MessageDispatcherServlet servlet = new MessageDispatcherServlet();
    servlet.setApplicationContext(context);
    servlet.setTransformWsdlLocations(true);
    return new ServletRegistrationBean<>(servlet, "/ws/*");
  }


  @Bean
  public XsdSchema checkSchema() {
    return new SimpleXsdSchema(
            new ClassPathResource("META-INF/schemas/LSC_BenefitChecker_WS_01.xsd"));
  }

  /**
   * check Bean.
   *
   * @param checkSchema checkSchema
   * @return DefaultWsdl11Definition
   */
  @Bean(name = "check")
  public DefaultWsdl11Definition check(XsdSchema checkSchema) {
    DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
    wsdl11Definition.setPortTypeName(LOCAL_PART);
    wsdl11Definition.setLocationUri("/ws");
    wsdl11Definition.setTargetNamespace(BcEndpoint.NAMESPACE_URI);
    wsdl11Definition.setSchema(checkSchema);
    return wsdl11Definition;
  }

  /*@Bean
  public SaajSoapMessageFactory messageFactory() {
    SaajSoapMessageFactory factory = new SaajSoapMessageFactory();
    factory.setSoapVersion(SoapVersion.SOAP_12);
    return factory;
  }

  // Interceptors
  @Bean
  SimplePasswordValidationCallbackHandler callbackHandler() {
    SimplePasswordValidationCallbackHandler callbackHandler
    = new SimplePasswordValidationCallbackHandler();
    callbackHandler.setUsersMap(Collections.singletonMap(userName, password));
    return callbackHandler;
  }

  @Bean
   public Wss4jSecurityInterceptor wss4jSecurityInterceptor() {
     Wss4jSecurityInterceptor interceptor = new Wss4jSecurityInterceptor();
     interceptor.setValidationActions(WSConstants.USERNAME_TOKEN_LN);
     interceptor.setValidationCallbackHandler(callbackHandler());
     return interceptor;
   }
   @Bean
     PayloadLoggingInterceptor payloadLoggingInterceptor() {
     return new PayloadLoggingInterceptor();
   }*/

}
