package com.shinkai.api.item;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemErrorFieldResponseDto {
    private String method, status, error, message;

    @JsonProperty("field_error")
    private String errorField;
}
