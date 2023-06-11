package com.shinkai.tests;

import com.shinkai.api.item.CreateItemRequestDto;
import com.shinkai.api.item.ItemApi;
import com.shinkai.api.item.ItemInfoResponseDto;
import com.shinkai.api.item.SingleItemRequestDto;
import com.shinkai.generators.ItemDataGenerator;
import io.qameta.allure.Step;

public class TestBase {
    ItemApi itemApi = new ItemApi();
    ItemDataGenerator data = new ItemDataGenerator();

    @Step("Performing item cleanup")
    public void cleanup(Integer itemId) {
        itemApi.deleteItem(SingleItemRequestDto.builder().
                id(itemId)
                .build());
    }

    @Step("Preparing new item for testing")
    public ItemInfoResponseDto createNewItem() {
        return itemApi.createItem(CreateItemRequestDto.builder()
                .name(data.getItemName())
                .section(data.getCategory())
                .description(data.getDescription())
                .build()
        );
    }
}
