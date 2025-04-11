package esprit.paiment.restcontroller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/paiment")
public class paimentRestController {

    private String title="Hello, i'm the  paiment Micro-Service";
    @RequestMapping("/hello")
    public String sayHello() {
        System.out.println(title);
        return title;
    }
}
