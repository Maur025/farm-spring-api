package com.kernotec.farm.report.rest.dto.response.activity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.response.data.EntityResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@AllArgsConstructor
public class ActivityTypeTotalResponse extends EntityResponse {

    private Long totalActivities;
    private Long totalComments;
    private Long totalGroups;
    private Long totalFriends;
    private Long totalPageFollows;
    private Long totalReactions;
    private Long totalPublications;
    private Long totalTiktokProfileFollows;
}
