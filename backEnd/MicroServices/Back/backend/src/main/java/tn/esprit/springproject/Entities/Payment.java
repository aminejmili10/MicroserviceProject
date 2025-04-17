package tn.esprit.springproject.Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    Double paymentAmount;
    Date   paymentDate;
    Double lateInterest;
    @JsonProperty("isLate")
    private boolean isLate;
    @Enumerated(EnumType.STRING)
    PaymentStatus paymentStatus;
    @Enumerated(EnumType.STRING)
    PaymentMethod paymentMethod;

    @ManyToOne
    @JsonIgnoreProperties({"amount", "transactionDate", "financialType", "financial", "project"})
    private Financial financial;

    @OneToOne
    private Invoice invoice;

   /* public double getLateInterest() {
        return lateInterest;
    }

    public void setLateInterest(double lateInterest) {
        this.lateInterest = lateInterest;
    }

    public boolean isLate() {
        return isLate;
    }

    public void setLate(boolean late) {
        this.isLate = late;
    }


    */
}
