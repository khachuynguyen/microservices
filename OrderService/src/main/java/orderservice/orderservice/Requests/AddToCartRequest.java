package orderservice.orderservice.Requests;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddToCartRequest {
    @Min(1)
    private int productId;

    @Min(1)
    private int quantity;

}
