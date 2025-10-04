package com.kernotec.farm.util;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;

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
}
