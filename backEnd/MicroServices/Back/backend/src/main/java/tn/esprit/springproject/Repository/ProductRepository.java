package tn.esprit.springproject.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.springproject.Entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
}