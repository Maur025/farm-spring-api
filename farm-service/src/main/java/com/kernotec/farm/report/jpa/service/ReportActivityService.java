package com.kernotec.farm.report.jpa.service;

import com.kernotec.farm.activity.jpa.entity.Activity;
import com.kernotec.farm.activity.jpa.repository.ActivityRepository;
import com.kernotec.farm.activity.jpa.specification.activity.ActivitySpecification;
import com.kernotec.farm.parametric.jpa.entity.ActivityType;
import com.kernotec.farm.parametric.jpa.enums.ActivityTypeCodeEnum;
import com.kernotec.farm.report.rest.dto.request.ReportActivityRequest;
import com.kernotec.farm.report.rest.dto.response.activity.ActivityTypeTotalResponse;
import com.kernotec.farm.util.CommonSpecification;
import com.kernotec.farm.util.ExcelUtil;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.time.ZonedDateTime;
import java.util.List;
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
}
