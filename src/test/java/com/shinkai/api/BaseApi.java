package com.shinkai.api;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

import static com.shinkai.helpers.CustomAllureListener.withCustomTemplates;
import static io.restassured.http.ContentType.JSON;

public class BaseApi {

    protected final static RequestSpecification defaultRequestSpec = new RequestSpecBuilder()
            .addFilter(withCustomTemplates())
            .setBaseUri("http://shop.bugred.ru/api")
            .setContentType(JSON)
            .build()
            .log().all();
}
