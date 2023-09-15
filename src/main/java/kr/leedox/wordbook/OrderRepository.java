package kr.leedox.wordbook;

import kr.leedox.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    Order findByPaypalOrderId(String paypalOrderId);
}
