package com.kernotec.farm.common.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonInclude(Include.NON_NULL)
public class MinimalResponse<T> {

    private Integer code;
    private T data;
    private String message;
}
