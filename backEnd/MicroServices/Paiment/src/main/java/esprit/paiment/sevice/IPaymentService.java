package esprit.paiment.sevice;

import esprit.paiment.entities.Payment;

import java.util.List;

public interface IPaymentService {
    Payment addPayment(Payment payment);
    void deletePayment(Integer idpayment);
    List<Payment> getAllPayments();
}
