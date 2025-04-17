package tn.esprit.springproject.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.springproject.Entities.Bloc;

@Repository
public interface BlocReopsitory extends JpaRepository<Bloc,Long> {
}
