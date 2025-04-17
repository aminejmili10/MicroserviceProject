package tn.esprit.springproject.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.springproject.Entities.Maintenance;

import java.util.List;

public interface MaintenanceRepository extends JpaRepository<Maintenance,Integer> {
    @Query("SELECT m FROM Maintenance m WHERE m.resource.id = :resourceId")
    List<Maintenance> findAllByResourceId(@Param("resourceId") Integer resourceId);
}
