package tn.esprit.springproject.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    String name;
    String content ;
    String postedby;
    String img;
    Date date;
    int likeCount;
    int viewCount;

@OneToMany(cascade = CascadeType.ALL,mappedBy = "post")
    List<Comment> comments;
@ManyToOne
    private User user;



}
