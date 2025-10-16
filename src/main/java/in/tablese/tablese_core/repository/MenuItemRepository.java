package in.tablese.tablese_core.repository;

import in.tablese.tablese_core.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    // Spring Data JPA automatically implements this method based on its name
    List<MenuItem> findByRestaurantIdAndIsActiveTrue(Long restaurantId);

    List<MenuItem> findAllByRestaurantId(Long restaurantId);
}