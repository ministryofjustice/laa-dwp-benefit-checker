package uk.gov.justice.laa.bc.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.justice.laa.bc.BenefitCheckerApplication;
import uk.gov.justice.laa.bc.model.BenefitCheckRequestBody;

@SpringBootTest(classes = BenefitCheckerApplication.class)
@AutoConfigureMockMvc
@Transactional
public class BenefitCheckerControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;



  @Test
  void shouldCreateItem() throws Exception {

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

    mockMvc
        .perform(
            post("/api/v1/benefitsCheck")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @SneakyThrows
  String toJson(BenefitCheckRequestBody benefitCheckRequestBody) {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.writeValueAsString(benefitCheckRequestBody);
  }


}
