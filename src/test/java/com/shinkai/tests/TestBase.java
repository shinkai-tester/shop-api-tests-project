package com.shinkai.tests;

import com.shinkai.api.ItemApi;
import com.shinkai.api.models.ItemInfoResponseDto;
import com.shinkai.api.models.SingleItemRequestDto;
import com.shinkai.generators.ItemFactory;
import io.qameta.allure.Step;

public class TestBase {
    ItemApi itemApi = new ItemApi();
    ItemFactory itemFactory = new ItemFactory();

    @Step("Performing item cleanup")
    public void cleanup(Integer itemId) {
        itemApi.deleteItem(SingleItemRequestDto.builder()
                .id(itemId)
                .build());
    }

    @Step("Preparing new item for testing")
    public ItemInfoResponseDto createNewItem() {
        return itemApi.createItem(itemFactory.getRandomItem()).extract().as(ItemInfoResponseDto.class);
    }
}
