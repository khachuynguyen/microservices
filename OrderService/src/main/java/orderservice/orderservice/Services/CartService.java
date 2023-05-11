package orderservice.orderservice.Services;


import jakarta.ws.rs.BadRequestException;
import orderservice.orderservice.Advices.NotFoundException;
import orderservice.orderservice.DTO.CartDTO;
import orderservice.orderservice.DTO.Product;
import orderservice.orderservice.FeignClient.ProductClient;
import orderservice.orderservice.Models.Carts;
import orderservice.orderservice.Models.CustomPrimaryKey.CartId;
import orderservice.orderservice.Repositories.CartRepository;
import orderservice.orderservice.Requests.AddToCartRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductClient productClient;

    //    @Autowired
//    private ProductService productService;
//    @Autowired
//    private UserService userService;
    public Carts addToCartRequest(AddToCartRequest request, int userId, Product found) {
//        int userId = principal.getId();
//        Product found = product;
        if (found == null)
            throw new NotFoundException("Not found Product");
        if (found.getQuantity() - request.getQuantity() < 0)
            throw new BadRequestException("Quantity is not enough");
        Optional<Carts> checkExist = cartRepository.findById(new CartId(userId, found.getId()));
        if (checkExist.isEmpty()) {
            Carts carts = new Carts();
            carts.setAvatar(found.getAvatar());
            carts.setCartId(new CartId(userId, found.getId()));
            carts.setPrice(found.getPrice());
            carts.setIsPossibleToOrder(1);
            carts.setTotal((int) request.getQuantity() * found.getPrice());
            carts.setQuantity(request.getQuantity());
            return cartRepository.save(carts);
        } else {
            Carts tmp = checkExist.get();
            tmp.setQuantity(request.getQuantity());
            tmp.setPrice(found.getPrice());
            tmp.setTotal((int) request.getQuantity() * found.getPrice());
            return cartRepository.save(tmp);
        }
//
//
    }

    public List<CartDTO> getAllCartsOfUser(int id) {
        List<CartDTO> list = new ArrayList<>();
        List<Carts> carts = cartRepository.findCartsByUserId(id);
        for (Carts cart :
                carts) {
            Product found = productClient.getProduct(cart.getCartId().getProductId());
            list.add(new CartDTO(cart.getCartId(), found, cart.getQuantity(), (int) cart.getTotal()));
        }
        return list;
    }
    public Carts findByUserIdAndProductId(int userId, int productId){
        return cartRepository.findByUserIdAndProductId(userId, productId);
    }
    public boolean deleteCartByProductId(int productId,int userId) {
        Product found=null;
        try{
           found =  productClient.getProduct(productId);
        }catch (Exception exception){
            return false;
        }
        Carts cart =  findByUserIdAndProductId(userId, productId) ;
        if(cart == null)
            throw new NotFoundException("Not found carts");
        cartRepository.delete(cart);
        if(findByUserIdAndProductId(userId, productId) == null)
            return true;
        return false;
    }
}
