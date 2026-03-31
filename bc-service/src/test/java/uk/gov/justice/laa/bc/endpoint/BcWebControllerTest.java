package uk.gov.justice.laa.bc.endpoint;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.justice.laa.bc.model.BenefitCheckRequestBody;
import uk.gov.justice.laa.bc.model.BenefitCheckResponseBody;
import uk.gov.justice.laa.bc.service.BcWebService;
import uk.gov.justice.laa.bc.service.ConfigurationService;

@WebMvcTest(BcWebController.class)
class BcWebControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private BcWebService service;
  @MockitoBean
  private ConfigurationService mockConfigurationService;

  @Test
  void shouldReturnOkResponseWhenServiceReturnsSuccess() throws Exception {
    BenefitCheckRequestBody request =
        BenefitCheckRequestBody.builder()
            .clientReference("ABC123")
            .nino("AB123456C")
            .dateOfAward("20200101")
            .dateOfBirth("19900101")
            .clientUserId("cl_user_id_1234")
            .clientOrgId("ab_orgc_12_34")
            .lscServiceName("SERVICE")
            .surname("Doe")
            .build();

    BenefitCheckResponseBody response = new BenefitCheckResponseBody();
    ObjectMapper objectMapper = new ObjectMapper();
    when(service.performCheck(any())).thenReturn(response);
    when(mockConfigurationService.containsScopedPrincipal(any(),
        anyString(), anyString(), anyString()))
        .thenReturn(true);
    mockMvc.perform(post("/api/v1/benefitsCheck")   // <-- The path defined in BenefitsCheckerApi
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  void shouldThrowRuntimeExceptionWhenServiceThrowsError() throws Exception {
    BenefitCheckRequestBody request = BenefitCheckRequestBody.builder()
        .clientReference("ABC123")
        .nino("AB123456C")
        .dateOfAward("20200101")
        .dateOfBirth("19900101")
        .clientUserId("cl_user_id_1234")
        .clientOrgId("ab_orgc_12_34")
        .lscServiceName("SERVICE")
        .surname("Doe")
        .build();
    ObjectMapper objectMapper = new ObjectMapper();
    when(service.performCheck(any())).thenThrow(new RuntimeException("Service error"));
    when(mockConfigurationService.containsScopedPrincipal(any(),
        anyString(), anyString(), anyString()))
        .thenReturn(true);
    mockMvc.perform(post("/api/v1/benefitsCheck")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isInternalServerError());
  }
}