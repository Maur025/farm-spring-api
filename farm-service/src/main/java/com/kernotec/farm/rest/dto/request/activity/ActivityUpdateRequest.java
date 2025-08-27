package com.kernotec.farm.rest.dto.request.activity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.request.BaseRequest;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class ActivityUpdateRequest extends BaseRequest {

    private String link;
    private LocalDateTime activityDate;
    private UUID accountId;
    private UUID activityTypeId;
}
