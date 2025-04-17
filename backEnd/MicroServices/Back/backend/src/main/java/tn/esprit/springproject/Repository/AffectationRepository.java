package tn.esprit.springproject.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.springproject.Entities.Affection;

import java.util.Date;
import java.util.List;

public interface AffectationRepository extends JpaRepository<Affection,Integer> {
    List<Affection> findByEndDateLessThanEqual(Date date);

}
