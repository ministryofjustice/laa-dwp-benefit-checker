package uk.gov.justice.laa.bc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.gov.justice.laa.bc.entity.ItemEntity;

/**
 * Repository for managing item entities.
 */
@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, Long> {
}