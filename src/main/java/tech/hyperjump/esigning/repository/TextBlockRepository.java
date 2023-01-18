package tech.hyperjump.esigning.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.hyperjump.esigning.domain.TextBlock;

/**
 * Spring Data JPA repository for the TextBlock entity.
 */
@Repository
public interface TextBlockRepository extends JpaRepository<TextBlock, Long> {
    @Query("select textBlock from TextBlock textBlock where textBlock.user.login = ?#{principal.username}")
    List<TextBlock> findByUserIsCurrentUser();

    default Optional<TextBlock> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<TextBlock> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<TextBlock> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct textBlock from TextBlock textBlock left join fetch textBlock.user",
        countQuery = "select count(distinct textBlock) from TextBlock textBlock"
    )
    Page<TextBlock> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct textBlock from TextBlock textBlock left join fetch textBlock.user")
    List<TextBlock> findAllWithToOneRelationships();

    @Query("select textBlock from TextBlock textBlock left join fetch textBlock.user where textBlock.id =:id")
    Optional<TextBlock> findOneWithToOneRelationships(@Param("id") Long id);
}
