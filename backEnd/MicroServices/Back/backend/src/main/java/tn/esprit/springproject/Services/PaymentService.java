package tn.esprit.springproject.Services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tn.esprit.springproject.Entities.*;
import tn.esprit.springproject.Repository.FinancialRepository;
import tn.esprit.springproject.Repository.PaymentRepository;
import tn.esprit.springproject.Repository.ProjectRepository;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
@Slf4j

public class PaymentService implements IPaymentService {
    PaymentRepository paymentRepository;
    FinancialRepository financialRepository;
    ProjectRepository projectRepository;
    private InvoiceService invoiceService;

    JavaMailSender mailSender;
    public Payment createPaymentt(Payment payment) {

        if (payment.getFinancial() == null || payment.getFinancial().getId() == null) {
            throw new RuntimeException("Financial ID is required");
        }
        return paymentRepository.save(payment);
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Payment getbyid(Integer paymentId) {
        return paymentRepository.findById(paymentId).get();
    }

    public Payment modifyPayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    public void deletePayment(Integer idpayment) {
        paymentRepository.deleteById(idpayment);
    }

    public List<Payment> getPaymentsByFinancialType(FinancialType financialType) {
        return paymentRepository.findByFinancial_FinancialType(financialType);
    }

    public Payment createPaymentWithFinancialType(Payment payment, FinancialType financialType) {
        List<Financial> financials = financialRepository.findByFinancialType(financialType);
        if (financials.isEmpty()) {
            throw new RuntimeException("No Financial found for type: " + financialType);
        }

        Financial financial = financials.get(0);


        payment.setFinancial(financial);
        Payment savedPayment = paymentRepository.save(payment);

        try {
            generateAndSendFinancialInvoicePdf(financial);
        } catch (Exception e) {
            log.error("Erreur lors de la génération de la facture PDF : " + e.getMessage(), e);
        }

        return savedPayment;
    }



    public List<Payment> getPaymentsByUserId(Long userId) {
        return paymentRepository.findPaymentsByUserId(userId);
    }

    public Payment createPayment(Payment payment, Integer userId) {
        Project project = projectRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("No projects found for this user!"));
        Financial financial = financialRepository.findByProject(project.getId())
                .orElseThrow(() -> new RuntimeException("No Financial found for this project!"));

        payment.setFinancial(financial);
        Payment savedPayment = paymentRepository.save(payment);

        try {
            generateAndSendFinancialInvoicePdf(financial);
        } catch (Exception e) {
            log.error("Erreur lors de la génération de la facture PDF : " + e.getMessage(), e);
        }

        return savedPayment;
    }


    public double calculateLateInterest(Financial financial, double annualInterestRate) {
        Date financialDate = financial.getTransactionDate();
        Date currentDate = new Date();
        if (currentDate.after(financialDate)) {
            long diffInMillies = Math.abs(currentDate.getTime() - financialDate.getTime());
            long daysLate = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            return (financial.getAmount() * annualInterestRate * daysLate) / 365;
        }
        return 0.0;
    }



    public Payment getPaymentById(Integer id) {
        return paymentRepository.findById(id).orElse(null);
    }

    public Payment updatePayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    public List<Payment> getPaymentsByFinancial(Integer financialId) {
        return paymentRepository.findByFinancialId(financialId);
    }

    public double calculateLateInterest(double unpaidAmount, double annualRate, Date dueDate) {
        Date currentDate = new Date();
        if (currentDate.after(dueDate)) {
            long diffInMillies = Math.abs(currentDate.getTime() - dueDate.getTime());
            long daysLate = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            return (unpaidAmount * annualRate * daysLate) / 365;
        }
        return 0.0;
    }



    @Scheduled(cron = "0 0 0 * * ?")
    public void sendPaymentReminders() {
        log.info("Starting sendPaymentReminders");
        List<Payment> payments = paymentRepository.findAll();
        Date today = new Date();
        log.info("Today: " + today + ", Number of payments: " + payments.size());

        if (payments.isEmpty()) {
            log.info("No payments found in the database");
            return;
        }

        for (Payment payment : payments) {
            Date dueDate = payment.getPaymentDate();
            log.info("Checking payment ID: " + payment.getId() + ", Due Date: " + dueDate + ", Status: " + payment.getPaymentStatus());
            if (payment.getPaymentStatus() != PaymentStatus.COMPLETED) {
                String userEmail = "ouennicheranim@gmail.com";
                log.info("Sending reminder to: " + userEmail + " for payment ID: " + payment.getId());
                sendReminderEmail(userEmail, payment);
            } else {
                log.info("Payment ID " + payment.getId() + " skipped: Status=" + payment.getPaymentStatus());
            }
        }
        log.info("Finished sendPaymentReminders");
    }

