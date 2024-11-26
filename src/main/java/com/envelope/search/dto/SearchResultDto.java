package com.envelope.search.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(toBuilder = true)
public class SearchResultDto {
    
    private Long id;

    private String name;

    private Float avgPrice;

    private Float rate;

}
