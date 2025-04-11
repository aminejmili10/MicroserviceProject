package esprit.getway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient

public class GetwayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GetwayApplication.class, args);
    }

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder){
        return builder.routes()
                .route("user", r -> r.path("/user/**").uri("lb://user"))
                .route("commande", r -> r.path("/commande/**").uri("lb://commande"))
                .route("blog", r -> r.path("/blog/**").uri("lb://blog"))
                .route("demande", r -> r.path("/demande/**").uri("lb://demandeGestion"))
                .route("livraison", r -> r.path("/livraison/**").uri("lb://livraison"))
                .route("paiement", r -> r.path("/paiement/**").uri("lb://paiment"))
                .route("product", r -> r.path("/product/**").uri("lb://productGestion"))
                .route("userGestion", r -> r.path("/userGestion/**").uri("lb://userGestion"))
                .build();

    }
}
