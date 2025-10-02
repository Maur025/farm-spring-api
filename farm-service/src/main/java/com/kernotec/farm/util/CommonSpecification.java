package com.kernotec.farm.util;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
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
}
