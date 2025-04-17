package tn.esprit.springproject.Services;

import jakarta.mail.MessagingException;
import tn.esprit.springproject.Entities.Invoice;

import java.io.IOException;
import java.util.List;

public interface IInvoiceService {

    Invoice createInvoice(Invoice invoice);
    List<Invoice> getAllInvoices();
    Invoice getbyid(Integer invoiceId) ;
    Invoice modifyInvoice(Invoice invoice);
    void deleteInvoice(Integer idinvoice);
   // Invoice createInvoiceWithPayment(Payment payment, Double amountDueFromUser);
    Invoice generateInvoiceForExistingPayment(Integer paymentId, Double amountDueFromUser);
    Invoice validateQuote(Integer id);
    List<Invoice> getQuotes();
    Invoice save(Invoice invoice);
    String generateAndSendInvoicePdf(Integer invoiceId) throws IOException, MessagingException;

}
