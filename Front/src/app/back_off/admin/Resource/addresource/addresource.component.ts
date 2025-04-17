import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ResourceService, Rtype } from "../../../../services/ResourceService/resource.service";

@Component({
  selector: 'app-addresource',
  templateUrl: './addresource.component.html',
  styleUrls: ['./addresource.component.css']
})
export class AddresourceComponent implements OnInit {
  machineForm!: FormGroup;
  toolForm!: FormGroup;
  selectedForm: string = '';
  machineBrands: string[] = ['Backhoe Loader', 'Excavator', 'cement truck', 'Dozer', 'bobcat', 'tower'];
  toolBrands: string[] = ['Bucket', 'Drill', 'Hammer', 'Level', 'Meter', 'Trowel'];
  materialBrands: string[] = ['Steel', 'bricks', 'concrete', 'concrete stones', 'sand'];
  constructor(private fb: FormBuilder, public resourceService: ResourceService) {}
  ngOnInit(): void {
    this.initializeForms();
  }

  initializeForms(): void {
    this.machineForm = this.fb.group({
      plateNumber: ['', Validators.required],
      brande: ['', Validators.required],
      price: [null, [Validators.required, Validators.min(1)]],
      nbWorkHours: [null, [Validators.required, Validators.min(1)]],
      monthsPay: [null, [Validators.required, Validators.min(1)]]
    });

    this.toolForm = this.fb.group({
      type: ['TOOL', Validators.required],
      brande: ['', Validators.required],
      price: [null, [Validators.required, Validators.min(1)]],
      quantity: [null, [Validators.required, Validators.min(1)]]
    });
  }

  showForm(formType: string): void {
    this.selectedForm = formType;
  }

  onSubmitMachine(): void {
    if (this.machineForm.valid) {
      const formData = { ...this.machineForm.value, rtype: 'MACHINE' };
      console.log("Sending Machine Data:", formData);
      this.resourceService.addResource(formData).subscribe({
        next: (response) => {
          console.log("Machine added successfully:", response);
          this.machineForm.reset();
          this.selectedForm = "";
        },
        error: (error) => {
          console.error("Error adding machine:", error);
          alert("Failed to add machine.");
        }
      });
    }
  }

  onSubmitTool(): void {
    if (this.toolForm.valid) {
      const formData = { ...this.toolForm.value, rtype: this.toolForm.value.type };
      console.log("Sending Tool/Material Data:", formData);
      this.resourceService.addResource(formData).subscribe({
        next: (response) => {
          console.log("Tool/Material added successfully:", response);
          this.toolForm.reset();
          this.selectedForm = "";
        },
        error: (error) => {
          console.error("Error adding tool/material:", error);
          alert("Failed to add tool/material.");
        }
      });
    }
  }

  // Helper function to check field errors
  isInvalid(form: FormGroup, field: string): boolean {
    return !!(form.get(field)?.invalid && (form.get(field)?.touched || form.get(field)?.dirty));
  }
}
