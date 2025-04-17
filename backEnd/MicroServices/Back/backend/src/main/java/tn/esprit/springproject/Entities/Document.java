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
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    String name;
    String content;
    Date uploadDate;
    int numVersion;
    DocumantType documantType;
    @ManyToOne
    private Project project;

}
