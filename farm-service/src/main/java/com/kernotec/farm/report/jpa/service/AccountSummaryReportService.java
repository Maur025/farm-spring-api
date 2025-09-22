package com.kernotec.farm.report.jpa.service;

import com.kernotec.farm.account.command.account.AccountGetDtoCmd;
import com.kernotec.farm.account.jpa.dto.entity.AccountDto;
import com.kernotec.farm.report.rest.dto.request.ActivitySummaryByAccountRequest;
import com.kernotec.farm.report.rest.dto.response.account.ActivitySummaryResponse;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AccountSummaryReportService {

    private final EntityManager entityManager;
    private final AccountGetDtoCmd accountGetDtoCmd;
    private final FriendSummaryService friendSummaryService;
    private final GroupSummaryService groupSummaryService;
    private final PageSummaryService pageSummaryService;
    private final PublishingSummaryService publishingSummaryService;
    private final ReactionSummaryService reactionSummaryService;
    private final CommentSummaryService commentSummaryService;

    private CriteriaBuilder cb;

    @PostConstruct
    private void init() {
        this.cb = entityManager.getCriteriaBuilder();
    }

    public ActivitySummaryResponse getActivitySummaryByAccountId(UUID accountId,
        ActivitySummaryByAccountRequest filterRequest)
    {
        UUID socialNetworkId = filterRequest.getSocialNetworkId();

        AccountDto accountDto = accountGetDtoCmd.withRequest(AccountGetDtoCmd.Request.builder()
                .accountId(accountId)
                .build())
            .execute();

        return ActivitySummaryResponse.builder()
            .accountId(accountId)
            .accountName(accountDto.getUsername())
            .socialNetworkId(socialNetworkId)
            .socialNetworkName(accountDto.getSocialNetwork()
                .getName())
            .friendSummary(friendSummaryService.getFriendSummary(accountId, filterRequest))
            .groupSummary(groupSummaryService.getGroupSummary(accountId, filterRequest))
            .pageSummary(pageSummaryService.getPageSummary(accountId, filterRequest))
            .publishingSummary(
                publishingSummaryService.getPublishingSummary(accountId, filterRequest))
            .reactionSummary(reactionSummaryService.getReactionSummary(accountId, filterRequest))
            .commentSummary(commentSummaryService.getCommentSummary(accountId, filterRequest))
            .build();
    }


}
