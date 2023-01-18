package tech.hyperjump.esigning.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.hyperjump.esigning.domain.AuditTrail;

/**
 * Spring Data JPA repository for the AuditTrail entity.
 */
@Repository
public interface AuditTrailRepository extends JpaRepository<AuditTrail, Long> {
    @Query("select auditTrail from AuditTrail auditTrail where auditTrail.user.login = ?#{principal.username}")
    List<AuditTrail> findByUserIsCurrentUser();

    default Optional<AuditTrail> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<AuditTrail> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<AuditTrail> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct auditTrail from AuditTrail auditTrail left join fetch auditTrail.document left join fetch auditTrail.user",
        countQuery = "select count(distinct auditTrail) from AuditTrail auditTrail"
    )
    Page<AuditTrail> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct auditTrail from AuditTrail auditTrail left join fetch auditTrail.document left join fetch auditTrail.user")
    List<AuditTrail> findAllWithToOneRelationships();

    @Query(
        "select auditTrail from AuditTrail auditTrail left join fetch auditTrail.document left join fetch auditTrail.user where auditTrail.id =:id"
    )
    Optional<AuditTrail> findOneWithToOneRelationships(@Param("id") Long id);
}
