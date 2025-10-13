package com.kernotec.farm.util;

import com.kernotec.farm.report.jpa.enums.TemporyDateForRequestEnum;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.LocalTime;
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

    public static ZoneId getZoneId(String zoneId) {
        return ZoneId.of(zoneId != null ? zoneId : ZoneId.systemDefault()
            .toString());
    }

    public static Predicate simpleDatePredicate(CriteriaBuilder cb, Path<ZonedDateTime> datePath,
        ZonedDateTime simpleDateInput, String zoneId)
    {
        ZoneId userZoneId = getZoneId(zoneId);
        LocalDate simpleLocalDate = simpleDateInput.toLocalDate();

        ZonedDateTime startOfDay = simpleLocalDate.atStartOfDay(userZoneId);

        ZonedDateTime endOfDay = simpleLocalDate.atTime(LocalTime.MAX)
            .atZone(userZoneId);

        log.info("Applying simple date filter from {} to {}", startOfDay, endOfDay);

        return cb.between(datePath, startOfDay, endOfDay);
    }

    public static Predicate dateRangePredicate(CriteriaBuilder cb, Path<ZonedDateTime> datePath,
        ZonedDateTime fromDate, ZonedDateTime toDate, String zoneId)
    {
        ZoneId userZoneId = getZoneId(zoneId);

        LocalDate fromLocalDate = fromDate.toLocalDate();
        ZonedDateTime startOfFromDate = fromLocalDate.atStartOfDay(userZoneId);

        LocalDate toLocalDate = toDate.toLocalDate();
        ZonedDateTime endOfToDate = toLocalDate.atTime(LocalTime.MAX)
            .atZone(userZoneId);

        log.info("Applying date range filter from {} to {}", startOfFromDate, endOfToDate);

        return cb.between(datePath, startOfFromDate, endOfToDate);
    }

    public static Predicate monthDatePredicate(CriteriaBuilder cb, Path<ZonedDateTime> datePath,
        ZonedDateTime monthDateInput, String zoneId)
    {
        ZoneId userZoneId = getZoneId(zoneId);

        LocalDate monthLocalDate = monthDateInput.withDayOfMonth(1)
            .toLocalDate();

        ZonedDateTime startOfMonth = monthLocalDate.atStartOfDay(userZoneId);

        ZonedDateTime endOfMonth = startOfMonth.plusMonths(1)
            .minusNanos(1);

        log.info("Applying month date filter from {} to {}", startOfMonth, endOfMonth);

        return cb.between(datePath, startOfMonth, endOfMonth);
    }

    public static Predicate yearDatePredicate(CriteriaBuilder cb, Path<ZonedDateTime> datePath,
        ZonedDateTime yearDateInput, String zoneId)
    {
        ZoneId userZoneId = getZoneId(zoneId);

        LocalDate yearLocalDate = yearDateInput.withDayOfYear(1)
            .toLocalDate();

        ZonedDateTime startOfYear = yearLocalDate.atStartOfDay(userZoneId);

        ZonedDateTime endOfYear = startOfYear.plusYears(1)
            .minusNanos(1);

        log.info("Applying year date filter from {} to {}", startOfYear, endOfYear);

        return cb.between(datePath, startOfYear, endOfYear);
    }
}
