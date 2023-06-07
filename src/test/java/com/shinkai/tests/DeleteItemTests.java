package com.shinkai.tests;

import com.shinkai.api.item.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Delete item")
public class DeleteItemTests extends TestBase {

    @Test
    @DisplayName("Delete item in Shop")
    public void deleteItem() {
        ItemInfoResponseDto createItemResponse = createNewItem();

        SingleItemRequestDto requestBody = SingleItemRequestDto.builder()
                .id(Integer.parseInt(createItemResponse.getResult().getId())).build();

        DeleteItemResponseDto deleteItemResponse = itemApi.deleteItem(requestBody);

        step("Verify response contains ID of deleted item", () ->
                assertThat(deleteItemResponse.getResult()).contains("ID " + createItemResponse.getResult().getId()));

        ItemErrorFieldResponseDto getDeletedItem = itemApi.getUnknownSingleItemInfo(requestBody);
        step("Verify get item response message that deleted item is not found", () ->
                assertThat(getDeletedItem.getError()).isEqualTo("item_with_id_not_found"));
    }


    static Stream<Arguments> badIdsAndErrors() {
        return Stream.of(
                Arguments.of(0, List.of("id_not_filled", "Поле ID товара  не заполнено")),
                Arguments.of(123456789, List.of("item_with_id_not_found", "Товар с ID 123456789 не найден!"))
        );
    }

    @MethodSource("badIdsAndErrors")
    @ParameterizedTest(name = "Delete item with bad id={0}")
    public void deleteItemWithBadId(Integer itemId, List<String> errors) {
        SingleItemRequestDto requestBody = SingleItemRequestDto.builder()
                .id(itemId).build();

        ItemErrorFieldResponseDto deleteItemResponse = itemApi.deleteItemWithErrorField(requestBody);

        step("Verify field_error=id", () ->
                assertThat(deleteItemResponse.getErrorField()).isEqualTo("id"));
        step("Verify error text: " + errors.get(0), () ->
                assertThat(deleteItemResponse.getError()).isEqualTo(errors.get(0)));
        step("Verify message text: " + errors.get(1), () ->
                assertThat(deleteItemResponse.getMessage()).isEqualTo(errors.get(1)));
    }
}
