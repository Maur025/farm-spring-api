package com.kernotec.farm.util;

import com.kernotec.farm.report.jpa.enums.TemporyDateForRequestEnum;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommonSpecification {

    public static void queryOrderBy(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder cb,
        String orderBy, boolean isDescending)
    {
        if (orderBy == null) {
            return;
        }

        query.orderBy(isDescending ? cb.desc(root.get(orderBy)) : cb.asc(root.get(orderBy)));
    }

    public static Expression<?> getTotalBySumCase(CriteriaBuilder cb, Path<?> pathToSum,
        Object valueToCompare)
    {
        return cb.coalesce(
            cb.sum(cb.<Long>selectCase()
                .when(cb.equal(pathToSum, valueToCompare), 1L)
                .otherwise(0L)), 0L
        );
    }

    public static Expression<?> getTotalBySumCase(CriteriaBuilder cb, Predicate predicate) {
        return cb.coalesce(
            cb.sum(cb.<Long>selectCase()
                .when(predicate, 1L)
                .otherwise(0L)), 0L
        );
    }

    public static Predicate addTimeLapsePredicateByEnum(CriteriaBuilder cb, ZoneId userZone,
        TemporyDateForRequestEnum temporaryDateEnum, Path<ZonedDateTime> zonedDateTimeToCompare)
    {
        if (userZone == null || temporaryDateEnum == null || zonedDateTimeToCompare == null) {
            return null;
        }

        ZonedDateTime to = ZonedDateTime.now(userZone);
        ZonedDateTime toStartOfDay = to.toLocalDate()
            .atStartOfDay(userZone);
        ZonedDateTime toEndOfDay = toStartOfDay.plusDays(1)
            .minusNanos(1);

        ZonedDateTime from = switch (temporaryDateEnum) {
            case ALL -> ZonedDateTime.of(1970, 1, 1, 0, 0, 0, 0, userZone);
            case LAST_WEEK -> to.minusWeeks(1);
            case LAST_MONTH -> to.minusMonths(1);
            case LAST_THREE_MONTHS -> to.minusMonths(3);
        };

        ZonedDateTime fromStartOfDay = from.toLocalDate()
            .atStartOfDay(userZone);

        log.info("Applying time lapse filter from {} to {}", fromStartOfDay, toEndOfDay);

        return cb.between(zonedDateTimeToCompare, fromStartOfDay, toEndOfDay);
    }

    public static ZonedDateTime[][] getMonthsOfYear(ZonedDateTime baseDate, ZoneId userZone) {
        List<ZonedDateTime> monthsOfYear = new ArrayList<>();

        for (int index = 1; index <= 12; index++) {
            monthsOfYear.add(baseDate.withMonth(index)
                .withDayOfMonth(1)
                .toLocalDate()
                .atStartOfDay(userZone));
        }

        List<ZonedDateTime[]> monthsOfYearList = new ArrayList<>();

        for (ZonedDateTime monthStart : monthsOfYear) {
            ZonedDateTime monthEnd = monthStart.plusMonths(1)
                .minusNanos(1);

            monthsOfYearList.add(new ZonedDateTime[]{monthStart, monthEnd});
        }

        return monthsOfYearList.toArray(ZonedDateTime[][]::new);
    }
}
