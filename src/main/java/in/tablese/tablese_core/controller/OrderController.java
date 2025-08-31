package in.tablese.tablese_core.controller;

import in.tablese.tablese_core.model.CustomerOrder;
import in.tablese.tablese_core.model.Restaurant;
import in.tablese.tablese_core.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public CustomerOrder createOrder(@RequestBody CustomerOrder order) {
        // HACK: Manually set a restaurant for testing
        Restaurant r = new Restaurant();
        r.setId(1L);
        order.setRestaurant(r);
        return orderService.createOrder(order);
    }
}