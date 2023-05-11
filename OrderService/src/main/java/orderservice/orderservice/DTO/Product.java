package orderservice.orderservice.DTO;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private int id;

    public Product(String productName, byte[] avatar, String category, String manufacturer, int price, int cost, int percent, String size, String weight, String tire, String description) {
        this.productName = productName;
        this.avatar = avatar;
        this.category = category;
        this.manufacturer = manufacturer;
        this.price = price;
        this.cost = cost;
        this.percent = percent;
        this.size = size;
        this.weight = weight;
        this.tire = tire;
        this.description = description;
    }

    private String productName;

    private byte[] avatar;

    private String category;

    private String manufacturer;
    private int price;
    private int quantity;
    private int cost;
    private int percent;
    private String size;
    private String weight;
    private String tire;
    private String description;
}
