package tn.esprit.springproject.Services;

import tn.esprit.springproject.Entities.*;

import java.util.List;

public interface IResourceSerivce {
    Resource addResource(Resource resource);
    List<Resource>getAllResource();
    Resource getResourceById(Integer id);
    Resource updateResource(Resource resource);
    void removeResource(Integer id);

    Resource affectMaintainToMachine(Integer id,Maintenance maintenance);
    List<Maintenance> getAllMaintainOfMachine(Integer id);

    void removeMaintain(Integer id);
    Resource affectResourcetoProject(Integer idr, Integer idp, Affection affection);
    List <Affection>getAllAffectaion();
    List<AffectationDTO> getAllAffectationDTOs();
    void removeAffectation(Integer id);
    List<Project> getAllProjects();
    List<MaintenanceStats> getMaintenanceStatsForMachines();
    Project addProject(Project project);
    void updateAffectationQuantity(Integer id, int newQuantity);
    MaintenancePredictionDTO predictNextMaintenance(Integer resourceId);
    // New Stripe methods
    String createCheckoutSession(String userId) throws Exception;
    boolean verifyPayment(String sessionId) throws Exception;
    void checkAndReleaseExpiredAffections();

}
