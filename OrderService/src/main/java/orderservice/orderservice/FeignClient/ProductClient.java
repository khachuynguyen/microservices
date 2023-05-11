package orderservice.orderservice.FeignClient;

import orderservice.orderservice.DTO.Product;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("PRODUCT-SERVICE")
@Component
public interface ProductClient {
    @RequestMapping(method = RequestMethod.GET, value = "/api/products/{id}")
    Product getProduct(@PathVariable("id") int productId);
    @RequestMapping(method = RequestMethod.PUT, value = "/api/products/update-quantity/{id}")
    Product updateQuantityToOrder(@PathVariable("id") int productId, @RequestParam("quantity") int quantity);
}
