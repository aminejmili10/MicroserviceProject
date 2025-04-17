package tn.esprit.springproject.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    Date invoice_date;
    Double total_amount;
    Double amount_due;
    Double remaining_amount;
    private String serviceList; // Liste des services du devis
    private String estimatedDuration; // Durée estimée du projet
    private LocalDate validUntil; // Date de validité du devis

    @Enumerated(EnumType.STRING)
    private InvoiceType type = InvoiceType.QUOTE; // Par défaut c’est un devis

    private boolean validated = false; // Par défaut, non validé

    @OneToOne
    private Payment payment ;
    @ManyToOne
    private Project project;

    @ManyToOne
    private User client;

    private LocalDate date = LocalDate.now();
}
