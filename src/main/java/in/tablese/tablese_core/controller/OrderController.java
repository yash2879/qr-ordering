package in.tablese.tablese_core.controller;

import in.tablese.tablese_core.dto.CreateOrderRequest;
import in.tablese.tablese_core.dto.CustomerOrderResponseDto;
import in.tablese.tablese_core.dto.UpdateOrderStatusRequest;
import in.tablese.tablese_core.mapper.OrderMapper;
import in.tablese.tablese_core.model.CustomerOrder;
import in.tablese.tablese_core.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<CustomerOrderResponseDto> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        CustomerOrder createdOrder = orderService.createOrder(request);
        // Convert to DTO before sending the response
        return new ResponseEntity<>(OrderMapper.toCustomerOrderResponseDto(createdOrder), HttpStatus.CREATED);
    }

    // Update this method as well
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<CustomerOrderResponseDto> updateOrderStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody UpdateOrderStatusRequest request) {
        CustomerOrder updatedOrder = orderService.updateOrderStatus(orderId, request.newStatus());
        // Convert to DTO before sending the response
        return ResponseEntity.ok(OrderMapper.toCustomerOrderResponseDto(updatedOrder));
    }

    // NEW ENDPOINT for the kitchen hub to fetch initial state
    @GetMapping("/restaurant/{restaurantId}/active")
    public ResponseEntity<List<CustomerOrderResponseDto>> getActiveOrders(@PathVariable Long restaurantId) {
        List<CustomerOrder> activeOrders = orderService.getActiveOrdersForRestaurant(restaurantId);

        // Convert the list of entities to a list of DTOs before returning
        List<CustomerOrderResponseDto> orderDtos = activeOrders.stream()
                .map(OrderMapper::toCustomerOrderResponseDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(orderDtos);
    }
}