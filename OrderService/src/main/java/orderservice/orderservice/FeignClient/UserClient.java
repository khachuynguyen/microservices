package orderservice.orderservice.FeignClient;

import orderservice.orderservice.DTO.Product;
import orderservice.orderservice.DTO.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("USER-SERVICE")
public interface UserClient {
    @RequestMapping(method = RequestMethod.GET, value = "/api/user/{id}")
    User getUserById(@PathVariable("id") int userId, @RequestHeader("Authorization") String token);
}
