package orderservice.orderservice.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import orderservice.orderservice.Models.CustomPrimaryKey.CartId;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Carts {
    @Id
    private CartId cartId;
    @Column(nullable = true)
    private String description;
    @Column(columnDefinition = "LONGBLOB")
    private byte[] avatar;
    private int price;
    private int isPossibleToOrder;
    private int quantity;
    private double total;
}
