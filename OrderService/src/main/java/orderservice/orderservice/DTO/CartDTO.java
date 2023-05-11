package orderservice.orderservice.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import orderservice.orderservice.Models.CustomPrimaryKey.CartId;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartDTO {
    private int productId;
    private int userId;
    private String productName;
    private byte[] avatar;
    private int quantity;
    private int price;
    private int total;

    public CartDTO(CartId cartId, Product product, int quantity, int total) {
        this.productId = cartId.getProductId();
        this.productName = product.getProductName();
        this.userId = cartId.getUserId();
        this.avatar =product.getAvatar() ;
        this.quantity = quantity;
        this.price = product.getPrice();
        this.total = total;
    }
}
