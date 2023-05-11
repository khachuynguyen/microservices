package orderservice.orderservice.Controllers;


import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import orderservice.orderservice.DTO.GetTokenFromBearToken;
import orderservice.orderservice.DTO.User;
import orderservice.orderservice.FeignClient.UserClient;
import orderservice.orderservice.Models.OrderDetail;
import orderservice.orderservice.Models.Orders;
import orderservice.orderservice.Requests.AcceptDeclineOrderRequest;
import orderservice.orderservice.Requests.CreateOderRequest;
import orderservice.orderservice.Requests.UpdatePaymentRequest;
import orderservice.orderservice.Services.OrderService;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@CrossOrigin
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private GetTokenFromBearToken jwt;
    @Autowired
    private UserClient userClient;
//    @Autowired
//    private OrderDetailService orderDetailService;
//    @Autowired
//    private UserService userService;
    @PostMapping("/api/orders")
    public ResponseEntity<Object> doOder(@RequestBody @Valid CreateOderRequest listProduct, @RequestHeader("Authorization") String token){
//        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        com.banxedap.cdio3.Entities.User userOrder = userService.getUserByUserName(user.getUsername());
        int userId  = jwt.getUserId(token);
        User user = userClient.getUserById(userId, token);
        Orders orders = orderService.createOrder(user, listProduct);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
    @GetMapping("/api/admin/orders")
    public ResponseEntity<Object> getAllOrders(){
        List<Orders>  orders = orderService.getAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
    @PutMapping("/api/admin/orders/{id}")
    public ResponseEntity<Object> doAcceptDeclineOrder(@PathVariable("id") int orderId,@RequestBody @Valid AcceptDeclineOrderRequest request){
        Orders orders = orderService.doAcceptDecline(orderId, request.getIsSuccess());
        return new ResponseEntity<>(request, HttpStatus.OK);
    }

    @GetMapping("/api/orders")
    public ResponseEntity<Object> getOrderByUserId(@RequestHeader("Authorization") String token){
//        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        com.banxedap.cdio3.Entities.User userOrder = userService.getUserByUserName(user.getUsername());
        int userId = jwt.getUserId(token);
        User userOrder = userClient.getUserById(userId, token);
        List<Orders> orders = orderService.getOrderOfUser(userOrder);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
    @GetMapping("/api/orders/{id}")
    public ResponseEntity<Object> getOrderDetailByOrderId(@PathVariable("id") int orderId, @RequestHeader("Authorization") String token){
//        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        com.banxedap.cdio3.Entities.User userOrder = userService.getUserByUserName(user.getUsername());
        User userOrder = userClient.getUserById(jwt.getUserId(token), token);
        List<OrderDetail> orders = orderService.getOrderDetailOfUser( orderId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
    @PutMapping("/api/orders/{id}")
    public ResponseEntity<Object> updateOrder(@PathVariable("id") int orderId, @Valid @RequestBody UpdatePaymentRequest updatePaymentRequest){
        Orders orders =  orderService.updateOrder(orderId, updatePaymentRequest.getVnpCardType(), updatePaymentRequest.getVnpBankTranNo());
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PostMapping("/api/orders/{id}")
    public ResponseEntity<Object> createPayment(@PathVariable("id") int orderId, @Nullable @RequestBody String returnUrl){
        try{
            Orders orders = orderService.getOrderById(orderId);
            String vnp_Version = "2.1.0";
            String vnp_Command = "pay";
            String vnp_OrderInfo = "Thanh+toan";
            String orderType = "billpayment";
            String vnp_TxnRef = "DONHANG"+orders.getId();
            String vnp_IpAddr = "127.0.0.1";
            String vnp_TmnCode = "KWPCU4B6";
            int amount = ((int)orders.getTotal()) * 100;
            Map vnp_Params = new HashMap();
            vnp_Params.put("vnp_Version", vnp_Version);
            vnp_Params.put("vnp_Command", vnp_Command);
            vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
            vnp_Params.put("vnp_Amount", String.valueOf(amount));
            vnp_Params.put("vnp_CurrCode", "VND");
            String bank_code = "";
            if (bank_code != null && !bank_code.isEmpty()) {
                vnp_Params.put("vnp_BankCode", bank_code);
            }
            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
            vnp_Params.put("vnp_OrderType", orderType);
            String locate = "vn";
            if (locate != null && !locate.isEmpty()) {
                vnp_Params.put("vnp_Locale", locate);
            } else {
                vnp_Params.put("vnp_Locale", "vn");
            }
            String vnp_Returnurl = "http://localhost:3000/success";
            vnp_Params.put("vnp_ReturnUrl", vnp_Returnurl);
            vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String vnp_CreateDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
            cld.add(Calendar.MINUTE, 15);
            String vnp_ExpireDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
            List fieldNames = new ArrayList(vnp_Params.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            Iterator itr = fieldNames.iterator();
            Mac sha512Hmac;
            while (itr.hasNext()) {
                String fieldName = (String) itr.next();
                String fieldValue = (String) vnp_Params.get(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    //Build hash data
                    hashData.append(fieldName);
                    hashData.append('=');
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    //Build query
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    if (itr.hasNext()) {
                        query.append('&');
                        hashData.append('&');
                    }
                }
            }
            String queryUrl = query.toString();
            String vnp_SecureHash = new HmacUtils(HmacAlgorithms.HMAC_SHA_512, "SWXZDCSBGTFJSQPMCXEQGIUSTDJITQFL").hmacHex(hashData.toString());
            queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
            String paymentUrl ="https://sandbox.vnpayment.vn/paymentv2/vpcpay.html" + "?" + queryUrl;
            return new ResponseEntity<>(paymentUrl,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("vpnUrl",HttpStatus.OK);
        }

    }
}
