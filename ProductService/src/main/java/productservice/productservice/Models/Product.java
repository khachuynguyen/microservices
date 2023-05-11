package productservice.productservice.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Column(unique = true)
    @Nationalized
    private String productName;
    @Column(columnDefinition = "LONGBLOB")
    private byte[] avatar;
    @Nationalized
    private String category;
    @Nationalized
    private String manufacturer;
    private int price;
    private int quantity;
    private int cost;
    private int percent;
    @Nationalized
    private String size;
    @Nationalized
    private String weight;
    @Nationalized
    private String tire;
    @Nationalized
    @Column(columnDefinition = "nvarchar(4000)")
    private String description;
}
