package esprit.productgestion.restcontroller;

import esprit.productgestion.Services.IProductService;
import esprit.productgestion.entity.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class productRestController {

    private final IProductService iProductService;
    private final String title = "Hello, I'm the PRODUCT Micro-Service";

    // Manually add the constructor
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
}