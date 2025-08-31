package in.tablese.tablese_core.service;

import in.tablese.tablese_core.model.CustomerOrder;
import in.tablese.tablese_core.repository.CustomerOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final CustomerOrderRepository customerOrderRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public CustomerOrder createOrder(CustomerOrder customerOrder) {
        CustomerOrder savedOrder = customerOrderRepository.save(customerOrder);

        String destination = "/topic/orders/" + customerOrder.getRestaurant().getId();
        messagingTemplate.convertAndSend(destination, customerOrder);

        log.info("Broadcasted Order: {} to {}", customerOrder.getId(), destination);

        return savedOrder;
    }
}