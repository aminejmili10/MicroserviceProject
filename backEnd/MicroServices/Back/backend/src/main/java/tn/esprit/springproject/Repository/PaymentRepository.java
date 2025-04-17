package tn.esprit.springproject.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.springproject.Entities.FinancialType;
import tn.esprit.springproject.Entities.Payment;
import tn.esprit.springproject.Entities.PaymentStatus;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    List<Payment> findByFinancial_FinancialType(FinancialType financialType);
    @Query("SELECT p FROM Payment p WHERE p.financial IN " +
            "(SELECT f FROM Financial f WHERE f.project IN " +
            "(SELECT pr FROM Project pr WHERE pr.user.id = :userId))")
    List<Payment> findPaymentsByUserId(@Param("userId") Long userId);
    List<Payment> findByFinancialId(Integer financialId);
    @Query("SELECT COALESCE(SUM(p.paymentAmount), 0) FROM Payment p WHERE p.financial.id = :financialId")
    double getTotalPaymentsByFinancialId(@Param("financialId") Integer financialId);
    List<Payment> findByPaymentStatusNot(PaymentStatus status);
    long countByPaymentStatus(PaymentStatus status);

    @Query("SELECT COALESCE(SUM(p.paymentAmount), 0) FROM Payment p WHERE p.paymentStatus = :status")
    double sumPaymentAmountByStatus(@Param("status") PaymentStatus status);
    List<Payment> findByInvoice_Id(Integer invoiceId);

}
