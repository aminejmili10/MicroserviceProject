package tn.esprit.springproject.Controllers;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.springproject.Entities.*;
import tn.esprit.springproject.Services.IResourceSerivce;
import tn.esprit.springproject.Services.TwitterService;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/resource")
public class ResourceController {
    private final IResourceSerivce iResourceSerivce;
    @Autowired
    private TwitterService twitterService;
    @PostMapping("/addresource")
    public ResponseEntity<?> addResource(@RequestBody Resource resource) {
        try {
            Resource savedResource = iResourceSerivce.addResource(resource);
            return ResponseEntity.ok(savedResource);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping("/getallresource")
    public List<Resource>getAllResource(){
        return iResourceSerivce.getAllResource();
    }
    @GetMapping("/getresourcebyid/{id}")
    public Resource getResourceById(@PathVariable Integer id){
        return iResourceSerivce.getResourceById(id);
    }
    @PutMapping("/updateresource")
    public ResponseEntity<?> updateResource(@RequestBody Resource resource) {
        try {
            Resource updatedResource = iResourceSerivce.updateResource(resource);
            return ResponseEntity.ok(updatedResource);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @DeleteMapping("/removeresource/{id}")
    public void removeResource(@PathVariable Integer id){
        iResourceSerivce.removeResource(id);
    }

    @PostMapping("/affectMaintain/{id}")
    public Resource affectMaintain(@PathVariable Integer id, @RequestBody Maintenance maintenance){
        return iResourceSerivce.affectMaintainToMachine(id,maintenance);
    }
    @GetMapping("/getAllMainatainOfMachine/{id}")
    public List<Maintenance> getAllMaintainOfMachine(@PathVariable Integer id){
        return iResourceSerivce.getAllMaintainOfMachine(id);
    }

    @DeleteMapping("/removeMaintain/{id}")
    public void removeMaintain(@PathVariable Integer id){
        iResourceSerivce.removeMaintain(id);
    }

    @PostMapping("/affecttoproject/{idr}/{idp}")
    public Resource affectResourceToProject(@PathVariable Integer idr, @PathVariable Integer idp, @RequestBody Affection affection){
        return iResourceSerivce.affectResourcetoProject(idr,idp,affection);
    }

    @GetMapping("/getAllAffectations")
    public List<AffectationDTO> getAllAffectations() {
        return iResourceSerivce.getAllAffectationDTOs();
    }
    @DeleteMapping("/removeAffectation/{id}")
    public void removeAffectation(@PathVariable Integer id) {
        iResourceSerivce.removeAffectation(id);
    }

    @GetMapping("/getallprojects")
    public List<Project> getAllProjects() {
        return iResourceSerivce.getAllProjects();
    }

    @GetMapping("/maintenance-stats")
    public List<MaintenanceStats> getMaintenanceStatsForMachines() {
        return iResourceSerivce.getMaintenanceStatsForMachines();
    }
    @PostMapping("/addproject")
    public ResponseEntity<Project> addProject(@RequestBody Project project) {
        Project savedProject = iResourceSerivce.addProject(project);
        return ResponseEntity.ok(savedProject);
    }
    @PutMapping("/updateAffectationQuantity/{id}")
    public ResponseEntity<Void> updateAffectationQuantity(@PathVariable Integer id, @RequestBody QuantityUpdateRequest request) {
        iResourceSerivce.updateAffectationQuantity(id, request.getQuantity());
        return ResponseEntity.ok().build();
    }

    // Helper class for the request body
    static class QuantityUpdateRequest {
        private int quantity;

        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }
    @PostMapping("/publish-equipment")
    public ResponseEntity<String> publishEquipment(@RequestBody TweetRequest tweetRequest) {
        try {
            String tweet = tweetRequest.getTitle(); // Use title directly as tweet content
            if (tweet.length() > 280) {
                tweet = tweet.substring(0, 277) + "...";
            }
            String response = twitterService.postTweet(tweet, tweetRequest.getImageUrl());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error posting tweet: " + e.getMessage());
        }
    }

    static class TweetRequest {
        private String title;
        private String price;
        private String imageUrl;

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getPrice() { return price; }
        public void setPrice(String price) { this.price = price; }
        public String getImageUrl() { return imageUrl; }
        public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    }
    @PostMapping("/test-tweet")
    public ResponseEntity<String> testTweet() {
        try {
            String response = twitterService.postTweet("first twitt", null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error posting tweet: " + e.getMessage());
        }
    }
    @GetMapping("/predict-maintenance/{id}")
    public ResponseEntity<MaintenancePredictionDTO> predictNextMaintenance(@PathVariable Integer id) {
        MaintenancePredictionDTO prediction = iResourceSerivce.predictNextMaintenance(id);
        return ResponseEntity.ok(prediction);
    }

    @PostMapping("/create-checkout-session")
    public ResponseEntity<?> createCheckoutSession(@RequestBody CheckoutRequest checkoutRequest) {
        try {
            String sessionId = iResourceSerivce.createCheckoutSession(checkoutRequest.getUserId());
            return ResponseEntity.ok(new CheckoutResponse(sessionId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating checkout session: " + e.getMessage());
        }
    }

    @GetMapping("/verify-payment/{sessionId}")
    public ResponseEntity<?> verifyPayment(@PathVariable String sessionId) {
        try {
            boolean isPaid = iResourceSerivce.verifyPayment(sessionId);
            return ResponseEntity.ok(new PaymentVerificationResponse(isPaid));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error verifying payment: " + e.getMessage());
        }
    }

    // Helper classes
    static class CheckoutRequest {
        private String userId;

        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
    }

    static class CheckoutResponse {
        private String sessionId;

        public CheckoutResponse(String sessionId) { this.sessionId = sessionId; }
        public String getSessionId() { return sessionId; }
        public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    }

    static class PaymentVerificationResponse {
        private boolean isPaid;

        public PaymentVerificationResponse(boolean isPaid) { this.isPaid = isPaid; }
        public boolean isPaid() { return isPaid; }
        public void setPaid(boolean paid) { isPaid = paid; }
    }
}

