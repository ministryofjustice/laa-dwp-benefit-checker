package uk.gov.justice.laa.bc.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import uk.gov.dwp.common.cis.getbenefitstatusext.service._3.Item;
import uk.gov.justice.laa.bc.model.BenefitCheckRequestBody;
import uk.gov.justice.laa.bc.model.BenefitCheckResponseBody;

@ExtendWith(MockitoExtension.class)
class BcWebServiceTest {

  @Mock
  private RestTemplate restTemplate;

  private BcWebService service;

  private static final String SOAP_URL = "http://test-soap.com";

  @BeforeEach
  void setup() {
    service = new BcWebService(restTemplate, SOAP_URL);
  }

  @Test
  void performCheck_ShouldReturnMappedResponse() throws Exception {
    // Arrange
    BenefitCheckRequestBody request = new BenefitCheckRequestBody();
    request.setSurname("Smith");
    request.setDateOfBirth("1990-01-01");
    request.setDateOfAward("2020-01-01");
    request.setNino("AA123456A");

    Item mockedItem = new Item();
    mockedItem.setId("12345");
    mockedItem.setValue("OK");

    BcWebService spyService = spy(service);
    doReturn(mockedItem).when(spyService).perform(any());

    // Act
    BenefitCheckResponseBody response = spyService.performCheck(request);

    // Assert
    assertEquals("12345", response.getConfirmationRef());
    assertEquals("OK", response.getBenefitCheckerStatus());
    assertEquals("CLIENT_ID", response.getOriginalClientRef());
  }

  @Test
  void perform_ShouldSendCorrectSoapEnvelopeAndParseResponse() throws Exception {
    // Arrange
    BenefitCheckRequestBody request = new BenefitCheckRequestBody();
    request.setSurname("Smith");
    request.setDateOfBirth("1990-01-01");
    request.setDateOfAward("2020-01-01");
    request.setNino("AA123456A");

    String xmlResponse =
        "<Envelope><Body><getBenefitStatusExtResponse>"
            + "<itemList><id>ABC123</id><value>APPROVED</value></itemList>"
            + "</getBenefitStatusExtResponse></Body></Envelope>";

    ResponseEntity<String> mockResponse = new ResponseEntity<>(xmlResponse, HttpStatus.OK);

    when(restTemplate.postForEntity(eq(SOAP_URL), any(HttpEntity.class), eq(String.class)))
        .thenReturn(mockResponse);

    // Act
    Item item = service.perform(request);

    // Assert SOAP parsed correctly
    assertEquals("ABC123", item.getId());
    assertEquals("APPROVED", item.getValue());

    // Verify HTTP request passed to RestTemplate
    ArgumentCaptor<HttpEntity<String>> captor = ArgumentCaptor.forClass(HttpEntity.class);
    verify(restTemplate).postForEntity(eq(SOAP_URL), captor.capture(), eq(String.class));

    HttpEntity<String> sentRequest = captor.getValue();
    HttpHeaders headers = sentRequest.getHeaders();

    assertEquals(MediaType.APPLICATION_XML, headers.getContentType());
    assertEquals("", headers.getFirst("SOAPAction"));
  }

  @Test
  void perform_ShouldReplaceNullNinoWithNil() throws Exception {
    BenefitCheckRequestBody request = new BenefitCheckRequestBody();
    request.setSurname("Smith");
    request.setDateOfBirth("1990-01-01");
    request.setDateOfAward("2020-01-01");
    request.setNino(null);

    String xmlResponse =
        "<Envelope><Body><getBenefitStatusExtResponse>"
            + "<itemList><id>X1</id><value>NONE</value></itemList>"
            + "</getBenefitStatusExtResponse></Body></Envelope>";

    when(restTemplate.postForEntity(eq(SOAP_URL), any(HttpEntity.class), eq(String.class)))
        .thenReturn(ResponseEntity.ok(xmlResponse));

    // Act
    service.perform(request);

    // Verify the SOAP payload contains "nil" for NINO
    ArgumentCaptor<HttpEntity<String>> captor = ArgumentCaptor.forClass(HttpEntity.class);
    verify(restTemplate).postForEntity(eq(SOAP_URL), captor.capture(), eq(String.class));

    String xmlSent = captor.getValue().getBody();
    assertTrue(xmlSent.contains("<m0:nino>nil</m0:nino>"));
  }

  @Test
  void perform_ShouldThrowException_WhenXmlCannotBeParsed() {
    BenefitCheckRequestBody request = new BenefitCheckRequestBody();
    request.setSurname("Smith");
    request.setDateOfBirth("1990-01-01");
    request.setDateOfAward("2020-01-01");
    request.setNino("AB123456C");

    when(restTemplate.postForEntity(eq(SOAP_URL), any(HttpEntity.class), eq(String.class)))
        .thenReturn(ResponseEntity.ok("<bad-xml>"));

    // Act + Assert
    assertThrows(Exception.class, () -> service.perform(request));
  }

}