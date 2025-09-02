package in.tablese.tablese_core.mapper;

import in.tablese.tablese_core.dto.CustomerOrderResponseDto;
import in.tablese.tablese_core.dto.KitchenOrderDto;
import in.tablese.tablese_core.dto.KitchenOrderItemDto;
import in.tablese.tablese_core.dto.OrderItemResponseDto;
import in.tablese.tablese_core.model.CustomerOrder;

import java.util.stream.Collectors;

public class OrderMapper {

    public static KitchenOrderDto toKitchenOrderDto(CustomerOrder order) {
        var items = order.getOrderItems().stream()
                .map(orderItem -> new KitchenOrderItemDto(
                        orderItem.getQuantity(),
                        orderItem.getMenuItem().getName()
                ))
                .collect(Collectors.toList());

        return new KitchenOrderDto(
                order.getId(),
                order.getTableNumber(),
                order.getStatus(),
                order.getOrderTime(),
                items
        );
    }

    public static CustomerOrderResponseDto toCustomerOrderResponseDto(CustomerOrder order) {
        var items = order.getOrderItems().stream()
                .map(orderItem -> new OrderItemResponseDto(
                        orderItem.getQuantity(),
                        orderItem.getMenuItem().getName(),
                        orderItem.getMenuItem().getPrice()
                ))
                .collect(Collectors.toList());

        return new CustomerOrderResponseDto(
                order.getId(),
                order.getTableNumber(),
                order.getStatus(),
                order.getOrderTime(),
                order.getRestaurant().getId(),
                items
        );
    }
}