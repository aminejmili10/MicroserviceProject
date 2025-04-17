package tn.esprit.springproject.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceStats {
    private String brande; // Machine brand
    private Long maintenanceCount; // Number of maintenance operations
}
