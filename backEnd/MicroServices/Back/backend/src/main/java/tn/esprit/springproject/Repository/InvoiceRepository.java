package tn.esprit.springproject.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.springproject.Entities.Invoice;
import tn.esprit.springproject.Entities.InvoiceType;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
    List<Invoice> findByType(InvoiceType type);
    List<Invoice> findByTypeAndValidated(InvoiceType type, boolean validated);
}
