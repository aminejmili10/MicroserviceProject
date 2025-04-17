package tn.esprit.springproject.Entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Project_Plane {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    int nb_etage;
    int nb_salle;
    int nb_chambre;
    boolean jardin;
    boolean picine;
    boolean garage;
    @Enumerated(EnumType.STRING)
    Category category;
    @OneToOne
    private Project project;



}
