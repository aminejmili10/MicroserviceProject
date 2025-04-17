package tn.esprit.springproject.Services;

import com.stripe.Stripe;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tn.esprit.springproject.Entities.*;
import tn.esprit.springproject.Repository.AffectationRepository;
import tn.esprit.springproject.Repository.MaintenanceRepository;
import tn.esprit.springproject.Repository.ProjectRepository;
import tn.esprit.springproject.Repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Value;
import java.util.*;
import java.util.stream.Collectors;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import java.util.logging.Logger;
@AllArgsConstructor
@Service
public class IResourceServiceImp implements IResourceSerivce{
    ResourceRepository resourceRepository;
    MaintenanceRepository maintenanceRepository;
    AffectationRepository affectationRepository;
    ProjectRepository projectRepository;
    private static final Logger logger = Logger.getLogger(IResourceServiceImp.class.getName());
    private static final String UPLOAD_DIR = "uploads/3dmodels/";
    @Override
    public Resource addResource(Resource resource) {
        if (resource.getRtype() == Rtype.MACHINE) {
            // Check for duplicate plate number
            if (resource.getPlateNumber() != null && !resource.getPlateNumber().isEmpty()) {
                Resource existingResource = resourceRepository.findByPlateNumber(resource.getPlateNumber());
                if (existingResource != null) {
                    throw new IllegalArgumentException("A machine with plate number " + resource.getPlateNumber() + " already exists.");
                }
            }
            resource.setStatus(true);
        } else {
            // Tools/Materials logic
            if (resource.getQuantity() > 0) {
                resource.setStatus(true);
            }
            Resource existingResource = resourceRepository.getResourcesByBrande(resource.getBrande());
            if (existingResource != null) {
                existingResource.setQuantity(existingResource.getQuantity() + resource.getQuantity());
                return resourceRepository.save(existingResource);
            }
        }
        return resourceRepository.save(resource);
    }

    @Override
    public Resource updateResource(Resource resource) {
        if (resourceRepository.existsById(resource.getId())) {
            Resource existingResource = resourceRepository.findById(resource.getId()).get();
            // Check for plate number uniqueness during update (for machines)
            if (resource.getRtype() == Rtype.MACHINE && resource.getPlateNumber() != null && !resource.getPlateNumber().isEmpty()) {
                Resource resourceWithPlate = resourceRepository.findByPlateNumber(resource.getPlateNumber());
                if (resourceWithPlate != null && !resourceWithPlate.getId().equals(resource.getId())) {
                    throw new IllegalArgumentException("A machine with plate number " + resource.getPlateNumber() + " already exists.");
                }
            }
            existingResource.setPlateNumber(resource.getPlateNumber());
            existingResource.setBrande(resource.getBrande());
            existingResource.setPrice(resource.getPrice());
            existingResource.setNbWorkHours(resource.getNbWorkHours());
            existingResource.setMonthsPay(resource.getMonthsPay());
            existingResource.setQuantity(resource.getQuantity());

            if (existingResource.getRtype() == Rtype.TOOL || existingResource.getRtype() == Rtype.MATERIALS) {
                existingResource.setStatus(existingResource.getQuantity() > 0);
            }

            return resourceRepository.save(existingResource);
        } else {
            throw new RuntimeException("Resource not found with id " + resource.getId());
        }
    }

    @Override
    public List<Resource> getAllResource() {
        return resourceRepository.findAll();
    }

    @Override
    public Resource getResourceById(Integer id) {
        return resourceRepository.findById(id).orElseThrow(() -> new RuntimeException("Resource not found"));
    }



    @Override
    public void removeResource(Integer id) {
        resourceRepository.deleteById(id);
    }

    @Override
    public Resource affectMaintainToMachine(Integer id, Maintenance maintenance) {
        Resource r = resourceRepository.findById(id).orElseThrow(() -> new RuntimeException("Resource not found"));
        if (r != null && r.getRtype() == Rtype.MACHINE) {
            r.getMaintenances().add(maintenance);
            maintenance.setResource(r);
        }
        return resourceRepository.save(r);
    }

    @Override
    public List<Maintenance> getAllMaintainOfMachine(Integer id) {
        return maintenanceRepository.findAllByResourceId(id);
    }

