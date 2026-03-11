package uk.gov.justice.laa.bc.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import uk.gov.justice.laa.bc.client.DwpClient;

@ExtendWith(MockitoExtension.class)
class WebServiceClientConfigTest {
  private WebServiceClientConfig config;

  @BeforeEach
  void setUp() {
    config = new WebServiceClientConfig();
  }

  @Test
  void marshaller_shouldSetCorrectContextPath() {
    // Act
    Jaxb2Marshaller marshaller = config.marshaller();

    // Assert
    assertNotNull(marshaller);
    assertEquals(
        "uk.gov.dwp.common.cis.getbenefitstatusext.service._3",
        marshaller.getContextPath()
    );
  }


  @Test
  void dwpClient() {
    // Arrange
    Jaxb2Marshaller mockMarshaller = new Jaxb2Marshaller();

    // Act
    DwpClient client = config.dwpClient(mockMarshaller);

    // Assert
    assertNotNull(client);
    assertEquals("http://localhost:8080/ws", client.getDefaultUri());
    assertSame(mockMarshaller, client.getMarshaller());
    assertSame(mockMarshaller, client.getUnmarshaller());

  }
}