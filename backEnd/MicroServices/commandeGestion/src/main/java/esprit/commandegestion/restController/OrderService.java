package esprit.commandegestion.restController;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderService implements IOrdreService {
    OrderRepository orderRepository;


    @Override
    public List<Order> retrieveAllOrder() {
        return (List<Order>) orderRepository.findAll();

    }

    @Override
    public Order addOrder(Order c) {
        return orderRepository.save(c);
    }

    @Override
    public Order updateOrder(Order c) {
        return orderRepository.save(c);
    }

    @Override
    public Order retrieveOrder(Integer id) {
        return orderRepository.findById(id).orElse(null);
    }
}
