package orderservice.orderservice.Services;

import orderservice.orderservice.Advices.NotFoundException;
import orderservice.orderservice.Advices.SaveEntityFailed;
import orderservice.orderservice.DTO.Product;
import orderservice.orderservice.DTO.User;
import orderservice.orderservice.FeignClient.ProductClient;
import orderservice.orderservice.Models.Carts;
import orderservice.orderservice.Models.OrderDetail;
import orderservice.orderservice.Models.Orders;
import orderservice.orderservice.Repositories.OrderDetailRepository;
import orderservice.orderservice.Repositories.OrderRepository;
import orderservice.orderservice.Requests.CreateOderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
//    @Autowired
//    private ProductRepository productRepository;
    @Autowired
    private ProductClient productClient;
    @Autowired
    private CartService cartService;
//    @Autowired
//    private ProductService productService;
    private boolean checkQuantity(Carts cart, Product product){
        if( product.getQuantity() - cart.getQuantity() < 0)
            return false;
        return true;
    }
    public Orders getOrderById(int id){
        if(orderRepository.findById(id).isEmpty())
            return null;
        return orderRepository.findById(id).get();
    }
    @Transactional(rollbackFor = {Exception.class, Throwable.class})
    public Orders createOrder(User userOrder, CreateOderRequest listProduct) {
        Orders orders = new Orders();
        orders.setTotal(0);
        orders.setUserId(userOrder.getId());
        orders.setAddress(userOrder.getAddress());
        orders.setPayment(false);
        orders.setTransported(false);
        orders.setIsSuccess(0);
        orders.setPaymentMethods("offline");
        orderRepository.save(orders);
        for (int productId: listProduct.getListProduct()
             ) {
            Product found = productClient.getProduct(productId);
            Carts cartFound = cartService.findByUserIdAndProductId(userOrder.getId(), productId);
            if(cartFound == null)
                throw new NotFoundException("Not found cart");
            if(!checkQuantity(cartFound, found))
                throw new SaveEntityFailed("Số lượng sản phẩm "+found.getProductName() + " không đủ");
            OrderDetail detail = new OrderDetail(cartFound, found.getAvatar(), orders.getId(), found.getProductName(), found.getId());
            detail.setUserName(userOrder.getUserName());
            cartService.deleteCartByProductId(productId, userOrder.getId());
            orderDetailRepository.save(detail);
//            found.setQuantity(found.getQuantity() - cartFound.getQuantity());
//            productRepository.save(found);
            productClient.updateQuantityToOrder(found.getId(), cartFound.getQuantity());
            orders.setTotal(orders.getTotal() + detail.getTotal());
        }
        orderRepository.save(orders);
        return orders;
    }

    public Orders updateOrder(int orderId, String vnpCardType, String vnpBankTranNo) {
        Orders found = orderRepository.findById(orderId).get();
        found.setPaymentMethods(vnpCardType);
        found.setIsSuccess(1);
        found.setTradingCode(vnpBankTranNo);
        found.setPayment(true);
        return orderRepository.save(found);
    }

    public List<Orders> getOrderOfUser(User userOrder) {
        return orderRepository.getAllOrderByUserId(userOrder.getId());
    }

    public List<OrderDetail> getOrderDetailOfUser(int orderId) {
        return orderDetailRepository.findAllByOrderId(orderId);
    }

    public List<Orders>  getAllOrders() {
        return orderRepository.findAll(Sort.by("id").descending());
    }

    public Orders doAcceptDecline(int orderId, int isSuccess) {
        Orders orders = orderRepository.findById(orderId).get();
        if(orders == null)
            throw  new NotFoundException("Not found product");
        orders.setIsSuccess(isSuccess);
        return orderRepository.save(orders);
    }
}
