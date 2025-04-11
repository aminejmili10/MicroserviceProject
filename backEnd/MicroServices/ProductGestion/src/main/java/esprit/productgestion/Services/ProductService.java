package esprit.productgestion.Services;

import esprit.productgestion.Repository.ProductRepository;
import esprit.productgestion.entity.Product;
import org.springframework.stereotype.Service;

@Service
public class ProductService implements IProductService {

    private final ProductRepository productRepository;

    // Manually add the constructor
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }
}