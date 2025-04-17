package tn.esprit.springproject.Controllers;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import tn.esprit.springproject.Entities.FinancialType;
import tn.esprit.springproject.Entities.Payment;
import tn.esprit.springproject.Entities.PaymentStatus;
import tn.esprit.springproject.Repository.PaymentRepository;
import tn.esprit.springproject.Services.IPaymentService;

import java.util.*;
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "gestion payment")
@RequiredArgsConstructor
@RestController
@RequestMapping("/payment")
public class PaymentController {
    private final PaymentRepository paymentRepository;
    private final IPaymentService paymentService;
    private final JavaMailSender mailSender;
    @Value("${stripe.secret.key}")
    private String stripeSecretKey;
    @PostMapping("/createPayment")
    public ResponseEntity<?> createPayment(@RequestBody Payment payment) {
        try {
            System.out.println("Données reçues : " + payment);
            Payment newPayment = paymentService.createPayment(payment,1);
            return ResponseEntity.ok(newPayment);
        } catch (RuntimeException e) {
            System.out.println("Erreur : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/getAllPayments")
    public List<Payment> getAllPayments() {
        return paymentService.getAllPayments();
    }

    @GetMapping("/getPayment/{id}")
    public Payment GetPayment(@PathVariable("id") Integer chId) {
        return paymentService.getbyid(chId);
    }

    @PutMapping("/modifyPayment")
    public Payment modifyPayment(@RequestBody Payment p) {
        return paymentService.modifyPayment(p);
    }

    @DeleteMapping("/deletePayment/{payment-id}")
    public ResponseEntity<Void> deletePayment(@PathVariable("payment-id") Integer chId) {
        paymentService.deletePayment(chId);
        return ResponseEntity.noContent().build(); // Explicit 204 No Content
    }

    @GetMapping("afficher/by-financial-type/{type}")
    public List<Payment> getPaymentsByFinancialType(@PathVariable FinancialType type) {
        return paymentService.getPaymentsByFinancialType(type);
    }

    @PostMapping("/create/{financialType}")
    public ResponseEntity<?> createPaymentWithFinancialType(@PathVariable FinancialType financialType, @RequestBody Payment payment) {
        try {
            Payment savedPayment = paymentService.createPaymentWithFinancialType(payment, financialType);
            return ResponseEntity.ok(savedPayment);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/history/user/{userId}")
    public ResponseEntity<List<Payment>> getPaymentsByUser(@PathVariable Long userId) {
        List<Payment> payments = paymentService.getPaymentsByUserId(userId);
        return ResponseEntity.ok(payments != null ? payments : new ArrayList<>());
    }

    @PostMapping("/create/user/{userId}")
    public ResponseEntity<?> createPayment(@PathVariable Integer userId, @RequestBody Payment payment) {
        try {
            System.out.println("Requête reçue - UserID : " + userId);
            System.out.println("Données reçues : " + payment);
            Payment newPayment = paymentService.createPayment(payment, userId);
            return ResponseEntity.ok(newPayment);
        } catch (RuntimeException e) {
            System.out.println("Erreur : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/update/status/{id}")
    public ResponseEntity<?> updatePaymentStatus(@PathVariable Integer id, @RequestBody Payment paymentDetails) {
        Optional<Payment> optionalPayment = paymentRepository.findById(id);
        if (!optionalPayment.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Payment not found");
        }
        Payment existingPayment = optionalPayment.get();
        existingPayment.setPaymentStatus(paymentDetails.getPaymentStatus());
        paymentRepository.save(existingPayment);
        return ResponseEntity.ok(existingPayment);
    }

    @GetMapping("/financial/{financialId}/payments")
    public ResponseEntity<List<Payment>> getPaymentsByFinancial(@PathVariable Integer financialId) {
        List<Payment> payments = paymentService.getPaymentsByFinancial(financialId);
        if (payments.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(payments);
        }
        return ResponseEntity.ok(payments);
    }

    @PutMapping("/payment/update/{id}")
    public ResponseEntity<?> updatePayment(@PathVariable Integer id, @RequestBody Payment payment) {
        Optional<Payment> optionalPayment = paymentRepository.findById(id);
        if (optionalPayment.isPresent()) {
            Payment existingPayment = optionalPayment.get();
            existingPayment.setPaymentStatus(payment.getPaymentStatus());
            existingPayment.setPaymentAmount(payment.getPaymentAmount());
            existingPayment.setPaymentDate(payment.getPaymentDate());
            existingPayment.setPaymentMethod(payment.getPaymentMethod());
            paymentRepository.save(existingPayment);
            return ResponseEntity.ok(existingPayment);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Paiement non trouvé !");
        }
    }
    @PostMapping("/create-checkout-session/{paymentId}")
    public ResponseEntity<Map<String, String>> createCheckoutSession(@PathVariable Integer paymentId) {
        try {
            Optional<Payment> optionalPayment = paymentRepository.findById(paymentId);
            if (!optionalPayment.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Payment not found"));
            }

            Payment payment = optionalPayment.get();
            Stripe.apiKey = stripeSecretKey;


            Integer projectId = payment.getFinancial().getProject().getId();

            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl("http://localhost:4200/client/success?paymentId=" + paymentId + "&projectId=" + projectId)
                    .setCancelUrl("http://localhost:4200/client/cancel")
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency("eur")
                                                    .setUnitAmount((long) (payment.getPaymentAmount() * 100))
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName("Payment for Financial ID: " + payment.getFinancial().getId())
                                                                    .build()
                                                    )
                                                    .build()
                                    )
                                    .setQuantity(1L)
                                    .build()
                    )
                    .build();

            Session session = Session.create(params);
            Map<String, String> response = new HashMap<>();
            response.put("url", session.getUrl());

            return ResponseEntity.ok(response);
        } catch (StripeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/complete-payment/{paymentId}")
    public ResponseEntity<?> completePayment(@PathVariable Integer paymentId) {
        Optional<Payment> optionalPayment = paymentRepository.findById(paymentId);
        if (!optionalPayment.isPresent()) {
            System.out.println("Payment not found for ID: " + paymentId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Payment not found");
        }

        Payment payment = optionalPayment.get();
        payment.setPaymentStatus(PaymentStatus.COMPLETED);
        paymentRepository.save(payment);
        System.out.println("Payment status updated to COMPLETED for ID: " + paymentId); // Log success
        paymentService.sendPaymentConfirmation(payment);
        return ResponseEntity.ok(payment);
    }
    @GetMapping("/test-reminders")
    public ResponseEntity<String> testPaymentReminders() {
        paymentService.sendPaymentReminders();
        return ResponseEntity.ok("Payment reminders triggered");
    }
    @GetMapping("/test-email")
    public ResponseEntity<String> testEmail() {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("ouennicheranim@gmail.com");
            message.setSubject("Test Email");
            message.setText("This is a test email from PaymentService.");
            message.setFrom("tbuilderscompro@gmail.com");
            mailSender.send(message);
            System.out.println("Test email sent successfully"); // Use System.out for simplicity
            return ResponseEntity.ok("Test email sent");
        } catch (Exception e) {
            System.out.println("Failed to send test email: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed: " + e.getMessage());
        }
    }
    @PostMapping("/update-late-payments")
    public ResponseEntity<String> triggerLatePaymentsUpdate() {
        paymentService.updateLatePayments();
        return ResponseEntity.ok("Mise à jour des paiements en retard déclenchée avec succès");
    }
    @PostMapping("/trigger-late-update")
    public ResponseEntity<String> triggerLateUpdate() {
        paymentService.updateLatePayments();
        return ResponseEntity.ok("Mise à jour des paiements en retard déclenchée");
    }



    @GetMapping("/test-email/{email}")
    public ResponseEntity<String> testEmail(@PathVariable String email) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Test Email");
            message.setText("Ceci est un test d'email depuis votre application Spring Boot.");
            message.setFrom("votre-email@gmail.com");

            mailSender.send(message);
            return ResponseEntity.ok("Email envoyé avec succès !");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur : " + e.getMessage());
        }
    }
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        return ResponseEntity.ok(paymentService.getPaymentStatistics());
    }
}


