package tn.esprit.springproject.Controllers;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.springproject.Entities.Financial;
import tn.esprit.springproject.Entities.Payment;
import tn.esprit.springproject.Repository.FinancialRepository;
import tn.esprit.springproject.Repository.PaymentRepository;
import tn.esprit.springproject.Services.IFinancialService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Tag(name="gestion financial")
@AllArgsConstructor
@RestController
@RequestMapping("/financial")
public class FinancialController {
    private final FinancialRepository financialRepository;
    private final PaymentRepository paymentRepository;
    IFinancialService financialService;
    @PostMapping("/createFinancial")


    @GetMapping("/getFinancial/{id}")

    public Financial GetFinancial(@PathVariable("id") Integer chId) {
        Financial  financial =financialService.getbyid(chId);
        return financial;
    }







    @GetMapping("/getAllFinancials")
    public ResponseEntity<List<Map<String, Object>>> getAllFinancials() {
        List<Financial> financials = financialService.getAllFinancials();
        List<Map<String, Object>> formattedFinancials = financials.stream().map(financial -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", financial.getId());
            map.put("amount", financial.getAmount());
            map.put("transactionDate", financial.getTransactionDate() != null ? financial.getTransactionDate().toString() : null);
            map.put("financialType", financial.getFinancialType() != null ? financial.getFinancialType().toString() : null);
            map.put("projectId", (financial.getProject() != null) ? financial.getProject().getId() : "N/A");
            return map;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(formattedFinancials);
    }


    @PutMapping("/modifyFinancial")
    public Financial modifyFinancial(@RequestBody Financial f) {
        Financial financial= financialService.modifyFinancial(f);
        return financial;
    }

    @PostMapping("/add/{projectId}")
    public ResponseEntity<?> addFinancialToProject(@PathVariable Integer projectId, @RequestBody Financial financial) {
        try {
            Financial newFinancial = financialService.addFinancialToProject(financial, projectId);
            return ResponseEntity.ok(newFinancial);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping("/project/{projectId}/financials")
    public ResponseEntity<List<Financial>> getFinancialsByProjectId(@PathVariable Integer projectId) {
        List<Financial> financials = financialService.getFinancialsByProjectId(projectId);
        if (financials.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(financials);
    }


    @DeleteMapping("/financial/delete/{id}")
    public ResponseEntity<Void> deleteFinancial(@PathVariable Integer id) {
        if (!financialRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        financialRepository.deleteById(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateFinancial(@PathVariable Integer id, @RequestBody Financial financial) {
        Optional<Financial> optionalFinancial = financialRepository.findById(id);

        if (optionalFinancial.isPresent()) {
            Financial existingFinancial = optionalFinancial.get();
            existingFinancial.setAmount(financial.getAmount());
            existingFinancial.setTransactionDate(financial.getTransactionDate());
            existingFinancial.setFinancialType(financial.getFinancialType());

            financialRepository.save(existingFinancial);
            return ResponseEntity.ok(existingFinancial);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ Financial ID " + id + " non trouvé !");
        }
    }
    @GetMapping("/{financialId}/payments")
    public ResponseEntity<List<Payment>> getPaymentsByFinancial(@PathVariable Integer financialId) {
        List<Payment> payments = paymentRepository.findByFinancialId(financialId);
        return ResponseEntity.ok(payments);
    }



}
