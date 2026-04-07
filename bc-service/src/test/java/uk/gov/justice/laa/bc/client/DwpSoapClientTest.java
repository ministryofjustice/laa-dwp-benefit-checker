package uk.gov.justice.laa.bc.client;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
class DwpSoapClientTest {

  @InjectMocks
  private DwpSoapClient dwpSoapClient;

  @Test
  void performCheck_shouldNotThrowAnything() {

    assertDoesNotThrow(dwpSoapClient::performCheck);
  }

}