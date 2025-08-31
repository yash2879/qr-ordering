package in.tablese.tablese_core.service;

import in.tablese.tablese_core.model.CustomerOrder;
import in.tablese.tablese_core.repository.CustomerOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final CustomerOrderRepository customerOrderRepository;
    // We will inject other repositories like RestaurantRepository and MenuItemRepository here later

    @Transactional
    public CustomerOrder createOrder(CustomerOrder customerOrder) {
        // In the future, this method will be more complex.
        // It will need to fetch the Restaurant, verify MenuItems, calculate the total, etc.
        // For now, we'll just save the incoming order object.
        return customerOrderRepository.save(customerOrder);
    }
}