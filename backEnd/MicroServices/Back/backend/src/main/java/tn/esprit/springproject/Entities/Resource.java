package tn.esprit.springproject.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Resource extends BaseEntity {

    private String plateNumber;
    private String brande;
    private Integer quantity;
    private Double price;
    private Integer monthsPay;
    private Boolean status;
    private Integer nbWorkHours;
    @Enumerated(EnumType.STRING)
    private Rtype rtype;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "resource")
    private Set<Maintenance>maintenances=new HashSet<Maintenance>();

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "resource")
    private Set<Affection>affections=new HashSet<Affection>();
}
