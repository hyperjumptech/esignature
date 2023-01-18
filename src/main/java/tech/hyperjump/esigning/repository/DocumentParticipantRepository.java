package tech.hyperjump.esigning.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.hyperjump.esigning.domain.DocumentParticipant;

/**
 * Spring Data JPA repository for the DocumentParticipant entity.
 */
@Repository
public interface DocumentParticipantRepository extends JpaRepository<DocumentParticipant, Long> {
    @Query(
        "select documentParticipant from DocumentParticipant documentParticipant where documentParticipant.user.login = ?#{principal.username}"
    )
    List<DocumentParticipant> findByUserIsCurrentUser();

    default Optional<DocumentParticipant> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<DocumentParticipant> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<DocumentParticipant> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct documentParticipant from DocumentParticipant documentParticipant left join fetch documentParticipant.document left join fetch documentParticipant.user",
        countQuery = "select count(distinct documentParticipant) from DocumentParticipant documentParticipant"
    )
    Page<DocumentParticipant> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct documentParticipant from DocumentParticipant documentParticipant left join fetch documentParticipant.document left join fetch documentParticipant.user"
    )
    List<DocumentParticipant> findAllWithToOneRelationships();

    @Query(
        "select documentParticipant from DocumentParticipant documentParticipant left join fetch documentParticipant.document left join fetch documentParticipant.user where documentParticipant.id =:id"
    )
    Optional<DocumentParticipant> findOneWithToOneRelationships(@Param("id") Long id);
}
