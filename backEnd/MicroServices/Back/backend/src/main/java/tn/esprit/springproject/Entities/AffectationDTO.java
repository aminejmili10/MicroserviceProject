package tn.esprit.springproject.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class AffectationDTO {
    private Integer id;
    private Date startDate;
    private Date endDate;
    private Integer workHours;
    private Integer quantity;
    private String projectName;
    private String resourceBrand;

}
