package in.tablese.tablese_core.service;

import in.tablese.tablese_core.dto.CreateOrderRequest;
import in.tablese.tablese_core.dto.KitchenOrderDto;
import in.tablese.tablese_core.dto.OrderItemRequest;
import in.tablese.tablese_core.exception.ResourceNotFoundException;
import in.tablese.tablese_core.mapper.OrderMapper;
import in.tablese.tablese_core.model.CustomerOrder;
import in.tablese.tablese_core.model.MenuItem;
import in.tablese.tablese_core.model.OrderItem;
import in.tablese.tablese_core.model.Restaurant;
import in.tablese.tablese_core.repository.CustomerOrderRepository;
import in.tablese.tablese_core.repository.MenuItemRepository;
import in.tablese.tablese_core.repository.RestaurantRepository;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final CustomerOrderRepository customerOrderRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;

    @Transactional
    public CustomerOrder createOrder(CreateOrderRequest request) {
        // 1. Find the restaurant
        Restaurant restaurant = restaurantRepository.findById(request.restaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        // 2. Create the main order object
        CustomerOrder newOrder = new CustomerOrder();
        newOrder.setRestaurant(restaurant);
        newOrder.setTableNumber(request.tableNumber());
        newOrder.setStatus("NEW");
        newOrder.setOrderTime(LocalDateTime.now());

        List<OrderItem> orderItems = new ArrayList<>();

        // 3. Loop through the items from the request
        for (OrderItemRequest itemRequest : request.items()) {
            MenuItem menuItem = menuItemRepository.findById(itemRequest.menuItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("Menu item not found"));

            OrderItem orderItem = new OrderItem();
            orderItem.setMenuItem(menuItem);
            orderItem.setQuantity(itemRequest.quantity());
            orderItem.setCustomerOrder(newOrder); // Link back to the main order
            orderItems.add(orderItem);
        }

        // 4. Set the complete list of items on the order
        newOrder.setOrderItems(orderItems);

        // 5. Save the order (this will also save all the linked order items)
        CustomerOrder savedOrder = customerOrderRepository.save(newOrder);

        KitchenOrderDto kitchenDto = OrderMapper.toKitchenOrderDto(savedOrder);
        String destination = "/topic/orders/" + savedOrder.getRestaurant().getId();
        messagingTemplate.convertAndSend(destination, kitchenDto); // Send the clean DTO
        log.info("Broadcasted Order: {} to {}", savedOrder.getId(), destination);

        return savedOrder;
    }

    @Transactional
    public CustomerOrder updateOrderStatus(Long orderId, String newStatus) {
        // 1. Find the existing order
        CustomerOrder order = customerOrderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        // 2. Update its status
        order.setStatus(newStatus);

        // 3. Save the change
        CustomerOrder updatedOrder = customerOrderRepository.save(order);

        // 4. Broadcast the ENTIRE updated order object back to the WebSocket topic
        KitchenOrderDto kitchenDto = OrderMapper.toKitchenOrderDto(updatedOrder);
        String destination = "/topic/orders/" + updatedOrder.getRestaurant().getId();
        messagingTemplate.convertAndSend(destination, kitchenDto); // Send the clean DTO
        log.info("Broadcasted status update for Order: {} to {}", updatedOrder.getId(), destination);

        return updatedOrder;
    }

    // NEW METHOD to get all active orders
    public List<CustomerOrder> getActiveOrdersForRestaurant(Long restaurantId) {
        // Define what "active" means for our business
        List<String> activeStatuses = Arrays.asList("NEW", "PREPARING", "COMPLETED");

        // Use our new repository method to fetch the data
        return customerOrderRepository.findByRestaurantIdAndStatusIn(restaurantId, activeStatuses);
    }
}