package com.shinkai.tests;

import com.shinkai.api.item.CreateItemRequestDto;
import com.shinkai.api.item.ItemErrorFieldResponseDto;
import com.shinkai.api.item.ItemInfoResponseDto;
import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Create item")
public class CreateItemTests extends TestBase {

    @Test
    @DisplayName("Create item in Shop with all fields")
    @Description("Create item in Shop with all possible fields: name, section, description, color, size, price, params, photo")
    public void createItemWithAllFields() {
        String itemNameReq = data.getItemName();
        String sectionReq = data.getCategory();
        String descriptionReq = data.getDescription();
        String colorReq = data.getColor();
        String sizeReq = data.getSize();
        Double priceReq = data.getPrice();
        String paramsReq = data.getParams();
        String photoReq = data.getBase64FileString(data.getRandomPhoto());

        CreateItemRequestDto newItem = step("Preparing request with new item data", () ->
                CreateItemRequestDto.builder()
                        .name(itemNameReq)
                        .section(sectionReq)
                        .description(descriptionReq)
                        .color(colorReq)
                        .size(sizeReq)
                        .price(priceReq)
                        .params(paramsReq)
                        .photo(photoReq)
                        .build());

        ItemInfoResponseDto createItemResponse = itemApi.createItem(newItem);

        step("Checking that created item has id", () ->
                assertThat(Integer.parseInt(createItemResponse.getResult().getId())).isGreaterThan(0));

        step("Verify that the created item contains the fields sent in the request", () ->
                assertAll(
                        () -> assertEquals(itemNameReq, createItemResponse.getResult().getName()),
                        () -> assertEquals(sectionReq, createItemResponse.getResult().getSection()),
                        () -> assertEquals(descriptionReq, createItemResponse.getResult().getDescription()),
                        () -> assertEquals(colorReq, createItemResponse.getResult().getColor()),
                        () -> assertEquals(sizeReq, createItemResponse.getResult().getSize()),
                        () -> assertEquals(priceReq, createItemResponse.getResult().getPrice()),
                        () -> assertEquals(paramsReq, createItemResponse.getResult().getParams()),
                        () -> assertEquals(photoReq, createItemResponse.getResult().getPhoto())
                ));

        cleanup(Integer.parseInt(createItemResponse.getResult().getId()));
    }

    @Test
    @DisplayName("Create item in Shop with required fields")
    @Description("Create item in Shop with only required fields: name, section and description")
    public void createItemWithRequiredFields() {
        String itemNameReq = data.getItemName();
        String sectionReq = data.getCategory();
        String descriptionReq = data.getDescription();

        CreateItemRequestDto newItem = step("Preparing request with new item data", () ->
                CreateItemRequestDto.builder()
                        .name(itemNameReq)
                        .section(sectionReq)
                        .description(descriptionReq)
                        .build());

        ItemInfoResponseDto createItemResponse = itemApi.createItem(newItem);

        step("Checking that created item has id", () ->
                assertThat(Integer.parseInt(createItemResponse.getResult().getId())).isGreaterThan(0));
        step("Verify that the created item contains the fields sent in the request", () ->
                assertAll(
                        () -> assertEquals(itemNameReq, createItemResponse.getResult().getName()),
                        () -> assertEquals(sectionReq, createItemResponse.getResult().getSection()),
                        () -> assertEquals(descriptionReq, createItemResponse.getResult().getDescription())
                ));

        cleanup(Integer.parseInt(createItemResponse.getResult().getId()));
    }

    @CsvSource(value = {
            "name | Название товара не заполнено!",
            "section | Категория не найдена!",
            "description | Описание товара не заполнено!"
    }, delimiterString = "|")
    @ParameterizedTest(name = "Unsuccessful item creation: missing required parameter {0}")
    public void createItemWithoutRequiredParam(String parameter, String expectedMessage) {
        String itemNameReq = data.getItemName();
        String sectionReq = data.getCategory();
        String descriptionReq = data.getDescription();

        CreateItemRequestDto newItem = step("Preparing request with new item data", () -> CreateItemRequestDto.builder()
                .name(itemNameReq)
                .section(sectionReq)
                .description(descriptionReq)
                .build()
        );

        switch (parameter) {
            case "name":
                newItem.setName("");
                break;
            case "section":
                newItem.setSection("");
                break;
            case "description":
                newItem.setDescription("");
                break;
        }

        ItemErrorFieldResponseDto createItemWithError = itemApi.createItemWithErrorField(newItem);

        step("Verify field_error", () ->
                assertThat(createItemWithError.getErrorField()).isEqualTo(parameter));
        step("Verify error", () ->
                assertThat(createItemWithError.getError()).isIn(parameter + "_not_filled", parameter + "_not_found"));
        step("Verify message", () ->
                assertThat(createItemWithError.getMessage()).isEqualTo(expectedMessage));
    }

    @ValueSource(strings = {"big_photo.jpg", "photo_501.jpg"})
    @ParameterizedTest(name = "Create item with photo with width more than 500px")
    public void createItemWithTooBigPhoto(String photoName) {
        String itemNameReq = data.getItemName();
        String sectionReq = data.getCategory();
        String descriptionReq = data.getDescription();
        String photoReq = data.getBase64FileString(photoName);

        CreateItemRequestDto newItem = step("Preparing request with new item data", () -> CreateItemRequestDto.builder()
                .name(itemNameReq)
                .section(sectionReq)
                .description(descriptionReq)
                .photo(photoReq)
                .build());

        ItemErrorFieldResponseDto createItemWithBadPhoto = itemApi.createItemWithErrorField(newItem);

        step("Verify field_error", () ->
                assertThat(createItemWithBadPhoto.getErrorField()).isEqualTo("photo"));
        step("Verify error", () ->
                assertThat(createItemWithBadPhoto.getError()).isIn("photo_not_correct"));
        step("Verify message", () ->
                assertThat(createItemWithBadPhoto.getMessage()).isEqualTo("Ширина должна быть не более 500px"));
    }
}
