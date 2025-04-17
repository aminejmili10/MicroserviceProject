package tn.esprit.springproject.Controllers;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.springproject.Entities.Invoice;
import tn.esprit.springproject.Entities.InvoiceType;
import tn.esprit.springproject.Services.IInvoiceService;
import tn.esprit.springproject.Services.IPaymentService;

import java.time.LocalDate;
import java.util.List;

@Tag(name="gestion invoice")
@RestController
@AllArgsConstructor
@RequestMapping("/invoice")
public class InvoiceController {
        IInvoiceService invoiceService;
        IPaymentService paymentService;
        @PostMapping("/createInvoice")
        public Invoice createInvoice(@RequestBody Invoice invoice) {
                return invoiceService.createInvoice(invoice);
        }


        @GetMapping("/getAllInvoices")
        public List<Invoice> getAllInvoices() {
                return invoiceService.getAllInvoices();
        }

        @GetMapping("/getInvoice/{id}")
        public Invoice GetInvoice(@PathVariable("id") Integer chId) {
                Invoice  invoice = invoiceService.getbyid(chId);
                return invoice;
        }

        @PutMapping("/modifyInvoice")
        public Invoice modifyInvoice(@RequestBody Invoice i) {
                Invoice invoice= invoiceService.modifyInvoice(i);
                return invoice;
        }
        @DeleteMapping("/deleteInvoice/{invoice-id}")
        public void deleteInvoice(@PathVariable("invoice-id") Integer chId) {
               invoiceService.deleteInvoice(chId);
        }
       /* @PostMapping("/createWithPayment/{amountDue}")
        public ResponseEntity<?> createInvoiceWithPayment(
                @RequestBody Payment payment,
                @PathVariable Double amountDue) {

                try {
                        Invoice newInvoice = invoiceService.createInvoiceWithPayment(payment, amountDue);
                        return ResponseEntity.ok(newInvoice);
                } catch (Exception e) {
                        return ResponseEntity.badRequest().body("Erreur : " + e.getMessage());
                }
        }

        */
       @PostMapping("/generateForPayment/{paymentId}/{amountDue}")
       public ResponseEntity<?> generateInvoiceForExistingPayment(
               @PathVariable Integer paymentId,
               @PathVariable Double amountDue) {

               try {
                       Invoice newInvoice = invoiceService.generateInvoiceForExistingPayment(paymentId, amountDue);
                       return ResponseEntity.ok(newInvoice);
               } catch (Exception e) {
                       return ResponseEntity.badRequest().body("Erreur : " + e.getMessage());
               }
       }

       /* @PostMapping("/generateInvoice/{paymentId}")
        public ResponseEntity<?> generateInvoice(@PathVariable Integer paymentId) {
                Payment payment = paymentService.getPaymentById(paymentId);
                if (payment == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Paiement introuvable.");
                }

                // ✅ Vérifier que le paiement est "COMPLETED"
                if (!"COMPLETED".equals(payment.getPaymentStatus())) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body("La facture ne peut être générée que pour les paiements complétés.");
                }

                byte[] pdf = invoiceService.generateInvoice(payment);
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_PDF)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=facture.pdf")
                        .body(pdf);
        }



        */
       @PostMapping("/quote")
       public ResponseEntity<Invoice> createQuote(@RequestBody Invoice invoice) {
               invoice.setType(InvoiceType.QUOTE);
               invoice.setValidated(false);
               invoice.setDate(LocalDate.now());
               return ResponseEntity.ok(invoiceService.save(invoice));
       }

        @GetMapping("/quotes")
        public ResponseEntity<List<Invoice>> getAllQuotes() {
                return ResponseEntity.ok(invoiceService.getQuotes());
        }
        @PutMapping("/validate/{id}")
        public ResponseEntity<Invoice> validateQuote(@PathVariable Integer id) {
                return ResponseEntity.ok(invoiceService.validateQuote(id));
        }
    @GetMapping("/generate-and-send/{invoiceId}")
    public ResponseEntity<String> generateAndSendInvoice(@PathVariable Integer invoiceId) {
        try {
            String result = invoiceService.generateAndSendInvoicePdf(invoiceId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur : " + e.getMessage());
        }
    }

}
