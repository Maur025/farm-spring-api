package com.kernotec.farm.activity.rest.dto.response.profile;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.response.data.EntityResponse;
import com.kernotec.farm.parametric.rest.dto.response.publishing.context.PublishingContextResponse;
import com.kernotec.farm.parametric.rest.dto.response.region.RegionResponse;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class ProfileResponse extends EntityResponse {

    private String name;

    private UUID regionId;
    private RegionResponse region;

    private UUID publishingContextId;
    private PublishingContextResponse publishingContext;
}
