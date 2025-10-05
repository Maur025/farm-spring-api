package com.kernotec.farm.report.jpa.service;

import com.kernotec.farm.account.jpa.entity.Account;
import com.kernotec.farm.account.jpa.enums.AccountTypeEnum;
import com.kernotec.farm.account.jpa.specification.account.AccountSpecification;
import com.kernotec.farm.activity.jpa.entity.Activity;
import com.kernotec.farm.activity.jpa.repository.ActivityRepository;
import com.kernotec.farm.activity.jpa.specification.activity.ActivitySpecification;
import com.kernotec.farm.inventory.jpa.entity.Device;
import com.kernotec.farm.inventory.jpa.entity.Farm;
import com.kernotec.farm.inventory.jpa.specification.device.DeviceSpecification;
import com.kernotec.farm.parametric.jpa.entity.ActivityType;
import com.kernotec.farm.parametric.jpa.enums.ActivityTypeCodeEnum;
import com.kernotec.farm.report.rest.dto.request.ReportActivityRequest;
import com.kernotec.farm.report.rest.dto.request.ReportRatingRequest;
import com.kernotec.farm.report.rest.dto.response.activity.ActivityTypeTotalResponse;
import com.kernotec.farm.report.rest.dto.response.rating.ReportRatingResponse;
import com.kernotec.farm.util.CommonSpecification;
import com.kernotec.farm.util.ExcelUtil;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReportActivityService {

    private final ActivityRepository repository;
    private final EntityManager entityManager;
    private final ExcelUtil excelUtil;
    private CriteriaBuilder cb;

    @PostConstruct
    public void init() {
        cb = entityManager.getCriteriaBuilder();
    }

    public Page<Activity> findAllWithFilters(ReportActivityRequest filterRequest, Pageable pageable)
    {
        return repository.findAll(
            ActivitySpecification.builder()
                .includeOnlyUserActivities(Boolean.TRUE)
                .withZoneId(filterRequest.getZoneId())
                .withUserAuthId(filterRequest.getUserAuthId())
                .withSocialNetworkId(filterRequest.getSocialNetworkId())
                .withDeviceId(filterRequest.getDeviceId())
                .withFarmId(filterRequest.getFarmId())
                .withActivityTypeId(filterRequest.getActivityTypeId())
                .withAccountId(filterRequest.getAccountId())
                .withSimpleDate(filterRequest.getSimpleDate())
                .withDateRange(filterRequest.getFromDate(), filterRequest.getToDate())
                .withMonthDate(filterRequest.getMonthDate())
                .withYearDate(filterRequest.getYearDate()), pageable
        );
    }

    public List<Activity> findAllWithFiltersNoPaginated(ReportActivityRequest filterRequest,
        String sortBy, boolean isDescending)
    {
        return repository.findAll(ActivitySpecification.builder()
            .includeOnlyUserActivities(Boolean.TRUE)
            .withZoneId(filterRequest.getZoneId())
            .withUserAuthId(filterRequest.getUserAuthId())
            .withSocialNetworkId(filterRequest.getSocialNetworkId())
            .withDeviceId(filterRequest.getDeviceId())
            .withFarmId(filterRequest.getFarmId())
            .withActivityTypeId(filterRequest.getActivityTypeId())
            .withAccountId(filterRequest.getAccountId())
            .withSimpleDate(filterRequest.getSimpleDate())
            .withDateRange(filterRequest.getFromDate(), filterRequest.getToDate())
            .withMonthDate(filterRequest.getMonthDate())
            .withYearDate(filterRequest.getYearDate())
            .withOrderBy(sortBy, isDescending));
    }

    public ActivityTypeTotalResponse getTotalActivitiesByType(ReportActivityRequest filterRequest) {
        CriteriaQuery<ActivityTypeTotalResponse> totalActivitiesQuery = cb.createQuery(
            ActivityTypeTotalResponse.class);
        Root<Activity> activityRoot = totalActivitiesQuery.from(Activity.class);
        Join<Activity, ActivityType> activityTypeJoin = activityRoot.join(
            "activityType", JoinType.INNER);

        setTotalActivitiesByTypeSelectClause(totalActivitiesQuery, activityRoot, activityTypeJoin);

        totalActivitiesQuery.where(ActivitySpecification.builder()
            .includeOnlyUserActivities(Boolean.TRUE)
            .withUserAuthId(filterRequest.getUserAuthId())
            .withSocialNetworkId(filterRequest.getSocialNetworkId())
            .withDeviceId(filterRequest.getDeviceId())
            .withFarmId(filterRequest.getFarmId())
            .withActivityTypeId(filterRequest.getActivityTypeId())
            .withAccountId(filterRequest.getAccountId())
            .withSimpleDate(filterRequest.getSimpleDate())
            .withDateRange(filterRequest.getFromDate(), filterRequest.getToDate())
            .withMonthDate(filterRequest.getMonthDate())
            .withYearDate(filterRequest.getYearDate())
            .toPredicate(activityRoot, totalActivitiesQuery, cb));

        return entityManager.createQuery(totalActivitiesQuery)
            .getSingleResult();
    }

    public void setTotalActivitiesByTypeSelectClause(
        CriteriaQuery<ActivityTypeTotalResponse> totalActivitiesQuery, Root<?> activityRoot,
        Join<?, ?> activityTypeJoin)
    {
        totalActivitiesQuery.select(cb.construct(
            ActivityTypeTotalResponse.class,
            // totalActivities;
            cb.countDistinct(activityRoot.get("id")),
            // totalComments;
            CommonSpecification.getTotalBySumCase(
                cb, activityTypeJoin.get("code"),
                ActivityTypeCodeEnum.COMENTARIO.toString()
            ),
            // totalGroups;
            CommonSpecification.getTotalBySumCase(
                cb, activityTypeJoin.get("code"), ActivityTypeCodeEnum.GRUPO.toString()),
            // totalFriends;
            CommonSpecification.getTotalBySumCase(
                cb, activityTypeJoin.get("code"), ActivityTypeCodeEnum.AMISTAD.toString()),
            // totalPageFollows;
            CommonSpecification.getTotalBySumCase(
                cb, activityTypeJoin.get("code"), ActivityTypeCodeEnum.FOLLOW.toString()),
            // totalReactions;
            CommonSpecification.getTotalBySumCase(
                cb, activityTypeJoin.get("code"), ActivityTypeCodeEnum.REACCION.toString()),
            // totalPublications;
            CommonSpecification.getTotalBySumCase(
                cb, activityTypeJoin.get("code"),
                ActivityTypeCodeEnum.PUBLICACION.toString()
            ),
            // totalTiktokProfileFollows;
            CommonSpecification.getTotalBySumCase(
                cb, activityTypeJoin.get("code"),
                ActivityTypeCodeEnum.FOLLOW_TIKTOK.toString()
            )
        ));
    }

    public String getReportDateDetail(ReportActivityRequest filterRequest) {
        var reportTypeStr = new StringBuilder();
        reportTypeStr.append("Por fechas: ");

        var dateDetailStr = new StringBuilder();

        try {
            for (PropertyDescriptor propertyDescriptor : Introspector.getBeanInfo(
                    filterRequest.getClass(), Object.class)
                .getPropertyDescriptors()) {

                Object value = propertyDescriptor.getReadMethod()
                    .invoke(filterRequest);

                if (value == null) {
                    continue;
                }

                switch (propertyDescriptor.getName()) {
                    case "simpleDate" -> dateDetailStr.append(
                        excelUtil.getDateFormat((ZonedDateTime) value, "dd-MM-yyyy"));
                    case "fromDate" -> dateDetailStr.append("Desde ")
                        .append(excelUtil.getDateFormat((ZonedDateTime) value, "dd-MM-yyyy"));
                    case "toDate" -> dateDetailStr.append(" hasta ")
                        .append(excelUtil.getDateFormat((ZonedDateTime) value, "dd-MM-yyyy"));
                    case "monthDate" -> dateDetailStr.append(
                        excelUtil.getDateFormat((ZonedDateTime) value, "MM-yyyy"));
                    case "yearDate" -> dateDetailStr.append(
                        excelUtil.getDateFormat((ZonedDateTime) value, "yyyy"));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return dateDetailStr.isEmpty() ? reportTypeStr.append("Sin filtro de fechas")
            .toString() : reportTypeStr.append(dateDetailStr)
            .toString();
    }

    public List<ReportRatingResponse> getActivitiesRatings(ReportRatingRequest filterRequest,
        boolean isDescending)
    {
        return switch (filterRequest.getRatingType()) {
            case ACCOUNT -> getActivitiesRatingByAccount(filterRequest, isDescending);
            case DEVICE -> getActivitiesRatingByDevice(filterRequest, isDescending);
            case FARM -> getActivitiesRatingByFarm(filterRequest, isDescending);
            case AUTH_USER -> getActivitiesRatingByAuthUser(filterRequest, isDescending);
        };
    }

    private List<ReportRatingResponse> getActivitiesRatingByAccount(
        ReportRatingRequest filterRequest, boolean isDescending)
    {
        CriteriaQuery<ReportRatingResponse> ratingAccountQuery = cb.createQuery(
            ReportRatingResponse.class);

        Root<Account> accountRoot = ratingAccountQuery.from(Account.class);
        Join<Account, Activity> activityJoin = accountRoot.join("activities", JoinType.INNER);

        Expression<Long> totalActivities = cb.countDistinct(activityJoin.get("id"));

        ratingAccountQuery.select(cb.construct(
            ReportRatingResponse.class,
            // ratingById
            accountRoot.get("id"),
            // ratingByName
            accountRoot.get("username"), totalActivities
        ));

        ratingAccountQuery.groupBy(accountRoot.get("id"), accountRoot.get("username"));

        ratingAccountQuery.orderBy(
            isDescending ? cb.desc(totalActivities) : cb.asc(totalActivities));

        ratingAccountQuery.where(AccountSpecification.builder()
            .withSocialNetworkId(filterRequest.getSocialNetworkId())
            .withAccountType(AccountTypeEnum.INTERNAL)
            .toPredicate(accountRoot, ratingAccountQuery, cb));

        return entityManager.createQuery(ratingAccountQuery)
            .setMaxResults(filterRequest.getLimit())
            .getResultList();
    }

    private List<ReportRatingResponse> getActivitiesRatingByDevice(
        ReportRatingRequest filterRequest, boolean isDescending)
    {
        CriteriaQuery<ReportRatingResponse> ratingDeviceQuery = cb.createQuery(
            ReportRatingResponse.class);

        Root<Device> deviceRoot = ratingDeviceQuery.from(Device.class);
        Join<Device, Account> accountJoin = deviceRoot.join("accounts", JoinType.INNER);
        Join<Account, Activity> activityJoin = accountJoin.join("activities", JoinType.INNER);

        Expression<Long> totalActivities = cb.countDistinct(activityJoin.get("id"));

        ratingDeviceQuery.select(cb.construct(
            ReportRatingResponse.class,
            // ratingById
            deviceRoot.get("id"),
            // ratingByName
            deviceRoot.get("deviceNumber"), totalActivities
        ));

        ratingDeviceQuery.groupBy(deviceRoot.get("id"), deviceRoot.get("deviceNumber"));

        ratingDeviceQuery.orderBy(
            isDescending ? cb.desc(totalActivities) : cb.asc(totalActivities));

        ratingDeviceQuery.where(DeviceSpecification.builder()
            .withSocialNetworkId(filterRequest.getSocialNetworkId())
            .toPredicate(deviceRoot, ratingDeviceQuery, cb));

        return entityManager.createQuery(ratingDeviceQuery)
            .setMaxResults(filterRequest.getLimit())
            .getResultList();
    }

    private List<ReportRatingResponse> getActivitiesRatingByFarm(ReportRatingRequest filterRequest,
        boolean isDescending)
    {
        CriteriaQuery<ReportRatingResponse> ratingFarmQuery = cb.createQuery(
            ReportRatingResponse.class);

        Root<Farm> farmRoot = ratingFarmQuery.from(Farm.class);
        Join<Farm, Device> deviceJoin = farmRoot.join("devices", JoinType.INNER);
        Join<Device, Account> accountJoin = deviceJoin.join("accounts", JoinType.INNER);
        Join<Account, Activity> activityJoin = accountJoin.join("activities", JoinType.INNER);

        Expression<Long> totalActivities = cb.countDistinct(activityJoin.get("id"));

        ratingFarmQuery.select(cb.construct(
            ReportRatingResponse.class,
            // ratingById
            farmRoot.get("id"),
            // ratingByName
            farmRoot.get("name"), totalActivities
        ));

        ratingFarmQuery.groupBy(farmRoot.get("id"), farmRoot.get("name"));

        ratingFarmQuery.orderBy(isDescending ? cb.desc(totalActivities) : cb.asc(totalActivities));

        return entityManager.createQuery(ratingFarmQuery)
            .setMaxResults(filterRequest.getLimit())
            .getResultList();
    }

    private List<ReportRatingResponse> getActivitiesRatingByAuthUser(
        ReportRatingRequest filterRequest, boolean isDescending)
    {
        CriteriaQuery<ReportRatingResponse> ratingAuthUserQuery = cb.createQuery(
            ReportRatingResponse.class);

        Root<Activity> activityRoot = ratingAuthUserQuery.from(Activity.class);

        Expression<Long> totalActivities = cb.countDistinct(activityRoot.get("id"));

        Expression<String> createdByName = cb.function(
            "jsonb_extract_path_text", String.class,
            activityRoot.get("createdByData"), cb.literal("name")
        );

        ratingAuthUserQuery.select(cb.construct(
            ReportRatingResponse.class,
            // ratingById
            activityRoot.get("createdBy")
                .as(UUID.class),
            // ratingByName
            createdByName, totalActivities
        ));

        ratingAuthUserQuery.groupBy(activityRoot.get("createdBy"), createdByName);

        ratingAuthUserQuery.orderBy(
            isDescending ? cb.desc(totalActivities) : cb.asc(totalActivities));

        ratingAuthUserQuery.where(ActivitySpecification.builder()
            .includeOnlyUserActivities(true)
            .withSocialNetworkId(filterRequest.getSocialNetworkId())
            .toPredicate(activityRoot, ratingAuthUserQuery, cb));

        return entityManager.createQuery(ratingAuthUserQuery)
            .setMaxResults(filterRequest.getLimit())
            .getResultList();
    }
}
