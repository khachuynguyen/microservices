package orderservice.orderservice.DTO;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer id;
    private String userName;
    private String fullName;
    private String email;
    private String password;
    private String phone;
    private String role;
    private String address;

    public User(String userName, String fullName, String email, String password, String phone, String role, String address) {
        this.userName = userName;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = role;
        this.address = address;
    }
}
