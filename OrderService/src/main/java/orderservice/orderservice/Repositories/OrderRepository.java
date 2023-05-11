package orderservice.orderservice.Repositories;

import orderservice.orderservice.Models.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Integer> {
    List<Orders> getAllOrderByUserId(Integer userId);
}