    @Override
    public void removeMaintain(Integer id) {
        maintenanceRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Resource affectResourcetoProject(Integer idr, Integer idp, Affection affection) {
        Resource r = resourceRepository.findById(idr).orElse(null);
        Project p = projectRepository.findById(idp).orElse(null);

        if (r == null || p == null) {
            throw new RuntimeException("Resource or Project not found!");
        }

        if (affection == null) {
            affection = new Affection();
        }

        if (r.getRtype() == Rtype.MACHINE) {
            if (!r.getStatus()) {
                throw new RuntimeException("Machine is not available for affectation.");
            }
            r.setStatus(false);
        }

        if (r.getRtype() == Rtype.TOOL || r.getRtype() == Rtype.MATERIALS) {
            if (r.getQuantity() < affection.getQuantity()) {
                throw new RuntimeException("Insufficient quantity of resource.");
            }
            r.setQuantity(r.getQuantity() - affection.getQuantity());
            r.setStatus(r.getQuantity() > 0);
            resourceRepository.save(r);
        }

        affection.setResource(r);
        affection.setProject(p);
        affectationRepository.save(affection);

        return resourceRepository.save(r);
    }

    @Override
    public List<Affection> getAllAffectaion() {
        return affectationRepository.findAll();
    }

    @Override
    public List<AffectationDTO> getAllAffectationDTOs() {
        List<Affection> affectations = affectationRepository.findAll();
        return affectations.stream().map(aff ->
                new AffectationDTO(
                        aff.getId(),
                        aff.getStartDate(),
                        aff.getEndDate(),
                        aff.getWorkHours(),
                        aff.getQuantity(),
                        aff.getProject() != null ? aff.getProject().getName() : null,
                        aff.getResource() != null ? aff.getResource().getBrande() : null
                )
        ).collect(Collectors.toList());
    }

    @Override
    public void removeAffectation(Integer id) {
        Affection affectation = affectationRepository.findById(id).orElseThrow(() -> new RuntimeException("Affectation not found"));
        Resource resource = affectation.getResource();

        if (resource != null) {
            if (resource.getRtype() == Rtype.MACHINE) {
                resource.setStatus(true);
                resourceRepository.save(resource);
            }
            if (resource.getRtype() == Rtype.TOOL || resource.getRtype() == Rtype.MATERIALS) {
                resource.setQuantity(resource.getQuantity() + affectation.getQuantity());
                resource.setStatus(resource.getQuantity() > 0);
                resourceRepository.save(resource);
            }
        }

        affectationRepository.deleteById(id);
    }

    @Override
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public List<MaintenanceStats> getMaintenanceStatsForMachines() {
        List<Resource> machines = resourceRepository.findByRtype(Rtype.MACHINE);
        return machines.stream()
                .map(resource -> new MaintenanceStats(
                        resource.getBrande(),
                        (long) resource.getMaintenances().size()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public Project addProject(Project project) {
        Project savedProject = projectRepository.save(project);
        predictAndAffectResources(savedProject); // This triggers the function
        return savedProject;
    }


    private void predictAndAffectResources(Project project) {
        float surface = project.getSurface();
        Category category = project.getCategory();
        Date startDate = project.getDate_debut();
        Date endDate = project.getDate_fin();


        float surfaceFactor = surface / 100;

        // Full list of resource types
        String[] allMachineTypes = {"Backhoe Loader", "Excavator", "cement truck", "Dozer", "bobcat", "tower"};
        String[] allToolTypes = {"Bucket", "Drill", "Hammer", "Level", "Meter", "Trowel"};
        String[] allMaterialTypes = {"Steel", "bricks", "concrete", "concrete stones", "sand"};

        if (category == Category.Maisons_individuelles) {

            int machineCount = (int) Math.ceil(surfaceFactor);


            int toolQuantityPerType = (int) (10 * surfaceFactor);


            int materialQuantityPerType = (int) (50 * surfaceFactor);


            affectMultipleMachines(machineCount, allMachineTypes, project, startDate, endDate);


            affectMultipleTools(toolQuantityPerType, allToolTypes, project);


            affectMultipleMaterials(materialQuantityPerType, allMaterialTypes, project);

        } else if (category == Category.Immeubles_habitation) {

            int machineCount = (int) Math.ceil(2 * surfaceFactor);


            int toolQuantityPerType = (int) (20 * surfaceFactor);


            int materialQuantityPerType = (int) (100 * surfaceFactor);


            affectMultipleMachines(machineCount, allMachineTypes, project, startDate, endDate);


            affectMultipleTools(toolQuantityPerType, allToolTypes, project);


            affectMultipleMaterials(materialQuantityPerType, allMaterialTypes, project);
        }
    }


    private void affectMultipleMachines(int machineCount, String[] machineTypes, Project project, Date startDate, Date endDate) {
        List<Resource> availableMachines = resourceRepository.findByRtypeAndStatus(Rtype.MACHINE, true);
        int affectedMachines = 0;
        int typeIndex = 0;

        for (Resource machine : availableMachines) {
            if (affectedMachines >= machineCount) break;

            String targetType = machineTypes[typeIndex % machineTypes.length];
            if (machine.getBrande().equals(targetType)) {
                Affection affection = new Affection();
                affection.setStartDate(startDate);
                affection.setEndDate(endDate);
                affection.setWorkHours(calculateWorkHours(startDate, endDate));
                affection.setQuantity(1); // Machine always has quantity 1
                affectResourcetoProject(machine.getId(), project.getId(), affection);
                affectedMachines++;
                typeIndex++; // Move to next type
            }
        }

        if (affectedMachines < machineCount) {
            System.out.println("Not enough machines available. Affected: " + affectedMachines + ", Required: " + machineCount);
        }
    }


    private void affectMultipleTools(int quantityPerType, String[] toolTypes, Project project) {
        for (String toolType : toolTypes) {
            Resource tool = resourceRepository.findFirstByRtypeAndBrandeAndStatus(Rtype.TOOL, toolType, true)
                    .orElse(null);
            if (tool != null && tool.getQuantity() >= quantityPerType) {
                Affection affection = new Affection();
                affection.setQuantity(quantityPerType);
                affectResourcetoProject(tool.getId(), project.getId(), affection);
            }
        }
    }

    // Helper method to affect multiple materials
    private void affectMultipleMaterials(int quantityPerType, String[] materialTypes, Project project) {
        for (String materialType : materialTypes) {
            Resource material = resourceRepository.findFirstByRtypeAndBrandeAndStatus(Rtype.MATERIALS, materialType, true)
                    .orElse(null);
            if (material != null && material.getQuantity() >= quantityPerType) {
                Affection affection = new Affection();
                affection.setQuantity(quantityPerType);
                affectResourcetoProject(material.getId(), project.getId(), affection);
            }
        }
    }

    private int calculateWorkHours(Date startDate, Date endDate) {
        long diffInMillies = Math.abs(endDate.getTime() - startDate.getTime());
        return (int) (diffInMillies / (1000 * 60 * 60)); // Convert to hours
    }
    @Override
    @Transactional
    public void updateAffectationQuantity(Integer id, int newQuantity) {
        Affection affectation = affectationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Affectation not found with ID: " + id));
        Resource resource = affectation.getResource();

        if (resource == null) {
            throw new RuntimeException("Resource not found for affectation ID: " + id);
        }

        // Calculate the difference in quantity
        int oldQuantity = affectation.getQuantity();
        int quantityDifference = newQuantity - oldQuantity;

        if (quantityDifference != 0) {
            if (resource.getRtype() == Rtype.MACHINE) {
                // Machines have a fixed quantity of 1, so no change needed
                if (newQuantity != 1) {
                    throw new RuntimeException("Machine quantity must remain 1");
                }
            } else if (resource.getRtype() == Rtype.TOOL || resource.getRtype() == Rtype.MATERIALS) {
                // Adjust resource quantity based on the difference
                if (quantityDifference > 0) {
                    // Increasing quantity: check if enough resources are available
                    if (resource.getQuantity() < quantityDifference) {
                        throw new RuntimeException("Insufficient resource quantity available");
                    }
                    resource.setQuantity(resource.getQuantity() - quantityDifference);
                } else {
                    // Decreasing quantity: add back to resource stock
                    resource.setQuantity(resource.getQuantity() + Math.abs(quantityDifference));
                }
                resource.setStatus(resource.getQuantity() > 0);
                resourceRepository.save(resource);
            }

            // Update affectation quantity
            affectation.setQuantity(newQuantity);
            affectationRepository.save(affectation);
        }
    }
    @Override
    public MaintenancePredictionDTO predictNextMaintenance(Integer resourceId) {
        // Fetch the resource (machine)
        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new RuntimeException("Resource not found with ID: " + resourceId));

        if (resource.getRtype() != Rtype.MACHINE) {
            throw new RuntimeException("Prediction is only available for machines.");
        }

        // Fetch all maintenance records for this machine
        List<Maintenance> maintenances = maintenanceRepository.findAllByResourceId(resourceId);
        if (maintenances.isEmpty()) {
            return new MaintenancePredictionDTO(
                    resource.getBrande(),
                    "N/A",
                    0,
                    "No maintenance history available. Schedule initial maintenance (e.g., OIL) after 500 hours."
            );
        }

        // Group maintenance records by type (OIL, TIRES, etc.)
        Map<MType, List<Maintenance>> maintenanceByType = maintenances.stream()
                .collect(Collectors.groupingBy(Maintenance::getMType));

        // Define typical maintenance intervals (in hours) for each type as a fallback
        Map<MType, Integer> typicalIntervals = new HashMap<>();
        typicalIntervals.put(MType.OIL, 500); // Oil change every 500 hours
        typicalIntervals.put(MType.AIR_FILER, 1000); // Air filter every 1000 hours
        typicalIntervals.put(MType.OIL_FILTER, 750); // Oil filter every 750 hours
        typicalIntervals.put(MType.TIRES, 2000); // Tires every 2000 hours

        // Calculate the average interval for each maintenance type based on historical data
        Map<MType, Integer> averageIntervals = new HashMap<>();
        for (MType mType : maintenanceByType.keySet()) {
            List<Maintenance> typeMaintenances = maintenanceByType.get(mType);
            if (typeMaintenances.size() < 2) {
                // If there's only one record, use the typical interval
                averageIntervals.put(mType, typicalIntervals.getOrDefault(mType, 500));
                continue;
            }

            // Sort by maintenance date to calculate intervals
            typeMaintenances.sort(Comparator.comparing(Maintenance::getMaintainDate));
            int totalInterval = 0;
            for (int i = 1; i < typeMaintenances.size(); i++) {
                int interval = typeMaintenances.get(i).getNbWHMainatain() - typeMaintenances.get(i - 1).getNbWHMainatain();
                totalInterval += interval;
            }
            averageIntervals.put(mType, totalInterval / (typeMaintenances.size() - 1));
        }

        // Find the last maintenance for each type and predict the next one
        Map<MType, Integer> hoursUntilNext = new HashMap<>();
        for (MType mType : MType.values()) {
            List<Maintenance> typeMaintenances = maintenanceByType.getOrDefault(mType, new ArrayList<>());
            int interval = averageIntervals.getOrDefault(mType, typicalIntervals.getOrDefault(mType, 500));

            if (typeMaintenances.isEmpty()) {
                // If no maintenance of this type has been done, assume it’s due after the typical interval
                hoursUntilNext.put(mType, interval - resource.getNbWorkHours());
            } else {
                // Get the last maintenance of this type
                Maintenance lastMaintenance = typeMaintenances.stream()
                        .max(Comparator.comparing(Maintenance::getMaintainDate))
                        .get();
                int hoursSinceLast = resource.getNbWorkHours() - lastMaintenance.getNbWHMainatain();
                hoursUntilNext.put(mType, interval - hoursSinceLast);
            }
        }

        // Find the maintenance type that’s due the soonest
        MType nextMaintenanceType = null;
        int minHoursUntilMaintenance = Integer.MAX_VALUE;
        for (MType mType : hoursUntilNext.keySet()) {
            int hours = hoursUntilNext.get(mType);
            if (hours < minHoursUntilMaintenance) {
                minHoursUntilMaintenance = hours;
                nextMaintenanceType = mType;
            }
        }

        // Generate the prediction message
        String message;
        if (minHoursUntilMaintenance <= 0) {
            message = "Maintenance overdue! Schedule " + nextMaintenanceType + " immediately.";
        } else {
            message = "Schedule " + nextMaintenanceType + " maintenance in " + minHoursUntilMaintenance + " hours.";
        }

        return new MaintenancePredictionDTO(
                resource.getBrande(),
                nextMaintenanceType != null ? nextMaintenanceType.toString() : "N/A",
                minHoursUntilMaintenance,
                message
        );
    }
    @Override
    public String createCheckoutSession(String userId) throws Exception {
        try {
            String stripeSecretKey = "sk_test_51M7hARHJtiyIQHNyyh2zWynv637KZhopDd0YIfeYv6VERix6VeWfqJ8wqbnF8dYO5irvoO1UeY9D8b7yWLkrrpGW00WTD7iDum";
            Stripe.apiKey = stripeSecretKey;

            // Updated frontend URL to match Angular route
            String frontendUrl = "http://localhost:4200/client";

            SessionCreateParams params = SessionCreateParams.builder()
                    .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(frontendUrl + "/wheel-roulette?session_id={CHECKOUT_SESSION_ID}")
                    .setCancelUrl(frontendUrl + "/wheel-roulette")
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(1L)
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency("usd")
                                                    .setUnitAmount(1_000_000L)
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName("Wheel Roulette Spin")
                                                                    .build()
                                                    )
                                                    .build()
                                    )
                                    .build()
                    )
                    .putMetadata("userId", userId)
                    .build();

            Session session = Session.create(params);
            return session.getId();
        } catch (StripeException e) {
            throw new Exception("Failed to create checkout session: " + e.getMessage());
        }
    }


