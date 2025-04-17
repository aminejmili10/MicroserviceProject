package tn.esprit.springproject.Services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import tn.esprit.springproject.Entities.Invoice;
import tn.esprit.springproject.Entities.InvoiceType;
import tn.esprit.springproject.Entities.Payment;
import tn.esprit.springproject.Repository.InvoiceRepository;
import tn.esprit.springproject.Repository.PaymentRepository;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class InvoiceService implements IInvoiceService {
    InvoiceRepository invoiceRepository;
    PaymentRepository paymentRepository;
    public Invoice createInvoice(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }
    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    public Invoice getbyid(Integer invoiceId) {
        return invoiceRepository.findById(invoiceId).get();
    }

    public Invoice modifyInvoice(Invoice invoice) {

        return invoiceRepository.save(invoice);
    }
    public void deleteInvoice(Integer idinvoice) {
        invoiceRepository.deleteById(idinvoice);

    }
   /* public Invoice createInvoiceWithPayment(Payment payment, Double amountDueFromUser) {

        if (payment.getPaymentAmount()== null || payment.getPaymentAmount() <= 0) {
            throw new RuntimeException("Le montant du paiement doit être supérieur à 0 !");
        }


        Invoice invoice = new Invoice();
        invoice.setInvoice_date(new Date());


        double totalAmount = payment.getPaymentAmount();


        double amountDue = (amountDueFromUser != null) ? amountDueFromUser : 0.0;
        double remainingAmount = totalAmount - amountDue;

        invoice.setTotal_amount(totalAmount);
        invoice.setAmount_due(amountDue);
        invoice.setRemaining_amount(remainingAmount);


        payment.setInvoice(invoice);


        invoiceRepository.save(invoice);
        return paymentRepository.save(payment).getInvoice();
    }

    */
   public Invoice generateInvoiceForExistingPayment(Integer paymentId, Double amountDueFromUser) {
       // ✅ Vérifier si le paiement existe
       Payment payment = paymentRepository.findById(paymentId)
               .orElseThrow(() -> new RuntimeException("Aucun paiement trouvé avec l'ID : " + paymentId));

       // ✅ Vérifier si le paiement a déjà une facture
       if (payment.getInvoice() != null) {
           throw new RuntimeException("Une facture existe déjà pour ce paiement !");
       }

       // ✅ Création d’une nouvelle facture
       Invoice invoice = new Invoice();
       invoice.setInvoice_date(new Date());

       // ✅ Définition du montant total
       double totalAmount = payment.getPaymentAmount(); // Le total de la facture

       // ✅ Vérification et application de la formule
       double amountDue = (amountDueFromUser != null) ? amountDueFromUser : 0.0; // Montant saisi par l'utilisateur
       double remainingAmount = totalAmount - amountDue; // ✅ Application de la formule

       invoice.setTotal_amount(totalAmount);
       invoice.setAmount_due(amountDue);
       invoice.setRemaining_amount(remainingAmount);

       // ✅ Associer la facture au paiement
       payment.setInvoice(invoice);

       // ✅ Sauvegarder la facture avant de mettre à jour le paiement
       invoiceRepository.save(invoice);
       paymentRepository.save(payment);

       return invoice;
   }
   /* public byte[] generateInvoice(Payment payment) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);
            document.open();

            // ✅ Vérifier que les données existent
            if (payment.getFinancial() == null || payment.getFinancial().getProject() == null) {
                throw new RuntimeException("Données du paiement ou du projet introuvables.");
            }

            // ✅ Ajouter le titre et les infos de la facture
            document.add(new Paragraph("T-Builders - Facture", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Nom du Projet : " + payment.getFinancial().getProject().getName()));
            document.add(new Paragraph("Montant Total : " + payment.getFinancial().getAmount() + " €"));
            document.add(new Paragraph("Montant Payé : " + payment.getPaymentAmount() + " €"));
            document.add(new Paragraph("Montant Restant : " +
                    (payment.getFinancial().getAmount() - paymentService.getTotalPaymentsByFinancialId(payment.getFinancial().getId())) + " €"));
            document.add(new Paragraph("Mode de Paiement : " + payment.getPaymentMethod()));
            document.add(new Paragraph("Date du Paiement : " + payment.getPaymentDate()));
            document.add(new Paragraph(" "));

            // ✅ Ajouter le logo (vérifie que le chemin est correct)
            Image logo = Image.getInstance("src/main/resources/static/logo.png");
            logo.scaleToFit(100, 50);
            document.add(logo);

            // ✅ Ajouter la signature numérique
            document.add(new Paragraph("\nSignature numérique"));
            document.close();

            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération de la facture", e);
        }

    */

    public Invoice validateQuote(Integer id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quote not found"));

        if (invoice.getType() != InvoiceType.QUOTE) {
            throw new RuntimeException("Not a quote");
        }

        invoice.setType(InvoiceType.INVOICE);
        invoice.setValidated(true);
        invoice.setDate(LocalDate.now());

        return invoiceRepository.save(invoice);
    }

    public List<Invoice> getQuotes() {
        return invoiceRepository.findByType(InvoiceType.QUOTE);
    }
    public Invoice save(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    private JavaMailSender mailSender;
    public String generateAndSendInvoicePdf(Integer invoiceId) throws IOException, MessagingException {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        List<Payment> payments = paymentRepository.findByInvoice_Id(invoiceId);
        double totalPaid = payments.stream().mapToDouble(Payment::getPaymentAmount).sum();
        double remaining = invoice.getTotal_amount() - totalPaid;

        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder.append("<html><body>");
        htmlBuilder.append("<h1>Facture Professionnelle</h1>");
        htmlBuilder.append("<p><strong>Date :</strong> ").append(invoice.getDate()).append("</p>");
        htmlBuilder.append("<p><strong>Montant total :</strong> ").append(invoice.getTotal_amount()).append(" TND</p>");
        htmlBuilder.append("<p><strong>Montant payé :</strong> ").append(totalPaid).append(" TND</p>");
        htmlBuilder.append("<p><strong>Montant restant :</strong> ").append(remaining).append(" TND</p>");
        htmlBuilder.append("<h2>Détails des paiements</h2><ul>");

        for (Payment p : payments) {
            htmlBuilder.append("<li>").append(p.getPaymentDate())
                    .append(" — ").append(p.getPaymentAmount()).append(" TND</li>");
        }

        htmlBuilder.append("</ul></body></html>");

        String apiKey = "ouennicheranim@gmail.com_6KOZktZzuo2OVXExbWsV2sfMUUCoAUFtqmcm1FlUP9DwsNQJoTwszydKDSQZBsqm";
        URL url = new URL("https://api.pdf.co/v1/pdf/convert/from/html");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("x-api-key", apiKey);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String jsonBody = "{ \"html\": \"" + htmlBuilder.toString().replace("\"", "\\\"") + "\" }";
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line.trim());
            }
        }

        String responseStr = response.toString();
        String pdfUrl = responseStr.split("\"url\":\"")[1].split("\"")[0].replace("\\/", "/");

        URL downloadUrl = new URL(pdfUrl);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (InputStream is = downloadUrl.openStream()) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo("tbuilderscompro@gmail.com");
        helper.setSubject("Facture T-Builders");
        helper.setText("Veuillez trouver ci-joint la facture professionnelle générée.");

        InputStreamSource attachmentSource = new ByteArrayResource(baos.toByteArray());
        helper.addAttachment("facture.pdf", attachmentSource);

        mailSender.send(message);

        return "Facture PDF générée et envoyée par e-mail à tbuilderscompro@gmail.com ✅";
    }

}

