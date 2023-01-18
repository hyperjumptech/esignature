package tech.hyperjump.esigning.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.hyperjump.esigning.domain.SignatureBlock;

/**
 * Spring Data JPA repository for the SignatureBlock entity.
 */
@Repository
public interface SignatureBlockRepository extends JpaRepository<SignatureBlock, Long> {
    @Query("select signatureBlock from SignatureBlock signatureBlock where signatureBlock.user.login = ?#{principal.username}")
    List<SignatureBlock> findByUserIsCurrentUser();

    default Optional<SignatureBlock> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<SignatureBlock> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<SignatureBlock> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct signatureBlock from SignatureBlock signatureBlock left join fetch signatureBlock.user",
        countQuery = "select count(distinct signatureBlock) from SignatureBlock signatureBlock"
    )
    Page<SignatureBlock> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct signatureBlock from SignatureBlock signatureBlock left join fetch signatureBlock.user")
    List<SignatureBlock> findAllWithToOneRelationships();

    @Query("select signatureBlock from SignatureBlock signatureBlock left join fetch signatureBlock.user where signatureBlock.id =:id")
    Optional<SignatureBlock> findOneWithToOneRelationships(@Param("id") Long id);
}
