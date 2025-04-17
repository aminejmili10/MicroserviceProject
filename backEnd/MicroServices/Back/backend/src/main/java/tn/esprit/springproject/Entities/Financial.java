package tn.esprit.springproject.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Financial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    Double amount;
    Date transactionDate;
    @Enumerated(EnumType.STRING)
    FinancialType financialType;

    @ManyToOne
    private Project project;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "financial")
    @JsonIgnore
    private Set<Payment>payments=new HashSet<Payment>();

}