    private boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }

    private void sendReminderEmail(String to, Payment payment) {
        String subject = "Payment Reminder: Due in 2 Days";
        String body = "Payment Reminder\n\n" +
                "Dear User,\n\n" +
                "This is a reminder that your payment of " + payment.getPaymentAmount() + " EUR " +
                "for Financial ID " + payment.getFinancial().getId() + " is due on " +
                payment.getPaymentDate() + ". Please make the payment soon.\n\n" +
                "Thank you!";
        log.info("Preparing to send email to: " + to + " for payment ID: " + payment.getId());

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            message.setFrom("tbuilderscompro@gmail.com");
            mailSender.send(message);
            log.info("Reminder email sent successfully to " + to + " for payment ID: " + payment.getId());
        } catch (Exception e) {
            log.error("Failed to send reminder email to " + to + ": " + e.getMessage(), e);
        }
    }



  //  @Scheduled(cron = "0 * * * * ?")

    @Scheduled(cron = "0 0 0 * * ?")
    public void updateLatePayments() {
        log.info("Démarrage de la mise à jour des paiements en retard");

        List<Payment> payments = paymentRepository.findByPaymentStatusNot(PaymentStatus.COMPLETED);
        if (payments.isEmpty()) {
            log.info("Aucun paiement non complété trouvé");
            return;
        }

        Date currentDate = new Date();

        double annualInterestRate = 0.2;


        for (Payment payment : payments) {
            Date dueDate = payment.getPaymentDate();

            log.info("Vérification du paiement ID: " + payment.getId() +
                    ", Date d'échéance: " + dueDate +
                    ", Date actuelle: " + currentDate +
                    ", Statut actuel: " + payment.getPaymentStatus() +
                    ", IsLate actuel: " + payment.isLate());


            if (currentDate.after(dueDate)) {

                long diffInMillies = currentDate.getTime() - dueDate.getTime();
                long daysLate = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);


                double lateInterest = calculateLateInterest(payment.getPaymentAmount(), annualInterestRate, dueDate);


                payment.setLate(true);
                payment.setLateInterest(lateInterest != 0.0 ?
                        (payment.getLateInterest() != null ? payment.getLateInterest() + lateInterest : lateInterest) :
                        0.0);


                Payment updatedPayment = paymentRepository.save(payment);

                log.info("Paiement ID: " + payment.getId() + " marqué comme en retard. " +
                        "Jours de retard: " + daysLate +
                        ", Pénalités: " + lateInterest +
                        ", IsLate mis à jour: " + updatedPayment.isLate() +
                        ", LateInterest mis à jour: " + updatedPayment.getLateInterest());
            } else {
                log.info("Paiement ID: " + payment.getId() + " n'est pas en retard.");
            }
        }

        log.info("Mise à jour des paiements en retard terminée");
    }


   public void sendPaymentConfirmation(Payment payment) {
       String userEmail = "ouennicheranim@gmail.com";
       String subject = "Confirmation of Successful Payment";
       String body = "Payment Confirmation\n\n" +
               "Dear User,\n\n" +
               "We confirm that your payment of " + payment.getPaymentAmount() + " EUR " +
               "for Financial ID " + payment.getFinancial().getId() + " a été effectué avec succès le " +
               new Date() + ".\n\n" +
               "Thank you for your payment !\n\n" +
               "Sincerely,\nThe team";

       log.info("Sending payment confirmation to : " + userEmail + " for payment ID : " + payment.getId());

       try {
           SimpleMailMessage message = new SimpleMailMessage();
           message.setTo(userEmail);
           message.setSubject(subject);
           message.setText(body);
           message.setFrom("tbuilderscompro@gmail.com");
           mailSender.send(message);
           log.info("Payment confirmation successfully sent to " + userEmail);
       } catch (Exception e) {
           log.error("Error sending confirmation to " + userEmail + " : " + e.getMessage(), e);
       }
   }


    public Map<String, Object> getPaymentStatistics() {
        Map<String, Object> stats = new HashMap<>();

        long totalPayments = paymentRepository.count();

        long completedPayments = paymentRepository.countByPaymentStatus(PaymentStatus.COMPLETED);
        long pendingPayments = paymentRepository.countByPaymentStatus(PaymentStatus.PENDING);


        double totalPaid = paymentRepository.sumPaymentAmountByStatus(PaymentStatus.COMPLETED);
        double totalPending = paymentRepository.sumPaymentAmountByStatus(PaymentStatus.PENDING);


        double completionRate = (totalPayments > 0) ? (double) completedPayments / totalPayments * 100 : 0;

        stats.put("totalPayments", totalPayments);
        stats.put("completedPayments", completedPayments);
        stats.put("pendingPayments", pendingPayments);

        stats.put("totalPaid", totalPaid);
        stats.put("totalPending", totalPending);

        stats.put("completionRate", completionRate);

        return stats;
    }
    public void generateAndSendFinancialInvoicePdf(Financial financial) throws IOException, MessagingException {
        List<Payment> payments = paymentRepository.findByFinancialId(financial.getId());
        double totalPaid = payments.stream().mapToDouble(Payment::getPaymentAmount).sum();
        double remaining = financial.getAmount() - totalPaid;

        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder.append("<html><body>");
        htmlBuilder.append("<h1>Facture T-Builders</h1>");
        htmlBuilder.append("<p><strong>Date de transaction :</strong> ").append(financial.getTransactionDate()).append("</p>");
        htmlBuilder.append("<p><strong>Montant total :</strong> ").append(financial.getAmount()).append(" TND</p>");
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

        String html = htmlBuilder.toString().replace("\"", "\\\"");
        String jsonBody = "{ \"html\": \"" + html + "\" }";

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

        JSONObject jsonResponse = new JSONObject(response.toString());
        String pdfUrl = jsonResponse.getString("url");
        log.info("✅ PDF URL : " + pdfUrl);

        URL downloadUrl = new URL(pdfUrl);
        HttpURLConnection downloadConn = (HttpURLConnection) downloadUrl.openConnection();
        downloadConn.setRequestMethod("GET");
        downloadConn.setRequestProperty("Accept", "application/pdf");

        if (downloadConn.getResponseCode() != 200) {
            throw new IOException("Erreur téléchargement PDF : Code HTTP " + downloadConn.getResponseCode());
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (InputStream is = downloadConn.getInputStream()) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo("tbuilderscompro@gmail.com");
        helper.setSubject("Facture T-Builders - Paiements pour projet");
        helper.setText("Veuillez trouver ci-joint la facture automatique générée.");

        InputStreamSource attachmentSource = new ByteArrayResource(baos.toByteArray());
        helper.addAttachment("facture.pdf", attachmentSource);

        mailSender.send(message);
    }


}

