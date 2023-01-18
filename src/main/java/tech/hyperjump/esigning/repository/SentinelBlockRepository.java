package tech.hyperjump.esigning.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tech.hyperjump.esigning.domain.SentinelBlock;

/**
 * Spring Data JPA repository for the SentinelBlock entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SentinelBlockRepository extends JpaRepository<SentinelBlock, Long> {}
