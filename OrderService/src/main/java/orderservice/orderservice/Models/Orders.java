package orderservice.orderservice.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String paymentMethods;
    private double total;
    private int userId;
    private String tradingCode;
    private String address;
    private boolean isPayment;
    private boolean isTransported;
    private int isSuccess;

    public Orders(String paymentMethods, double total, int userId, String tradingCode, boolean isPayment, boolean isTransported, int isSuccess) {
        this.paymentMethods = paymentMethods;
        this.total = total;
        this.userId = userId;
        this.tradingCode = tradingCode;
        this.isPayment = isPayment;
        this.isTransported = isTransported;
        this.isSuccess = isSuccess;
    }
}
