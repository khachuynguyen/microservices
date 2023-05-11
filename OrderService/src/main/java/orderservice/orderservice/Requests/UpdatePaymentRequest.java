package orderservice.orderservice.Requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdatePaymentRequest {
    @NotEmpty
    @NotBlank
    private String vnpBankTranNo;
    @NotEmpty
    @NotBlank
    private String vnpCardType;
}
