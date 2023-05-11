package orderservice.orderservice.Requests;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOderRequest {
    @NotEmpty
    private List<Integer> listProduct;
    private String address;
}
