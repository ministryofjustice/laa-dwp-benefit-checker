package uk.gov.justice.laa.bc.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.justice.laa.bc.BenefitCheckerApplication;
import uk.gov.justice.laa.bc.model.Item;
import uk.gov.justice.laa.bc.model.ItemRequestBody;
import uk.gov.justice.laa.bc.service.ItemService;

import java.util.List;

@SpringBootTest(classes = BenefitCheckerApplication.class)
@AutoConfigureMockMvc
@Transactional
public class ItemControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private ItemService mockItemService;

  @Test
  void shouldGetAllItems() throws Exception {

    List<Item> items = getItems();
    when(mockItemService.getAllItems()).thenReturn(items);

    mockMvc
        .perform(get("/api/v1/items"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.*", hasSize(2)));
  }

  @Test
  void shouldGetItem() throws Exception {

    List<Item> items = getItems();
    when(mockItemService.getItem(1L)).thenReturn(items.stream().findFirst().get());

    mockMvc.perform(get("/api/v1/items/1"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("Item One"))
        .andExpect(jsonPath("$.description").value("This is a test item one."));
  }

  @Test
  void shouldCreateItem() throws Exception {

    when(mockItemService.createItem(any(ItemRequestBody.class))).thenReturn(
            1L
    );
    mockMvc
        .perform(
            post("/api/v1/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Item Six\", \"description\": \"This is a description of Item Six.\"}")
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated());
  }

  @Test
  void shouldUpdateItem() throws Exception {
    mockMvc
        .perform(
            put("/api/v1/items/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": 2, \"name\": \"Item Two\", \"description\": \"This is a updated description of Item Three.\"}")
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());
  }

  @Test
  void shouldDeleteItem() throws Exception {
    mockMvc.perform(delete("/api/v1/items/3")).andExpect(status().isNoContent());
  }

  private static @NonNull List<Item> getItems() {
    return List.of(Item.builder().id(1L).name("Item One").description("This is a test item one.").build(),
            Item.builder().id(2L).name("Item Two").description("This is a test item two.").build());
  }
}
