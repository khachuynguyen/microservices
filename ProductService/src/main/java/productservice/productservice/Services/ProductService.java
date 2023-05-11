package productservice.productservice.Services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import productservice.productservice.Advices.NotFoundException;
import productservice.productservice.Advices.SaveEntityFailed;
import productservice.productservice.Models.Product;
import productservice.productservice.Repositories.ProductRepository;
import productservice.productservice.Requests.CreateProductRequest;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class ProductService {
    private ProductRepository productRepository;
    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }
    public Product findProductById(int id){
        if(!productRepository.findById(id).isPresent())
            throw new NotFoundException("Not found Product by id = "+id);
        return productRepository.findById(id).get();
    }
    public Product saveProduct(CreateProductRequest productInformation){
        Product toSave = new Product();
        toSave.setProductName(productInformation.getProductName());
        toSave.setAvatar(productInformation.getAvatar());
        toSave.setCategory(productInformation.getCategory());
        toSave.setQuantity(productInformation.getQuantity());
        toSave.setManufacturer(productInformation.getManufacturer());
        toSave.setPrice(  ( productInformation.getCost() - (int)(productInformation.getCost()* productInformation.getPercent() / 100)  ));
        toSave.setCost(productInformation.getCost());
        toSave.setPercent(productInformation.getPercent());
        toSave.setSize(productInformation.getSize());
        toSave.setWeight(productInformation.getWeight());
        toSave.setTire(productInformation.getTire());
        toSave.setDescription(productInformation.getDescription());

        try{
            return productRepository.save(toSave);
        }catch (Exception exception){

            throw new SaveEntityFailed("Save product failed");
        }
    }

    public Product updateProduct(CreateProductRequest productInformation, int productId) {
        Product toSave = findProductById(productId);
        toSave.setProductName(productInformation.getProductName());
        toSave.setAvatar(productInformation.getAvatar());
        toSave.setQuantity(productInformation.getQuantity());
        toSave.setCategory(productInformation.getCategory());
        toSave.setManufacturer(productInformation.getManufacturer());
        toSave.setPrice(  ( productInformation.getCost() - (int)(productInformation.getCost()* productInformation.getPercent() / 100)  ));
        toSave.setCost(productInformation.getCost());
        toSave.setPercent(productInformation.getPercent());
        toSave.setSize(productInformation.getSize());
        toSave.setWeight(productInformation.getWeight());
        toSave.setTire(productInformation.getTire());
        toSave.setDescription(productInformation.getDescription());
        try{
            return productRepository.save(toSave);
        }catch (Exception exception){

            throw new SaveEntityFailed("Save product failed");
        }
    }

    public boolean deleteProduct(int productId) {
        Product found = findProductById(productId);
        try{
            productRepository.delete(found);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public List<Object>  getAllManufacturers() {
        return productRepository.getAllManufacturers();
    }

    public List<Product> searchProduct(Map<String, String> allParams) {
        List<Product> list=null;
        if(allParams.containsKey("manufacturer")){
            if(!allParams.containsKey("find"))
                list = productRepository.searchProduct(allParams.get("manufacturer"));
            else
                list = productRepository.searchProduct(allParams.get("manufacturer"), allParams.get("find"));
        }else {
            if(allParams.containsKey("find"))
                list = productRepository.searchProductName(allParams.get("find"));
        }
        return list;
    }

    public Product updateQuantityToOrder(int productId, int quantity) {
        Product toUpdate = findProductById(productId);
        if(toUpdate.getQuantity() - quantity < 0)
            throw new SaveEntityFailed("Quantity is not enough to order");
        toUpdate.setQuantity(toUpdate.getQuantity() - quantity);
        productRepository.save(toUpdate);
        return toUpdate;
    }
}