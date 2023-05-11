package orderservice.orderservice.Requests;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AcceptDeclineOrderRequest {
    @NotNull
    private int isSuccess;
}
