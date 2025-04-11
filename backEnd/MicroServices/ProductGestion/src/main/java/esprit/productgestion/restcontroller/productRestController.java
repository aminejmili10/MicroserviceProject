package esprit.productgestion.restcontroller;

import esprit.productgestion.Services.IProductService;
import esprit.productgestion.entity.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class productRestController {

    private final IProductService iProductService;
    private final String title = "Hello, I'm the PRODUCT Micro-Service";

    public productRestController(IProductService iProductService) {
        this.iProductService = iProductService;
    }

    @RequestMapping("/hello")
    public String sayHello() {
        System.out.println(title);
        return title;
    }


    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        Product createdProduct = iProductService.addProduct(product);
        return ResponseEntity.ok(createdProduct);
    }

    // Read all products
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = iProductService.getAllProducts();
        return ResponseEntity.ok(products);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable int id) {
        return iProductService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable int id, @RequestBody Product productDetails) {
        try {
            Product updatedProduct = iProductService.updateProduct(id, productDetails);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int id) {
        try {
            iProductService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}