package com.hrms.common.entity;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import jakarta.persistence.Column;

import java.time.LocalDateTime;


@Getter
@Setter
@MappedSuperclass //@MappedSuperclass is used when a class contains common fields that should be inherited by multiple entities. Hibernate does not create a table for the superclass; instead, it maps its fields into the child entity tables. This avoids code duplication and follows the DRY principle.
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(updatable = false,nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

}
