package productservice.productservice.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import productservice.productservice.Models.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query(value = "select distinct manufacturer from product", nativeQuery = true)
    List<Object> getAllManufacturers();
    @Query(value = "select * from product where manufacturer = ?1", nativeQuery = true)
    List<Product> searchProduct(String manufacturer);
    @Query(value = "select * from product where manufacturer = :manufacturer and product_name like %:product_name%", nativeQuery = true)
    List<Product> searchProduct(@Param("manufacturer") String manufacturer, @Param("product_name") String find);
    @Query(value = "select * from product where product_name like %:product_name%", nativeQuery = true)
    List<Product> searchProductName(@Param("product_name")String productName);
}