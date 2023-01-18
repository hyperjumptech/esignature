package tech.hyperjump.esigning.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.hyperjump.esigning.domain.StorageBlobAttachment;

/**
 * Spring Data JPA repository for the StorageBlobAttachment entity.
 */
@Repository
public interface StorageBlobAttachmentRepository extends JpaRepository<StorageBlobAttachment, Long> {
    default Optional<StorageBlobAttachment> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<StorageBlobAttachment> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<StorageBlobAttachment> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct storageBlobAttachment from StorageBlobAttachment storageBlobAttachment left join fetch storageBlobAttachment.sblob",
        countQuery = "select count(distinct storageBlobAttachment) from StorageBlobAttachment storageBlobAttachment"
    )
    Page<StorageBlobAttachment> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct storageBlobAttachment from StorageBlobAttachment storageBlobAttachment left join fetch storageBlobAttachment.sblob"
    )
    List<StorageBlobAttachment> findAllWithToOneRelationships();

    @Query(
        "select storageBlobAttachment from StorageBlobAttachment storageBlobAttachment left join fetch storageBlobAttachment.sblob where storageBlobAttachment.id =:id"
    )
    Optional<StorageBlobAttachment> findOneWithToOneRelationships(@Param("id") Long id);
}
