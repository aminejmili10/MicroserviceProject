package esprit.paiment.sevice;

import esprit.paiment.entities.Payment;
import esprit.paiment.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PaymentService implements IPaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public Payment addPayment(Payment payment) {
        return paymentRepository.save(payment);
    }
    public void deletePayment(Integer idpayment) {
        paymentRepository.deleteById(idpayment);
    }
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

}
