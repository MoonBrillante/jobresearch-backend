package com.example.jobresearch.repositories;

import com.example.jobresearch.domain.models.Job;
import com.example.jobresearch.domain.models.JobStatus;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class JobSpecification {

    public static Specification<Job> filterBy(
            String position,
            String company,
            String location,
            String mode,
            JobStatus status
    ) {
        return (root, query, criteriaBuilder) -> {
            var predicates = criteriaBuilder.conjunction();

            if (StringUtils.hasText(position)) {
                predicates = criteriaBuilder.and(predicates,
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("position")),
                                "%" + position.toLowerCase() + "%"
                        ));
            }

            if (StringUtils.hasText(company)) {
                predicates = criteriaBuilder.and(predicates,
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("company")),
                                "%" + company.toLowerCase() + "%"
                        ));
            }

            if (StringUtils.hasText(location)) {
                predicates = criteriaBuilder.and(predicates,
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("location")),
                                "%" + location.toLowerCase() + "%"
                        ));
            }

            if (StringUtils.hasText(mode)) {
                predicates = criteriaBuilder.and(predicates,
                        criteriaBuilder.equal(root.get("mode"), mode));
            }

            if (status != null) {
                predicates = criteriaBuilder.and(predicates,
                        criteriaBuilder.equal(root.get("status"), status));
            }

            return predicates;
        };
    }
}