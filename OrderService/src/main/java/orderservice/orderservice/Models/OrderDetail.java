package orderservice.orderservice.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int productId;
    private int orderId;
    private String productName;
    private String userName;
    @Column(columnDefinition = "LONGBLOB")
    private byte[] avatar;
    private int price;
    private int quantity;
    private int total;

    public OrderDetail(int productId, int orderId, String productName, byte[] avatar, int price, int quantity, int total) {
        this.productId = productId;
        this.orderId = orderId;
        this.productName = productName;
        this.avatar = avatar;
        this.price = price;
        this.quantity = quantity;
        this.total = total;
    }

    public OrderDetail(Carts carts, byte[] avatar, int id, String productName, int foundId) {
        this.avatar  = avatar;
        this.orderId = id;
        this.productId = foundId;
        this.productName = productName;
        this.price = carts.getPrice();
        this.quantity = carts.getQuantity();
        this.total = (int) carts.getTotal();
    }
}
