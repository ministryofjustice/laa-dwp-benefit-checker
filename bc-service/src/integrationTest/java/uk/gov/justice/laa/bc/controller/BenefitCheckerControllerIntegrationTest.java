package uk.gov.justice.laa.bc.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.justice.laa.bc.BenefitCheckerApplication;
import uk.gov.justice.laa.bc.model.BenefitCheckRequestBody;

@SpringBootTest(classes = BenefitCheckerApplication.class)
@AutoConfigureMockMvc
public class BenefitCheckerControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;



  @Test
  void shouldCreateItem() throws Exception {

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

    mockMvc
        .perform(
            post("/api/v1/benefitsCheck")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  void shouldReturn400WhenValidateFail() throws Exception {

    BenefitCheckRequestBody request =
        BenefitCheckRequestBody.builder()
            .clientReference("ABC123")
            .nino("AB123456C")
            .dateOfAward("20200101")
            .dateOfBirth("19900101")
            .clientUserId("cl_user_id_1234")
            .clientOrgId("errorOrgId")
            .lscServiceName("SERVICE")
            .surname("Doe")
            .build();

    mockMvc
        .perform(
            post("/api/v1/benefitsCheck")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError());
  }

  @SneakyThrows
  String toJson(BenefitCheckRequestBody benefitCheckRequestBody) {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.writeValueAsString(benefitCheckRequestBody);
  }


}
