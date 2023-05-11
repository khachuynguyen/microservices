package orderservice.orderservice.Controllers;


import jakarta.validation.Valid;
import orderservice.orderservice.DTO.CartDTO;
import orderservice.orderservice.DTO.GetTokenFromBearToken;
import orderservice.orderservice.DTO.Product;
import orderservice.orderservice.DTO.User;
import orderservice.orderservice.FeignClient.ProductClient;
import orderservice.orderservice.Models.Carts;
import orderservice.orderservice.Requests.AddToCartRequest;
import orderservice.orderservice.Services.CartService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class CartController {
    @Autowired
    private ProductClient productClient;
    @GetMapping("/carts/test")
    public ResponseEntity<String> testRestTemplate(){
        try{
            Product product =productClient.getProduct(10);
            return new ResponseEntity<>(product.getProductName(),HttpStatus.OK);
        }catch (Exception exception){
            return new ResponseEntity<>(exception.toString(),HttpStatus.OK);
        }

    }
    @Autowired
    private CartService cartService;
    @Autowired
    private GetTokenFromBearToken jwt;
    @PostMapping("/carts")
    public ResponseEntity<Carts> addToCart(@RequestBody @Valid AddToCartRequest request, @RequestHeader("Authorization") String token){
        Product product= null;
        try{
            product =productClient.getProduct(request.getProductId());
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        int userId = jwt.getUserId(token);
        Carts carts = cartService.addToCartRequest(request, userId, product );
        return new ResponseEntity<>(carts, HttpStatus.OK);
    }
    @GetMapping("/carts")
    public ResponseEntity<Object> getAllCartsOfUser(@RequestHeader("Authorization") String token){
        try{
            int userId = jwt.getUserId(token);
            List<CartDTO> carts = cartService.getAllCartsOfUser(userId);
            return new ResponseEntity<>(carts, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @DeleteMapping("/carts/{id}")
    public ResponseEntity<Object> deleteCartById(@PathVariable("id") int id, @RequestHeader("Authorization") String token){
        try{
            int userId = jwt.getUserId(token);
            boolean isSuccess = cartService.deleteCartByProductId(id,userId);
            return new ResponseEntity<>("Xóa thành công", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
