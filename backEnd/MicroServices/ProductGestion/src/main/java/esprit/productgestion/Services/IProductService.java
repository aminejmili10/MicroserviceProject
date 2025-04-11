package esprit.productgestion.Services;

import esprit.productgestion.entity.Product;

import java.util.List;
import java.util.Optional;

public interface IProductService {
    Product addProduct(Product product); // Create
    List<Product> getAllProducts(); // Read (all)
    Optional<Product> getProductById(int id); // Read (by ID)
    Product updateProduct(int id, Product productDetails); // Update
    void deleteProduct(int id); // Delete
}