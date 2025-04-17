package tn.esprit.springproject.Services;


import org.springframework.stereotype.Service;
import tn.esprit.springproject.Entities.Product;
import tn.esprit.springproject.Repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService implements IProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> getProductById(int id) {
        return productRepository.findById(id);
    }

    @Override
    public Product updateProduct(int id, Product productDetails) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setDesignation(productDetails.getDesignation());
            product.setPrix(productDetails.getPrix());
            product.setDiscount(productDetails.getDiscount());
            product.setTauxRemise(productDetails.getTauxRemise());
            product.setImage(productDetails.getImage());
            product.setArticle(productDetails.getArticle());
            product.setCategory(productDetails.getCategory());
            product.setMarque(productDetails.getMarque());
            return productRepository.save(product);
        } else {
            throw new RuntimeException("Product not found with id: " + id);
        }
    }

    @Override
    public void deleteProduct(int id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else {
            throw new RuntimeException("Product not found with id: " + id);
        }
    }
}