package uk.gov.justice.laa.bc.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.justice.laa.bc.model.BenefitCheckRequestBody;
import uk.gov.justice.laa.bc.service.BenefitCheckerService;

@WebMvcTest(BenefitCheckerController.class)
class BenefitCheckerControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private BenefitCheckerService mockBenefitCheckerService;


  @Test
  void performCheck_returnsCreatedStatusAndLocationHeader() throws Exception {
    /*BenefitCheckRequestBody itemRequestBody = BenefitCheckRequestBody.builder().build();
    when(mockBenefitCheckerService.performCheck(itemRequestBody))
        .thenReturn("ACK");

    mockMvc
        .perform(post("/api/v1/items")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\": \"Item Three\", \"description\": \"This is an updated item three.\"}")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(header().string("Location", containsString("/api/v1/items/3")));*/
  }

  @Test
  void createItem_returnsBadRequestStatus() throws Exception {
    // This could be done in a better way
    /*mockMvc
        .perform(post("/api/v1/items")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\": \"Item Three\"}")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(content().json("{\"type\":\"about:blank\",\"title\":\"Bad Request\"," +
            "\"status\":400,\"detail\":\"Invalid request content.\",\"instance\":\"/api/v1/items\"}"));

    verify(mockBenefitCheckerService, never()).performCheck(any(BenefitCheckRequestBody.class));*/
  }
}
