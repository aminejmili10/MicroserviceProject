package tn.esprit.springproject.Entities;

public class MaintenancePredictionDTO {
    private String machineBrand;
    private String predictedMaintenanceType;
    private Integer hoursUntilMaintenance;
    private String message;

    public MaintenancePredictionDTO(String machineBrand, String predictedMaintenanceType, Integer hoursUntilMaintenance, String message) {
        this.machineBrand = machineBrand;
        this.predictedMaintenanceType = predictedMaintenanceType;
        this.hoursUntilMaintenance = hoursUntilMaintenance;
        this.message = message;
    }

    // Getters and Setters
    public String getMachineBrand() {
        return machineBrand;
    }

    public void setMachineBrand(String machineBrand) {
        this.machineBrand = machineBrand;
    }

    public String getPredictedMaintenanceType() {
        return predictedMaintenanceType;
    }

    public void setPredictedMaintenanceType(String predictedMaintenanceType) {
        this.predictedMaintenanceType = predictedMaintenanceType;
    }

    public Integer getHoursUntilMaintenance() {
        return hoursUntilMaintenance;
    }

    public void setHoursUntilMaintenance(Integer hoursUntilMaintenance) {
        this.hoursUntilMaintenance = hoursUntilMaintenance;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}