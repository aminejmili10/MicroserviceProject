package tn.esprit.springproject.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    String title;
    int progress;
    int AssignedTo;
    Date date;
    TaskStatus status;
    @ManyToOne
    private Schedule schedule;


}
