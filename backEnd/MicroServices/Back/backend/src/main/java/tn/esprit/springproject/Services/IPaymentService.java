package tn.esprit.springproject.Services;

import tn.esprit.springproject.Entities.Financial;
import tn.esprit.springproject.Entities.FinancialType;
import tn.esprit.springproject.Entities.Payment;

import java.util.List;
import java.util.Map;

public interface IPaymentService {

    List<Payment> getAllPayments();
    Payment getbyid(Integer paymentId) ;
    Payment modifyPayment(Payment payment);
    void deletePayment(Integer idpayment);
    List<Payment> getPaymentsByFinancialType(FinancialType financialType);
    Payment createPaymentWithFinancialType(Payment payment, FinancialType financialType);
    List<Payment> getPaymentsByUserId(Long userId);
    Payment createPayment(Payment payment, Integer userId);
    double calculateLateInterest(Financial financial, double annualInterestRate);
    Payment getPaymentById(Integer id);
    Payment updatePayment(Payment payment);
    List<Payment> getPaymentsByFinancial(Integer financialId);
    //Payment processLatePayment(Payment payment, double annualInterestRate);
    //double calculateLateInterest(double unpaidAmount, double annualRate, Date dueDate);
    //double getTotalPaymentsByFinancialId(Integer financialId);
    void sendPaymentReminders();
    void updateLatePayments();
    //PaymentStats generatePaymentStats();
    //Payment completePayment(Integer paymentId);
   // void sendPaymentConfirmationEmail(String to, Payment payment);
    //void sendEmail(String to, String subject, String body) throws MessagingException;
   // Payment completePayment(Integer paymentId);
    void sendPaymentConfirmation(Payment payment);
    Map<String, Object> getPaymentStatistics();
    }

