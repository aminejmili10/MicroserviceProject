package tn.esprit.springproject.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.springproject.Entities.Financial;
import tn.esprit.springproject.Entities.FinancialType;
import tn.esprit.springproject.Entities.Project;

import java.util.List;
import java.util.Optional;

@Repository
public interface FinancialRepository extends JpaRepository<Financial, Integer> {
  //  Financial findByFinancialType(FinancialType financialType);
    @Query("SELECT f FROM Financial f WHERE f.project.id = :projectId")
    Optional<Financial> findByProject(@Param("projectId") Integer projectId);
    boolean existsByProject(Project project);
    Financial findByProjectId(Integer projectId);
    @Query("SELECT f FROM Financial f WHERE f.project.id = :projectId")
    List<Financial> findAllByProjectId(@Param("projectId") Integer projectId);
    List<Financial> findByFinancialType(FinancialType financialType);

}

