package com.shinkai.api.item;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemResultDto {

    @JsonProperty(required = true)
    private String id;

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