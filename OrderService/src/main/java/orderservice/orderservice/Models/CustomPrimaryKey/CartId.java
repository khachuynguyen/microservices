package orderservice.orderservice.Models.CustomPrimaryKey;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CartId implements Serializable {
    private int userId;
    private int productId;
}
