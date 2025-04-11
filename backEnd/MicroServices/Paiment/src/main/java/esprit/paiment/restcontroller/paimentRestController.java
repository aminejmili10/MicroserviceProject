package esprit.paiment.restcontroller;

import esprit.paiment.entities.Payment;
import esprit.paiment.repository.PaymentRepository;
import esprit.paiment.sevice.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/paiment")
public class paimentRestController {

    @Autowired
    private PaymentService paymentService;
    private PaymentRepository paymentRepository;

    @PostMapping("/add")
    public Payment addPayment(@RequestBody Payment payment) {
        return paymentService.addPayment(payment);
    }
    @DeleteMapping("/deletePayment/{payment-id}")
    public ResponseEntity<Void> deletePayment(@PathVariable("payment-id") Integer chId) {
        paymentService.deletePayment(chId);
        return ResponseEntity.noContent().build(); // Explicit 204 No Content
    }
    @GetMapping("/getAllPayments")
    public List<Payment> getAllPayments() {
        return paymentService.getAllPayments();
    }

}
