package productservice.productservice.Requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateProductRequest {
    @NotNull
    @NotEmpty
    private String productName;
    @NotNull
    @NotEmpty
    private String category;
    @NotNull
    private byte[] avatar;
    @NotNull
    @NotEmpty
    private String manufacturer;
    @NotNull
    private int cost;
    @NotNull
    private int quantity;
    @NotNull
    private int percent;
    @NotNull
    @NotEmpty
    private String size;
    @NotNull
    @NotEmpty
    private String weight;
    @NotNull
    @NotEmpty
    private String tire;
    @NotNull
    @NotEmpty
    private String description;
}