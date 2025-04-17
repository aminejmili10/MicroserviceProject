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
public class Project {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;
    float surface;
    float budget;
    String image;
    String localisation;
    String name;
    Date Date_debut;
    Date date_fin;
    Category category;
    @ManyToOne
    @JsonIgnore
    private User user;

    @OneToOne(cascade = CascadeType.ALL,mappedBy ="project")
    private Project_Plane projectPlane;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "project")
    private Set<Schedule>schedules=new HashSet<Schedule>();

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "project")
    private Set<Document>documents=new HashSet<Document>();

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "project")
    @JsonIgnore
    private Set<Financial> financials=new HashSet<Financial>();
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "project")
    private Set<Affection>affections=new HashSet<Affection>();




}
