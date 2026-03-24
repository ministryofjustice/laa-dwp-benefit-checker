package uk.gov.justice.laa.bc.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import uk.gov.justice.laa.bc.model.BenefitCheckRequestBody;
import uk.gov.justice.laa.bc.model.BenefitCheckResponseBody;
import uk.gov.justice.laa.bc.service.BenefitCheckerService;
import uk.gov.justice.laa.bc.service.ConfigurationService;

@WebMvcTest(uk.gov.justice.laa.bc.controller.BenefitCheckerController.class)
class BenefitCheckerControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private BenefitCheckerService mockBenefitCheckerService;
  @MockitoBean
  private ConfigurationService mockConfigurationService;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void performCheck_returnsCreatedStatus() throws Exception {

    BenefitCheckRequestBody request =
            BenefitCheckRequestBody.builder()
                    .clientReference("ABC123")
                    .nino("AB123456C")
                    .dateOfAward("20200101")
                    .dateOfBirth("19900101")
                    .clientUserId("cl_user_id_1234")
                    .clientOrgId("ab_orgc_12_34")
                    .lscServiceName("bc1-api")
                    .surname("Doe")
                    .build();

    BenefitCheckResponseBody expectedResponse = BenefitCheckResponseBody.builder()
            .benefitCheckerStatus("Ok")
            .build();

    when(mockBenefitCheckerService.performCheck(request))
        .thenReturn(expectedResponse);
    when(mockConfigurationService.containsScopedPrincipal(any(),
        anyString(), anyString(), anyString()))
        .thenReturn(true);

    MvcResult result = mockMvc
            .perform(post("/api/v1/benefitsCheck")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toJson(request))
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

    String json = result.getResponse().getContentAsString();
    BenefitCheckResponseBody responseBody = objectMapper.readValue(
        json, BenefitCheckResponseBody.class);

    assertNotNull(responseBody);
    assertEquals("Ok", responseBody.getBenefitCheckerStatus());
  }

  @Test
  void performCheck_returnsBadRequestStatus() throws Exception {
    BenefitCheckRequestBody itemRequestBody = BenefitCheckRequestBody.builder().build();
    when(mockBenefitCheckerService.performCheck(itemRequestBody))
            .thenReturn(BenefitCheckResponseBody.builder().build());

    mockMvc
            .perform(post("/api/v1/benefitsCheck")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toJson(BenefitCheckRequestBody.builder()
                            .build()))
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());

  }

  @SneakyThrows
  String toJson(BenefitCheckRequestBody benefitCheckRequestBody) {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.writeValueAsString(benefitCheckRequestBody);
  }
}
