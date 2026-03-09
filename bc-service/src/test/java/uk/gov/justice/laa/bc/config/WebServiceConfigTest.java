package uk.gov.justice.laa.bc.config;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.transform.ResourceSource;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@ExtendWith(MockitoExtension.class)
class WebServiceConfigTest {
  private WebServiceConfig config;

  @BeforeEach
  void setUp() {
    config = new WebServiceConfig();
  }

  @Test
  void webServiceServlet() {

    // Act
    ApplicationContext context = mock(WebApplicationContext.class);
    ServletRegistrationBean<MessageDispatcherServlet> bean =
        config.webServiceServlet(context);

    // Assert
    assertNotNull(bean);
    assertEquals(1, bean.getUrlMappings().size());
    assertTrue(bean.getUrlMappings().contains("/ws/*"));

    MessageDispatcherServlet servlet = bean.getServlet();
    assertNotNull(servlet);
    assertNotNull(servlet.getWebApplicationContext());
    assertTrue(servlet.isTransformWsdlLocations());

  }

  @Test
  void checkSchema() {

    // Act
    XsdSchema schema = config.checkSchema();

    // Assert base object
    assertNotNull(schema);
    assertTrue(schema instanceof SimpleXsdSchema);

    // Assert resource path
    SimpleXsdSchema simpleSchema = (SimpleXsdSchema) schema;
    ResourceSource resource = (ResourceSource) simpleSchema.getSource();
    assertTrue(resource.getSystemId().contains("META-INF/schemas/LSC_BenefitChecker_WS_01.xsd"));
  }

  @Test
  void check() {

    // Arrange
    XsdSchema schema = mock(XsdSchema.class);

    // Act
    DefaultWsdl11Definition def = config.check(schema);

    // Assert
    assertNotNull(def);
  }
}