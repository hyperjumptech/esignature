package tech.hyperjump.esigning.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.hyperjump.esigning.domain.StorageBlob;

/**
 * Spring Data JPA repository for the StorageBlob entity.
 */
@Repository
public interface StorageBlobRepository extends JpaRepository<StorageBlob, Long> {
    default Optional<StorageBlob> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<StorageBlob> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<StorageBlob> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct storageBlob from StorageBlob storageBlob left join fetch storageBlob.document",
        countQuery = "select count(distinct storageBlob) from StorageBlob storageBlob"
    )
    Page<StorageBlob> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct storageBlob from StorageBlob storageBlob left join fetch storageBlob.document")
    List<StorageBlob> findAllWithToOneRelationships();

    @Query("select storageBlob from StorageBlob storageBlob left join fetch storageBlob.document where storageBlob.id =:id")
    Optional<StorageBlob> findOneWithToOneRelationships(@Param("id") Long id);

    Optional<StorageBlob> findOneByDocumentId(@Param("id") Long id);

    Set<StorageBlob> findAllByDocumentId(@Param("id") Long id);
}
