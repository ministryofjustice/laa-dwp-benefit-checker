package uk.gov.justice.laa.bc.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

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

@WebMvcTest(BenefitCheckerController.class)
class BenefitCheckerControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private BenefitCheckerService mockBenefitCheckerService;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void performCheck_returnsCreatedStatus() throws Exception {

    BenefitCheckRequestBody request =
            BenefitCheckRequestBody.builder()
                    .clientReference("clientReference")
                    .nino("nino")
                    .dateOfAward("211226")
                    .dateOfBirth("171226")
                    .clientUserId("user")
                    .clientOrgId("org")
                    .lscServiceName("a")
                    .surname("smith")
                    .build();

    BenefitCheckResponseBody expectedResponse = BenefitCheckResponseBody.builder()
            .benefitCheckerStatus("Ok")
            .build();

    when(mockBenefitCheckerService.performCheck(request))
        .thenReturn(expectedResponse);

    MvcResult result = mockMvc
            .perform(post("/api/v1/benefitsCheck")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toJson(request))
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

    String json = result.getResponse().getContentAsString();
    BenefitCheckResponseBody responseBody = objectMapper.readValue(json, BenefitCheckResponseBody.class);

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
