package com.kernotec.farm.report.jpa.service;

import com.kernotec.farm.account.command.account.AccountGetDtoCmd;
import com.kernotec.farm.account.jpa.dto.entity.AccountDto;
import com.kernotec.farm.account.jpa.entity.Account;
import com.kernotec.farm.account.jpa.entity.AccountFollowProfile;
import com.kernotec.farm.account.jpa.entity.AccountGroup;
import com.kernotec.farm.account.jpa.enums.AccountFollowProfileStateEnum;
import com.kernotec.farm.activity.jpa.entity.Activity;
import com.kernotec.farm.activity.jpa.entity.Comment;
import com.kernotec.farm.activity.jpa.entity.Group;
import com.kernotec.farm.activity.jpa.entity.Profile;
import com.kernotec.farm.activity.jpa.entity.Publishing;
import com.kernotec.farm.activity.jpa.entity.Reaction;
import com.kernotec.farm.parametric.jpa.entity.GroupState;
import com.kernotec.farm.parametric.jpa.entity.PublishingContext;
import com.kernotec.farm.parametric.jpa.entity.PublishingType;
import com.kernotec.farm.parametric.jpa.entity.ReactionType;
import com.kernotec.farm.parametric.jpa.entity.Region;
import com.kernotec.farm.parametric.jpa.enums.GroupStateCodeEnum;
import com.kernotec.farm.report.rest.dto.request.ActivitySummaryByAccountRequest;
import com.kernotec.farm.report.rest.dto.response.account.AccountSummaryTableResponse;
import com.kernotec.farm.report.rest.dto.response.account.ActivitySummaryResponse;
import com.kernotec.farm.report.rest.dto.response.account.CommentSummaryResponse;
import com.kernotec.farm.report.rest.dto.response.account.GroupRegionSummaryResponse;
import com.kernotec.farm.report.rest.dto.response.account.GroupSummaryResponse;
import com.kernotec.farm.report.rest.dto.response.account.PageRegionSummaryResponse;
import com.kernotec.farm.report.rest.dto.response.account.PageSummaryResponse;
import com.kernotec.farm.report.rest.dto.response.account.PublishingContextSummaryResponse;
import com.kernotec.farm.report.rest.dto.response.account.PublishingSummaryResponse;
import com.kernotec.farm.report.rest.dto.response.account.ReactionSummaryResponse;
import com.kernotec.farm.report.rest.dto.response.account.ReactionTypeSummaryResponse;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AccountSummaryReportService {

    private final EntityManager entityManager;
    private final AccountGetDtoCmd accountGetDtoCmd;
    private final FriendSummaryService friendSummaryService;
    private final CommonPredicateToSummary commonPredicateToSummary;
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

        CommentSummaryResponse commentSummary = getCommentSummary(accountId, socialNetworkId);

        return ActivitySummaryResponse.builder()
            .accountId(accountId)
            .accountName(accountDto.getUsername())
            .socialNetworkId(socialNetworkId)
            .socialNetworkName(accountDto.getSocialNetwork()
                .getName())
            .friendSummary(friendSummaryService.getFriendSummary(accountId, filterRequest))
            .groupSummary(GroupSummaryResponse.builder()
                .totalGroups(getTotalGroups(accountId, socialNetworkId))
                .totalsByRegion(getTotalGroupsByRegion(accountId, socialNetworkId))
                .groups(getGroupsOfResult(accountId, socialNetworkId))
                .build())
            .pageSummary(PageSummaryResponse.builder()
                .totalPages(getTotalPages(accountId, socialNetworkId))
                .totalsByRegion(getTotalPagesByRegion(accountId, socialNetworkId))
                .profiles(getPagesOfResult(accountId, socialNetworkId))
                .build())
            .publishingSummary(PublishingSummaryResponse.builder()
                .totalPublications(getTotalPublications(accountId, socialNetworkId))
                .totalsByContext(getTotalPublicationsByContext(accountId, socialNetworkId))
                .publications(getPublicationsOfResult(accountId, socialNetworkId))
                .build())
            .reactionSummary(ReactionSummaryResponse.builder()
                .totalReactions(getTotalReactions(accountId, socialNetworkId))
                .totalsByType(getTotalReactionsByType(accountId, socialNetworkId))
                .build())
            .commentSummary(
                commentSummary.withComments(getCommentsOfResult(accountId, socialNetworkId)))
            .build();
    }

    private Long getTotalGroups(UUID accountId, UUID socialNetworkId) {
        CriteriaQuery<Long> queryOfTotalGroups = cb.createQuery(Long.class);
        Root<Account> accountRoot = queryOfTotalGroups.from(Account.class);

        Join<Account, AccountGroup> accountGroupJoin = accountRoot.join(
            "accountGroups", JoinType.INNER);
        Join<AccountGroup, GroupState> groupStateJoin = accountGroupJoin.join(
            "groupState", JoinType.INNER);

        queryOfTotalGroups.select(cb.countDistinct(accountGroupJoin.get("id")));

        queryOfTotalGroups.where(cb.and(
            getPredicatesOfGroups(accountRoot, accountId, socialNetworkId, groupStateJoin).toArray(
                Predicate[]::new)));

        return entityManager.createQuery(queryOfTotalGroups)
            .getSingleResult();
    }

    private List<AccountSummaryTableResponse> getGroupsOfResult(UUID accountId,
        UUID socialNetworkId)
    {
        CriteriaQuery<AccountSummaryTableResponse> queryGroupsOfResult = cb.createQuery(
            AccountSummaryTableResponse.class);
        Root<Account> accountRoot = queryGroupsOfResult.from(Account.class);

        Join<Account, AccountGroup> accountGroupJoin = accountRoot.join(
            "accountGroups", JoinType.INNER);
        Join<AccountGroup, Group> groupJoin = accountGroupJoin.join("group", JoinType.LEFT);
        Join<Group, Region> regionJoin = groupJoin.join("region", JoinType.LEFT);
        Join<AccountGroup, GroupState> groupStateJoin = accountGroupJoin.join(
            "groupState", JoinType.INNER);

        queryGroupsOfResult.select(cb.construct(
            AccountSummaryTableResponse.class,
            /* id -> groupId */
            groupJoin.get("id"),
            /* name -> groupName */
            groupJoin.get("name"),
            /* detail -> regionName */
            regionJoin.get("name")
        ));

        queryGroupsOfResult.where(cb.and(
            getPredicatesOfGroups(accountRoot, accountId, socialNetworkId, groupStateJoin).toArray(
                Predicate[]::new)));

        return entityManager.createQuery(queryGroupsOfResult)
            .getResultList();
    }

    private List<GroupRegionSummaryResponse> getTotalGroupsByRegion(UUID accountId,
        UUID socialNetworkId)
    {
        CriteriaQuery<GroupRegionSummaryResponse> queryGroupsByRegion = cb.createQuery(
            GroupRegionSummaryResponse.class);
        Root<Account> accountRoot = queryGroupsByRegion.from(Account.class);

        Join<Account, AccountGroup> accountGroupJoin = accountRoot.join(
            "accountGroups", JoinType.INNER);

        Join<AccountGroup, Group> groupJoin = accountGroupJoin.join("group", JoinType.INNER);
        Join<Group, Region> regionJoin = groupJoin.join("region", JoinType.INNER);

        Join<AccountGroup, GroupState> groupStateJoin = accountGroupJoin.join(
            "groupState", JoinType.INNER);

        queryGroupsByRegion.select(cb.construct(
            GroupRegionSummaryResponse.class,
            /* regionName */
            regionJoin.get("name"),
            /* regionId */
            regionJoin.get("id"),
            /* totalGroupsByRegion */
            cb.countDistinct(accountGroupJoin)
        ));

        queryGroupsByRegion.where(cb.and(
            getPredicatesOfGroups(accountRoot, accountId, socialNetworkId, groupStateJoin).toArray(
                Predicate[]::new)));

        queryGroupsByRegion.groupBy(regionJoin.get("id"), regionJoin.get("name"));

        return entityManager.createQuery(queryGroupsByRegion)
            .getResultList();
    }

    private List<Predicate> getPredicatesOfGroups(Root<Account> accountRoot, UUID accountId,
        UUID socialNetworkId, Join<?, ?> groupStateJoin)
    {
        List<Predicate> predicateList = commonPredicateToSummary.getCommonAccountPredicates(
            accountRoot, accountId, socialNetworkId);

        if (groupStateJoin != null) {
            predicateList.add(
                cb.equal(groupStateJoin.get("code"), GroupStateCodeEnum.JOINED.toString()));
        }

        return predicateList;
    }

    private Long getTotalPages(UUID accountId, UUID socialNetworkId) {
        CriteriaQuery<Long> queryOfTotalPages = cb.createQuery(Long.class);
        Root<Account> accountRoot = queryOfTotalPages.from(Account.class);

        Join<Account, AccountFollowProfile> accountFollowProfileJoin = accountRoot.join(
            "accountFollowProfiles", JoinType.INNER);

        queryOfTotalPages.select(cb.countDistinct(accountFollowProfileJoin.get("id")));

        queryOfTotalPages.where(cb.and(getPredicatesOfPages(
            accountRoot, accountId, socialNetworkId,
            accountFollowProfileJoin
        ).toArray(Predicate[]::new)));

        return entityManager.createQuery(queryOfTotalPages)
            .getSingleResult();
    }

    private List<AccountSummaryTableResponse> getPagesOfResult(UUID accountId, UUID socialNetworkId)
    {
        CriteriaQuery<AccountSummaryTableResponse> getPagesQuery = cb.createQuery(
            AccountSummaryTableResponse.class);
        Root<Account> accountRoot = getPagesQuery.from(Account.class);

        Join<Account, AccountFollowProfile> accountFollowProfileJoin = accountRoot.join(
            "accountFollowProfiles", JoinType.INNER);
        Join<AccountFollowProfile, Profile> profileJoin = accountFollowProfileJoin.join(
            "profile", JoinType.INNER);
        Join<Profile, Region> regionJoin = profileJoin.join("region", JoinType.LEFT);

        getPagesQuery.select(cb.construct(
            AccountSummaryTableResponse.class,
            /* profileId */
            profileJoin.get("id"),
            /* profileName */
            profileJoin.get("name"),
            /* regionName */
            regionJoin.get("name")
        ));

        getPagesQuery.where(cb.and(getPredicatesOfPages(
            accountRoot, accountId, socialNetworkId,
            accountFollowProfileJoin
        ).toArray(Predicate[]::new)));

        return entityManager.createQuery(getPagesQuery)
            .getResultList();
    }

    private List<PageRegionSummaryResponse> getTotalPagesByRegion(UUID accountId,
        UUID socialNetworkId)
    {
        CriteriaQuery<PageRegionSummaryResponse> queryPagesByRegion = cb.createQuery(
            PageRegionSummaryResponse.class);
        Root<Account> accountRoot = queryPagesByRegion.from(Account.class);

        Join<Account, AccountFollowProfile> accountFollowProfileJoin = accountRoot.join(
            "accountFollowProfiles", JoinType.INNER);
        Join<AccountFollowProfile, Profile> profileJoin = accountFollowProfileJoin.join(
            "profile", JoinType.INNER);
        Join<Profile, Region> regionJoin = profileJoin.join("region", JoinType.INNER);

        queryPagesByRegion.select(cb.construct(
            PageRegionSummaryResponse.class,
            /* regionName */
            regionJoin.get("name"),
            /* regionId */
            regionJoin.get("id"),
            /* totalPagesByRegion */
            cb.countDistinct(accountFollowProfileJoin.get("id"))
        ));

        queryPagesByRegion.where(cb.and(
            getPredicatesOfPages(
                accountRoot, accountId, socialNetworkId, accountFollowProfileJoin).toArray(
                Predicate[]::new)));

        queryPagesByRegion.groupBy(regionJoin.get("id"), regionJoin.get("name"));

        return entityManager.createQuery(queryPagesByRegion)
            .getResultList();
    }

    private List<Predicate> getPredicatesOfPages(Root<Account> accountRoot, UUID accountId,
        UUID socialNetworkId, Join<?, ?> accountFollowProfileJoin)
    {
        List<Predicate> predicateList = commonPredicateToSummary.getCommonAccountPredicates(
            accountRoot, accountId, socialNetworkId);

        cb.equal(
            accountFollowProfileJoin.get("followState"), AccountFollowProfileStateEnum.FOLLOWING);

        return predicateList;
    }

    private Long getTotalPublications(UUID accountId, UUID socialNetworkId) {
        CriteriaQuery<Long> queryOfTotalPublications = cb.createQuery(Long.class);
        Root<Activity> activityRoot = queryOfTotalPublications.from(Activity.class);

        Join<Activity, Publishing> publishingJoin = activityRoot.join(
            "publishings", JoinType.INNER);

        queryOfTotalPublications.select(cb.countDistinct(publishingJoin.get("id")));

        queryOfTotalPublications.where(
            getCommonActivityPredicates(activityRoot, accountId, socialNetworkId).toArray(
                Predicate[]::new));

        return entityManager.createQuery(queryOfTotalPublications)
            .getSingleResult();
    }

    private List<AccountSummaryTableResponse> getPublicationsOfResult(UUID accountId,
        UUID socialNetworkId)
    {
        CriteriaQuery<AccountSummaryTableResponse> queryPublicationsOfResult = cb.createQuery(
            AccountSummaryTableResponse.class);
        Root<Activity> activityRoot = queryPublicationsOfResult.from(Activity.class);

        Join<Activity, Publishing> publishingJoin = activityRoot.join(
            "publishings", JoinType.INNER);
        Join<Publishing, PublishingType> publishingTypeJoin = publishingJoin.join(
            "publishingType", JoinType.LEFT);
        Join<Publishing, PublishingContext> publishingContextJoin = publishingJoin.join(
            "publishingContext", JoinType.LEFT);

        queryPublicationsOfResult.select(cb.construct(
            AccountSummaryTableResponse.class,
            /* id -> publishingId */
            publishingJoin.get("id"),
            /* name -> publishinTypeName */
            publishingTypeJoin.get("name"),
            /* detail -> publishinContextName */
            publishingContextJoin.get("name")
        ));

        queryPublicationsOfResult.where(cb.and(
            getCommonActivityPredicates(activityRoot, accountId, socialNetworkId).toArray(
                Predicate[]::new)));

        return entityManager.createQuery(queryPublicationsOfResult)
            .getResultList();
    }

    private List<PublishingContextSummaryResponse> getTotalPublicationsByContext(UUID accountId,
        UUID socialNetworkId)
    {
        CriteriaQuery<PublishingContextSummaryResponse> queryPublicationsByContext = cb.createQuery(
            PublishingContextSummaryResponse.class);
        Root<Activity> activityRoot = queryPublicationsByContext.from(Activity.class);

        Join<Activity, Publishing> publishingJoin = activityRoot.join(
            "publishings", JoinType.INNER);
        Join<Publishing, PublishingContext> publishingContextJoin = publishingJoin.join(
            "publishingContext", JoinType.INNER);

        queryPublicationsByContext.select(cb.construct(
            PublishingContextSummaryResponse.class,
            /* publishingContextId */
            publishingContextJoin.get("id"),
            /* publishingContextName */
            publishingContextJoin.get("name"),
            /* totalPublicationsByContext */
            cb.countDistinct(publishingJoin.get("id"))
        ));

        queryPublicationsByContext.where(cb.and(
            getCommonActivityPredicates(activityRoot, accountId, socialNetworkId).toArray(
                Predicate[]::new)));

        queryPublicationsByContext.groupBy(
            publishingContextJoin.get("id"), publishingContextJoin.get("name"));

        return entityManager.createQuery(queryPublicationsByContext)
            .getResultList();
    }

    private Long getTotalReactions(UUID accountId, UUID socialNetworkId) {
        CriteriaQuery<Long> queryOfTotalReactions = cb.createQuery(Long.class);
        Root<Activity> activityRoot = queryOfTotalReactions.from(Activity.class);

        Join<Activity, Reaction> reactionJoin = activityRoot.join("reactions", JoinType.INNER);

        queryOfTotalReactions.select(cb.countDistinct(reactionJoin.get("id")));

        queryOfTotalReactions.where(
            getCommonActivityPredicates(activityRoot, accountId, socialNetworkId).toArray(
                Predicate[]::new));

        return entityManager.createQuery(queryOfTotalReactions)
            .getSingleResult();
    }

    private List<ReactionTypeSummaryResponse> getTotalReactionsByType(UUID accountId,
        UUID socialNetworkId)
    {
        CriteriaQuery<ReactionTypeSummaryResponse> queryReactionsByType = cb.createQuery(
            ReactionTypeSummaryResponse.class);
        Root<Activity> activityRoot = queryReactionsByType.from(Activity.class);

        Join<Activity, Reaction> reactionJoin = activityRoot.join("reactions", JoinType.INNER);
        Join<Reaction, ReactionType> reactionTypeJoin = reactionJoin.join(
            "reactionType", JoinType.INNER);

        queryReactionsByType.select(cb.construct(
            ReactionTypeSummaryResponse.class,
            /* reactionTypeId */
            reactionTypeJoin.get("id"),
            /* reactionTypeName */
            reactionTypeJoin.get("name"),
            /* reactionTypeCode */
            reactionTypeJoin.get("code"),
            /* totalReactionsByType */
            cb.countDistinct(reactionJoin.get("id"))
        ));

        queryReactionsByType.where(cb.and(
            getCommonActivityPredicates(activityRoot, accountId, socialNetworkId).toArray(
                Predicate[]::new)));

        queryReactionsByType.groupBy(
            reactionTypeJoin.get("id"), reactionTypeJoin.get("name"), reactionTypeJoin.get("code"));

        return entityManager.createQuery(queryReactionsByType)
            .getResultList();
    }

    private CommentSummaryResponse getCommentSummary(UUID accountId, UUID socialNetworkId) {
        CriteriaQuery<CommentSummaryResponse> queryCommentSummary = cb.createQuery(
            CommentSummaryResponse.class);
        Root<Activity> activityRoot = queryCommentSummary.from(Activity.class);

        Join<Activity, Comment> commentJoin = activityRoot.join("comments", JoinType.INNER);

        queryCommentSummary.select(cb.construct(
            CommentSummaryResponse.class,
            /* totalComments */
            cb.countDistinct(commentJoin.get("id")),
            /* totalPositiveComments */
            cb.coalesce(
                cb.sum(cb.<Long>selectCase()
                    .when(cb.isTrue(commentJoin.get("isAgreeComment")), 1L)
                    .otherwise(0L)), 0L
            ),
            /* totalNegativeComments */
            cb.coalesce(
                cb.sum(cb.<Long>selectCase()
                    .when(cb.isFalse(commentJoin.get("isAgreeComment")), 1L)
                    .otherwise(0L)), 0L
            )
        ));

        queryCommentSummary.where(cb.and(
            getCommonActivityPredicates(activityRoot, accountId, socialNetworkId).toArray(
                Predicate[]::new)));

        return entityManager.createQuery(queryCommentSummary)
            .getSingleResult();
    }

    private List<AccountSummaryTableResponse> getCommentsOfResult(UUID accountId,
        UUID socialNetworkId)
    {
        CriteriaQuery<AccountSummaryTableResponse> queryCommentsOfResult = cb.createQuery(
            AccountSummaryTableResponse.class);
        Root<Activity> activityRoot = queryCommentsOfResult.from(Activity.class);

        Join<Activity, Comment> commentJoin = activityRoot.join("comments", JoinType.INNER);
        Join<Comment, PublishingContext> publishingContextJoin = commentJoin.join(
            "publishingContext", JoinType.LEFT);

        queryCommentsOfResult.select(cb.construct(
            AccountSummaryTableResponse.class,
            /* id -> commentId */
            commentJoin.get("id"),
            /* name -> publishingContextName */
            publishingContextJoin.get("name"),
            /* detail -> isAgreeComment */
            cb.selectCase()
                .when(cb.isTrue(commentJoin.get("isAgreeComment")), "Positivo")
                .otherwise("Negativo")
        ));

        queryCommentsOfResult.where(cb.and(
            getCommonActivityPredicates(activityRoot, accountId, socialNetworkId).toArray(
                Predicate[]::new)));

        return entityManager.createQuery(queryCommentsOfResult)
            .getResultList();
    }

    private List<Predicate> getCommonActivityPredicates(Root<Activity> activityRoot, UUID accountId,
        UUID socialNetworkId)
    {
        Join<Activity, Account> accountJoin = activityRoot.join("account");

        List<Predicate> predicateList = new ArrayList<>();
        predicateList.add(cb.equal(activityRoot.get("accountId"), accountId));
        predicateList.add(cb.equal(accountJoin.get("socialNetworkId"), socialNetworkId));

        return predicateList;
    }
}
