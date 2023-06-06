package com.shinkai.api.item;

import com.shinkai.api.BaseApi;
import com.shinkai.api.endpoint.ItemEndPoint;
import io.qameta.allure.Step;

import static com.shinkai.helpers.CustomAllureListener.withCustomTemplates;
import static io.restassured.RestAssured.with;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;

public class ItemApi extends BaseApi {

    @Step("Sending request to create new item")
    public ItemInfoResponseDto createItem(CreateItemRequestDto item) {
        return with()
                .filter(withCustomTemplates())
                .given().spec(defaultRequestSpec)
                .body(item)
                .when()
                .post(ItemEndPoint.CREATE)
                .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("schemas/itemInfoSchema.json"))
                .body("method", equalTo(ItemEndPoint.CREATE))
                .body("status", equalTo("ok"))
                .log().all()
                .extract().as(ItemInfoResponseDto.class);
    }

    @Step("Sending create item request which leads to error response")
    public ItemErrorFieldResponseDto createItemWithErrorField(CreateItemRequestDto item) {
        return with()
                .filter(withCustomTemplates())
                .given().spec(defaultRequestSpec)
                .body(item)
                .when()
                .post(ItemEndPoint.CREATE)
                .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("schemas/itemErrorSchema.json"))
                .body("method", equalTo(ItemEndPoint.CREATE + "/"))
                .body("status", equalTo("error"))
                .log().all()
                .extract().as(ItemErrorFieldResponseDto.class);
    }

    @Step("Deleting existing item")
    public DeleteItemResponseDto deleteItem(SingleItemRequestDto itemId) {
        return with()
                .filter(withCustomTemplates())
                .given().spec(defaultRequestSpec)
                .body(itemId)
                .when()
                .post(ItemEndPoint.DELETE)
                .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("schemas/deleteItemSchema.json"))
                .body("method", equalTo(ItemEndPoint.DELETE))
                .body("status", equalTo("ok"))
                .log().all()
                .extract().as(DeleteItemResponseDto.class);
    }

    @Step("Sending delete item request which leads to error response")
    public ItemErrorFieldResponseDto deleteItemWithErrorField(SingleItemRequestDto itemId) {
        return with()
                .filter(withCustomTemplates())
                .given().spec(defaultRequestSpec)
                .body(itemId)
                .when()
                .post(ItemEndPoint.DELETE)
                .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("schemas/itemErrorSchema.json"))
                .body("method", equalTo(ItemEndPoint.DELETE + "/"))
                .body("status", equalTo("error"))
                .log().all()
                .extract().as(ItemErrorFieldResponseDto.class);
    }

    @Step("Sending get unknown item request")
    public ItemErrorFieldResponseDto getUnknownSingleItemInfo(SingleItemRequestDto itemId) {
        return with()
                .filter(withCustomTemplates())
                .given().spec(defaultRequestSpec)
                .body(itemId)
                .when()
                .post(ItemEndPoint.GET_INFO)
                .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("schemas/itemErrorSchema.json"))
                .body("method", equalTo(ItemEndPoint.GET_INFO + "/"))
                .body("status", equalTo("error"))
                .log().all()
                .extract().as(ItemErrorFieldResponseDto.class);
    }
}
