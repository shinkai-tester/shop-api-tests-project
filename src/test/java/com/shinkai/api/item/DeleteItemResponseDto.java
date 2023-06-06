package com.shinkai.api.item;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeleteItemResponseDto {
    private String method, status, result;
}
