package com.shinkai.api;

import com.shinkai.api.endpoint.ItemEndPoint;
import com.shinkai.api.models.CreateItemRequestDto;
import com.shinkai.api.models.SingleItemRequestDto;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static com.shinkai.helpers.CustomAllureListener.withCustomTemplates;
import static io.restassured.RestAssured.with;

public class ItemApi extends BaseApi {

    @Step("Sending request to create new item")
    public ValidatableResponse createItem(CreateItemRequestDto item) {
        return with()
                .filter(withCustomTemplates())
                .given().spec(defaultRequestSpec)
                .body(item)
                .when()
                .post(ItemEndPoint.CREATE)
                .then()
                .log().all();
    }

    @Step("Sending request to delete item")
    public ValidatableResponse deleteItem(SingleItemRequestDto itemId) {
        return with()
                .filter(withCustomTemplates())
                .given().spec(defaultRequestSpec)
                .body(itemId)
                .when()
                .post(ItemEndPoint.DELETE)
                .then()
                .log().all();
    }

    @Step("Sending get item request")
    public ValidatableResponse getSingleItem(SingleItemRequestDto itemId) {
        return with()
                .filter(withCustomTemplates())
                .given().spec(defaultRequestSpec)
                .body(itemId)
                .when()
                .post(ItemEndPoint.GET_INFO)
                .then()
                .log().all();
    }
}
