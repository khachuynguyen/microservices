package productservice.productservice.Controllers;


import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import productservice.productservice.Components.JwtUtils;
import productservice.productservice.Models.Product;
import productservice.productservice.Requests.CreateProductRequest;
import productservice.productservice.Services.ProductService;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class ProductController {
    @Autowired
    private ProductService productService;
    @Value("${server.port}")
    private int serverPort;
    @Value("${jwt.app.jwtSecret}")
    private String jwtSecret;
    @Autowired
    private JwtUtils jwtUtils;
    @GetMapping("/api/products")
    public ResponseEntity<List<Product>> getAllProducts(){
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK) ;
    }
    @GetMapping("/api/products/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") int productId){
        return new ResponseEntity<>(productService.findProductById(productId), HttpStatus.OK) ;
    }
    @GetMapping("/api/manufacturers")
    public ResponseEntity< List<Object>> getAllManufacturers(){
        return new ResponseEntity<>(productService.getAllManufacturers(), HttpStatus.OK) ;
    }
    @GetMapping("/api/search")
    public ResponseEntity< List<Product>> getAllManufacturers(@RequestParam Map<String, String> allParams){
        List<Product> list = productService.searchProduct(allParams);
        return new ResponseEntity<>(list, HttpStatus.OK) ;
    }
    @PostMapping("/api/products")
    public ResponseEntity<Object> createProduct(@RequestBody @Valid CreateProductRequest productInformation){
        try {
            return new ResponseEntity<>(productService.saveProduct(productInformation), HttpStatus.OK);
        }catch (Exception exception){
            return new ResponseEntity<>(exception.toString(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @GetMapping("/api/products/test")
    public ResponseEntity<String> test(@Nullable @RequestHeader("Authorization") String token){
        try {
//            String token = "Unauth";
//            HttpHeaders header =  request.getHeaders();
//            if(header.containsKey("Authorization")) {
//                token = header.get("Authorization").toString();
//            }
            String userName = "0";
            if(token !=null ){
                String[] parts =token.split("\\s");
                userName =  parts[1];
                userName =  jwtUtils.getRoleFromJwt(userName);
            }
            return new ResponseEntity<>("port: "+serverPort+", userId="+userName, HttpStatus.OK);
        }catch (Exception exception){
            return new ResponseEntity<>(exception.toString(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @PutMapping("/api/products/{id}")
    public ResponseEntity<Product> updateProduct(@RequestBody @Valid CreateProductRequest productInformation, @PathVariable("id") int product_id){
        return new ResponseEntity<>(productService.updateProduct(productInformation, product_id), HttpStatus.OK);
    }
    @PutMapping("/api/products/update-quantity/{id}")
    public ResponseEntity<Product> updateQuantityToOrder(@PathVariable("id") int product_id, @RequestParam("quantity") int quantity){
        Product product = productService.updateQuantityToOrder(product_id, quantity);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }
    @DeleteMapping("/api/products/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable("id") int product_id){
        if(productService.deleteProduct(product_id) )
            return new ResponseEntity<>( HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}