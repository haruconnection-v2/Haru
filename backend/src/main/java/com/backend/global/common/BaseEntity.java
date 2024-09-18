package com.backend.global.common;

import static java.lang.Boolean.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@MappedSuperclass
@Getter
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
	@CreatedDate
	private String createdAt;
	@LastModifiedDate private String updatedAt;
	private Boolean isDeleted = FALSE;
}
