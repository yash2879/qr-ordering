package in.tablese.tablese_core.repository;

import in.tablese.tablese_core.model.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {

    // This method finds all orders for a given restaurant ID where the status
    // is one of the values in the provided list of statuses.
    List<CustomerOrder> findByRestaurantIdAndStatusIn(Long restaurantId, List<String> statuses);
}