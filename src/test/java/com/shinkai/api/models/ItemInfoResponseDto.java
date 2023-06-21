package com.shinkai.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemInfoResponseDto {
    private String method,
            status;

    @JsonProperty("result")
    private ItemResultDto result;
}
