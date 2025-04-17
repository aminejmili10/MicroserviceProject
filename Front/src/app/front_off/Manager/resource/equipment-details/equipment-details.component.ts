import { Component } from '@angular/core';
import {Maintenance, ResourceService} from "../../../../services/ResourceService/resource.service";
import {ActivatedRoute} from "@angular/router";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-equipment-details',
  templateUrl: './equipment-details.component.html',
  styleUrls: ['./equipment-details.component.css']
})
export class EquipmentDetailsComponent {
  resourceId!: number;
  maintenanceRecords: Maintenance[] = [];
  totalCost: number = 0;
  showAddForm: boolean = false;
  lastWorkHours: number = 0; // To store the last maintenance work hours
  maintenanceForm!: FormGroup; // Reactive form group

  constructor(
    private route: ActivatedRoute,
    private resourceService: ResourceService,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.resourceId = Number(this.route.snapshot.paramMap.get('id'));

    // Initialize the form group
    this.maintenanceForm = this.fb.group({
      mtype: ['', Validators.required],
      description: ['', Validators.required],
      cost: [0, [Validators.required, Validators.min(1)]],
      maintainDate: ['', Validators.required],
      nbWHMainatain: [0, [Validators.required, Validators.min(1)]]
    });

    this.resourceService.getAllMaintenanceOfMachine(this.resourceId).subscribe(
      (data) => {
        this.maintenanceRecords = data;
        this.calculateTotalCost();
        this.setLastWorkHours(); // Set last maintenance's work hours
      },
      (error) => {
        console.error('Error fetching maintenance records:', error);
      }
    );
  }

  setLastWorkHours(): void {
    if (this.maintenanceRecords.length > 0) {
      const lastMaintenance = this.maintenanceRecords[this.maintenanceRecords.length - 1];
      this.lastWorkHours = lastMaintenance.nbWHMainatain;
    }
  }

  // Method to calculate the total cost
  calculateTotalCost(): void {
    this.totalCost = this.maintenanceRecords.reduce((acc, maintenance) => acc + maintenance.cost, 0);
  }

  // Toggle the visibility of the add maintenance form
  toggleAddForm(): void {
    this.showAddForm = !this.showAddForm;
  }

  // Submit the new maintenance form
  submitNewMaintenance(): void {
    if (this.maintenanceForm.valid) {
      this.resourceService.addMaintenance(this.resourceId, this.maintenanceForm.value).subscribe(
        (newMaintenance) => {
          // Refresh the maintenance records by fetching the latest list
          this.resourceService.getAllMaintenanceOfMachine(this.resourceId).subscribe(
            (data) => {
              this.maintenanceRecords = data; // Update maintenance records
              this.calculateTotalCost(); // Recalculate total cost
              this.setLastWorkHours(); // Update last work hours
              this.maintenanceForm.reset(); // Reset the form
              this.showAddForm = false; // Hide the form
            },
            (error) => {
              console.error('Error fetching updated maintenance records:', error);
            }
          );
        },
        (error) => {
          console.error('Error adding maintenance:', error);
        }
      );
    }
  }
  deleteMaintenance(id: number, index: number): void {
    this.resourceService.removeMaintenance(id).subscribe(
      () => {
        // If successful, remove the record from the list
        this.maintenanceRecords.splice(index, 1);
        this.calculateTotalCost(); // Recalculate total cost after deletion
        this.setLastWorkHours(); // Update last work hours after deletion
      },
      (error) => {
        console.error('Error deleting maintenance:', error);
      }
    );
  }

}
