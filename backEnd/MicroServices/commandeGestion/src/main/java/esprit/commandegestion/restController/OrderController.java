package esprit.commandegestion.restController;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequestMapping("api/order")
public class OrderController {
    OrderService orderService;

    @GetMapping("/retrieveAllOrder")
    public List<Order> retrieveAllOrder() {
        return orderService.retrieveAllOrder();
    }

    @PostMapping("/addOrder")
    public Order addOrder(@RequestBody Order c) {
        return orderService.addOrder(c);
    }

    @PutMapping("/updateOrder")
    public Order updateOrder(@RequestBody Order c) {
        return orderService.updateOrder(c);
    }

    @GetMapping("/retrieveOrder/{idOrder}")
    public Order retrieveOrder(@PathVariable("idOrder") Integer idOrder) {
        return orderService.retrieveOrder(idOrder);
    }
}