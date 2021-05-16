package com.example.web.controller;

import com.example.domain.Beer;
import com.example.repositories.BeerRepository;
import com.example.web.model.BeerDto;
import com.example.web.model.BeerStyleEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs
@WebMvcTest(BeerController.class)
@ComponentScan(basePackages = "com.example.web.mappers")
class BeerControllerTest {

  @Autowired MockMvc mockMvc;

  @Autowired ObjectMapper objectMapper;

  @MockBean BeerRepository beerRepository;

  @Test
  void testBeerById() throws Exception {
    BDDMockito.given(beerRepository.findById(any(UUID.class)))
        .willReturn(Optional.of(Beer.builder().build()));
    mockMvc
        .perform(
            get("/api/v1/beer/{beerId}", UUID.randomUUID())
                .param("isCold", "yes")
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(
            document(
                "/v1/beer",
                pathParameters(parameterWithName("beerId").description("UUID of Beer Object.")),
                requestParameters(parameterWithName("isCold").description("Request Query Param")),
                responseFields(
                    fieldWithPath("id").description("ID of Beer Object"),
                    fieldWithPath("beerName").description("Name of Beer Object"),
                    fieldWithPath("beerStyle").description("Style of Beer Object"),
                    fieldWithPath("version").description("Version of Beer Object"),
                    fieldWithPath("upc").description("UPC of Beer Object"),
                    fieldWithPath("price").description("Price of Beer Object"),
                    fieldWithPath("quantityOnHand").description("On Hand Quantity of Beer Object"),
                    fieldWithPath("createdDate").description("Created Date of Beer Object"),
                    fieldWithPath("lastModifiedDate")
                        .description("Last Modified Date of Beer Object"))));
  }

  @Test
  void testSaveBeer() throws Exception {
    BDDMockito.given(beerRepository.save(any(Beer.class)))
        .willReturn(Beer.builder().id(UUID.randomUUID()).build());
    BeerDto beerDto = getValidBeerDto();
    String beerDtoJson = objectMapper.writeValueAsString(beerDto);

    mockMvc
        .perform(post("/api/v1/beer/").contentType(MediaType.APPLICATION_JSON).content(beerDtoJson))
        .andExpect(status().isCreated())
        .andDo(
            document(
                "v1/beer",
                requestFields(
                    fieldWithPath("id").ignored(),
                    fieldWithPath("beerName").description("Name of Beer Object"),
                    fieldWithPath("beerStyle").description("Style of Beer Object"),
                    fieldWithPath("version").ignored(),
                    fieldWithPath("upc").description("UPC of Beer Object"),
                    fieldWithPath("price").description("Price of Beer Object"),
                    fieldWithPath("quantityOnHand").description("On Hand Quantity of Beer Object"),
                    fieldWithPath("createdDate").ignored(),
                    fieldWithPath("lastModifiedDate").ignored())));
  }

  @Test
  void testUpdateBeer() throws Exception {
    BDDMockito.given(beerRepository.findById(any(UUID.class)))
        .willReturn(Optional.of(Beer.builder().build()));
    BDDMockito.given(beerRepository.save(any(Beer.class))).willReturn(Beer.builder().build());
    BeerDto beerDto = getValidBeerDto();
    String beerDtoJson = objectMapper.writeValueAsString(beerDto);

    mockMvc
        .perform(
            put("/api/v1/beer/" + UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(beerDtoJson))
        .andExpect(status().isOk());
  }

  private BeerDto getValidBeerDto() {
    return BeerDto.builder()
        .beerName("Nice Ale")
        .beerStyle(BeerStyleEnum.ALE)
        .price(new BigDecimal("29.99"))
        .upc(123123123123L)
        .quantityOnHand(5)
        .build();
  }
}
