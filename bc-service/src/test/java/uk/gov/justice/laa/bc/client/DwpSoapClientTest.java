package uk.gov.justice.laa.bc.client;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DwpSoapClientTest {

  @InjectMocks
  private DwpSoapClient dwpSoapClient;

  @Test
  void performCheck_shouldNotThrowAnything() {

    assertDoesNotThrow(dwpSoapClient::performCheck);
  }

}