    @Override
    public boolean verifyPayment(String sessionId) throws Exception {
        try {
            String stripeSecretKey = "sk_test_51M7hARHJtiyIQHNyyh2zWynv637KZhopDd0YIfeYv6VERix6VeWfqJ8wqbnF8dYO5irvoO1UeY9D8b7yWLkrrpGW00WTD7iDum";
            Stripe.apiKey = stripeSecretKey;
            Session session = Session.retrieve(sessionId);
            return "paid".equals(session.getPaymentStatus());
        } catch (StripeException e) {
            throw new Exception("Failed to verify payment: " + e.getMessage());
        }
    }
    @Override
    @Transactional
    @Scheduled(cron = "0 * * * * ?") // Keep for testing
    public void checkAndReleaseExpiredAffections() {
        logger.info("Starting checkAndReleaseExpiredAffections");
        Date currentDate = new Date();
        logger.info("Current date: " + currentDate);
        List<Affection> expiredAffections = affectationRepository.findByEndDateLessThanEqual(currentDate);
        logger.info("Found " + expiredAffections.size() + " expired affections");

        for (Affection affection : expiredAffections) {
            logger.info("Processing affection ID: " + affection.getId() + ", endDate: " + affection.getEndDate());
            Resource resource = affection.getResource();
            if (resource != null && resource.getRtype() == Rtype.MACHINE) {
                logger.info("Machine ID: " + resource.getId() + ", current status: " + resource.getStatus());
                // Add work hours to machine
                if (affection.getWorkHours() != null) {
                    int currentWorkHours = resource.getNbWorkHours() != null ? resource.getNbWorkHours() : 0;
                    resource.setNbWorkHours(currentWorkHours + affection.getWorkHours());
                    logger.info("Updated nbWorkHours to: " + resource.getNbWorkHours());
                } else {
                    logger.warning("workHours is null for affection ID: " + affection.getId());
                }
                // Set machine as available
                resource.setStatus(true);
                resourceRepository.save(resource);
                logger.info("Set machine status to true for ID: " + resource.getId());
                // Remove the affection
                affectationRepository.delete(affection);
                logger.info("Deleted affection ID: " + affection.getId());
            } else {
                logger.warning("Skipping affection ID: " + affection.getId() + ", resource is null or not a MACHINE");
            }
        }
        logger.info("Finished checkAndReleaseExpiredAffections");
    }
}
