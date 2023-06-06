package com.shinkai.api.item;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class CreateItemRequestDto {
    @JsonProperty(required = true)
    private String name;

    @JsonProperty(required = true)
    private String section;

    @JsonProperty(required = true)
    private String description;

    private String color;

    private String params;

    private String photo;

    private String size;

    private Double price;
}

