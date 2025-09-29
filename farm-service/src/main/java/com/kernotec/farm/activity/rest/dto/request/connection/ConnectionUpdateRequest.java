package com.kernotec.farm.activity.rest.dto.request.connection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.request.BaseRequest;
import com.kernotec.farm.activity.jpa.enums.ResponseRequestStateEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.ZonedDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ConnectionUpdateRequest extends BaseRequest {

    private ResponseRequestStateEnum responseRequestState;
    @JsonIgnore
    @Schema(hidden = true)
    private ZonedDateTime responseDate;
}
