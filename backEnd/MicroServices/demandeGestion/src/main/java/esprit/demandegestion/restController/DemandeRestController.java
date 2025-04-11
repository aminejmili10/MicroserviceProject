package esprit.demandegestion.restController;

import esprit.demandegestion.entity.Demande;
import esprit.demandegestion.services.DemandeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/demandes") // Changement vers une URL REST standard
public class DemandeRestController {

    private final DemandeService demandeService;

    // Injection par constructeur (meilleure pratique)
    @Autowired
    public DemandeRestController(DemandeService demandeService) {
        this.demandeService = demandeService;
    }

    // Create
    @PostMapping
    public ResponseEntity<Demande> createDemande(@Valid @RequestBody Demande demande) {
        Demande createdDemande = demandeService.createDemande(demande);
        return new ResponseEntity<>(createdDemande, HttpStatus.CREATED);
    }

    // Read all
    @GetMapping
    public ResponseEntity<List<Demande>> getAllDemandes() {
        List<Demande> demandes = demandeService.getAllDemandes();
        return new ResponseEntity<>(demandes, HttpStatus.OK);
    }

    // Read one
    @GetMapping("/{id}")
    public ResponseEntity<Demande> getDemandeById(@PathVariable Long id) {
        Demande demande = demandeService.getDemandeById(id);
        if (demande != null) {
            return new ResponseEntity<>(demande, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<Demande> updateDemande(@PathVariable Long id, @Valid @RequestBody Demande demande) {
        Demande existingDemande = demandeService.getDemandeById(id);
        if (existingDemande != null) {
            demande.setId(id);
            Demande updatedDemande = demandeService.updateDemande(demande);
            return new ResponseEntity<>(updatedDemande, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDemande(@PathVariable Long id) {
        Demande existingDemande = demandeService.getDemandeById(id);
        if (existingDemande != null) {
            demandeService.deleteDemande(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}