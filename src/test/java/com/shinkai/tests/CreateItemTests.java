package com.shinkai.tests;

import com.shinkai.api.endpoint.ItemEndPoint;
import com.shinkai.api.models.CreateItemRequestDto;
import com.shinkai.api.models.ItemErrorFieldResponseDto;
import com.shinkai.api.models.ItemInfoResponseDto;
import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static com.shinkai.generators.ItemFactory.getBase64FileString;
import static io.qameta.allure.Allure.step;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Create item")
public class CreateItemTests extends TestBase {
    CreateItemRequestDto newItem = itemFactory.getRandomItem();

    @Test
    @DisplayName("Create item in Shop with all fields")
    @Description("Create item in Shop with all possible fields: name, section, description, color, size, price, params, photo")
    public void createItemWithAllFields() {
        ItemInfoResponseDto createItemResponse = itemApi.createItem(newItem)
                .body(matchesJsonSchemaInClasspath("schemas/itemInfoSchema.json"))
                .body("method", equalTo(ItemEndPoint.CREATE))
                .body("status", equalTo("ok"))
                .extract().as(ItemInfoResponseDto.class);

        step("Checking that created item has id", () ->
                assertThat(Integer.parseInt(createItemResponse.getResult().getId())).isGreaterThan(0));

        step("Verify that the created item contains the fields sent in the request", () ->
                assertAll(
                        () -> assertEquals(newItem.getName(), createItemResponse.getResult().getName()),
                        () -> assertEquals(newItem.getSection(), createItemResponse.getResult().getSection()),
                        () -> assertEquals(newItem.getDescription(), createItemResponse.getResult().getDescription()),
                        () -> assertEquals(newItem.getColor(), createItemResponse.getResult().getColor()),
                        () -> assertEquals(newItem.getSize(), createItemResponse.getResult().getSize()),
                        () -> assertEquals(newItem.getPrice(), createItemResponse.getResult().getPrice()),
                        () -> assertEquals(newItem.getParams(), createItemResponse.getResult().getParams()),
                        () -> assertEquals(newItem.getPhoto(), createItemResponse.getResult().getPhoto())
                ));

        cleanup(Integer.parseInt(createItemResponse.getResult().getId()));
    }

    @Test
    @DisplayName("Create item in Shop with required fields")
    @Description("Create item in Shop with only required fields: name, section and description")
    public void createItemWithRequiredFields() {
        newItem.setPhoto(null);
        newItem.setParams(null);
        newItem.setColor(null);
        newItem.setSize(null);
        newItem.setPrice(null);

        ItemInfoResponseDto createItemResponse = itemApi.createItem(newItem).extract().as(ItemInfoResponseDto.class);

        step("Checking that created item has id", () ->
                assertThat(Integer.parseInt(createItemResponse.getResult().getId())).isGreaterThan(0));
        step("Verify that the created item contains the fields sent in the request", () ->
                assertAll(
                        () -> assertEquals(newItem.getName(), createItemResponse.getResult().getName()),
                        () -> assertEquals(newItem.getSection(), createItemResponse.getResult().getSection()),
                        () -> assertEquals(newItem.getDescription(), createItemResponse.getResult().getDescription())
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

        ItemErrorFieldResponseDto createItemWithError = itemApi.createItem(newItem)
                .body(matchesJsonSchemaInClasspath("schemas/itemErrorSchema.json"))
                .body("method", equalTo(ItemEndPoint.CREATE + "/"))
                .body("status", equalTo("error"))
                .extract().as(ItemErrorFieldResponseDto.class);

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
        newItem.setPhoto(getBase64FileString(photoName));

        ItemErrorFieldResponseDto createItemWithBadPhoto = itemApi.createItem(newItem).extract().as(ItemErrorFieldResponseDto.class);

        step("Verify field_error", () ->
                assertThat(createItemWithBadPhoto.getErrorField()).isEqualTo("photo"));
        step("Verify error", () ->
                assertThat(createItemWithBadPhoto.getError()).isIn("photo_not_correct"));
        step("Verify message", () ->
                assertThat(createItemWithBadPhoto.getMessage()).isEqualTo("Ширина должна быть не более 500px"));
    }
}
