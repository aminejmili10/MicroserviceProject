package esprit.livraison.Services;

import esprit.livraison.Repository.LivraisonRepo;
import esprit.livraison.entity.Livraison;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@AllArgsConstructor
@Service
public class livraisonserv implements ILivraisonServ {

    private LivraisonRepo livraisonRepo;

    public Livraison createLivraison(Livraison livraison) {
        return livraisonRepo.save(livraison);
    }

    public List<Livraison> getAllLivraisons() {
        return livraisonRepo.findAll();
    }



    public Optional<Livraison> getLivraisonyById(Long id) {
        return livraisonRepo.findById(id);
    }

    public Livraison updateLivraison(Long id, Livraison liv) {
        liv.setId(id);
        return livraisonRepo.save(liv);
    }

    public void deleteLivraison(Long id) {
        livraisonRepo.deleteById(id);
    }
}
