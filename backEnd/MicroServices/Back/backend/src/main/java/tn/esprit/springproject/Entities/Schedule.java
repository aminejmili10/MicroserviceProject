package tn.esprit.springproject.Entities;

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
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    String title;
    Date startDate ;
    Date  endDate;
    @ManyToOne
    private Project project;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "schedule")
    private Set<Task>tasks=new HashSet<Task>();
}
