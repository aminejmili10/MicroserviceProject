package tn.esprit.springproject.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.springproject.Entities.Resource;
import tn.esprit.springproject.Entities.Rtype;

import java.util.List;
import java.util.Optional;

public interface ResourceRepository extends JpaRepository<Resource,Integer> {
    Resource getResourcesByBrande(String brand);
    List<Resource> findByRtype(Rtype rtype);
    // Find all resources by rtype and status
    List<Resource> findByRtypeAndStatus(Rtype rtype, Boolean status);

    // Find the first resource by rtype and status
    Optional<Resource> findFirstByRtypeAndStatus(Rtype rtype, Boolean status);

    // Find the first resource by rtype, brande, and status
    Optional<Resource> findFirstByRtypeAndBrandeAndStatus(Rtype rtype, String brande, Boolean status);

    Resource findByPlateNumber(String plate);


}
