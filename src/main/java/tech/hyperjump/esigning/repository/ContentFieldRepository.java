package tech.hyperjump.esigning.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.hyperjump.esigning.domain.ContentField;

/**
 * Spring Data JPA repository for the ContentField entity.
 */
@Repository
public interface ContentFieldRepository extends JpaRepository<ContentField, Long> {
    @Query("select contentField from ContentField contentField where contentField.signatory.login = ?#{principal.username}")
    List<ContentField> findBySignatoryIsCurrentUser();

    default Optional<ContentField> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ContentField> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ContentField> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct contentField from ContentField contentField left join fetch contentField.document left join fetch contentField.signatory",
        countQuery = "select count(distinct contentField) from ContentField contentField"
    )
    Page<ContentField> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct contentField from ContentField contentField left join fetch contentField.document left join fetch contentField.signatory"
    )
    List<ContentField> findAllWithToOneRelationships();

    @Query(
        "select contentField from ContentField contentField left join fetch contentField.document left join fetch contentField.signatory where contentField.id =:id"
    )
    Optional<ContentField> findOneWithToOneRelationships(@Param("id") Long id);
}
