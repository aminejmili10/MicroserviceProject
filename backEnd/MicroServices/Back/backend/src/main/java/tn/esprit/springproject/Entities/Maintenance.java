package tn.esprit.springproject.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Maintenance extends BaseEntity{
    private String description;
    private Double cost;
    private Date maintainDate;
    private Integer nbWHMainatain;
    @Enumerated(EnumType.STRING)
    private MType mType;
    @ManyToOne
    @JsonIgnore
    private Resource resource;
}
