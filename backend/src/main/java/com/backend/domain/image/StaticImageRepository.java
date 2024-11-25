package com.backend.domain.image;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StaticImageRepository extends JpaRepository<StaticImage, Long> {
}