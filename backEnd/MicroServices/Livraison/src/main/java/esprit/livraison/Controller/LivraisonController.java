package esprit.livraison.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Livraison")
public class LivraisonController {
    private String title="Hello, i'm the  Livraison Micro-Service";
    @RequestMapping("/hello")
    public String sayHello() {
        System.out.println(title);
        return title;
    }
}
