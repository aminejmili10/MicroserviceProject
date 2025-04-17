import { Component } from '@angular/core';
import {Resource, ResourceService} from "../../../../services/ResourceService/resource.service";
import {ActivatedRoute, Router} from "@angular/router";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-resourceadmindetails',
  templateUrl: './resourceadmindetails.component.html',
  styleUrls: ['./resourceadmindetails.component.css']
})
export class ResourceadmindetailsComponent {
  resource!: Resource;
  showUpdateForm = false;

  machineForm!: FormGroup;
  toolMaterialForm!: FormGroup;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    public resourceService: ResourceService,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.loadResource();
  }

  loadResource(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id) {
      this.resourceService.getResourceById(id).subscribe({
        next: (data) => {
          this.resource = data;
          this.initForms(); // Initialize forms after data is loaded
        },
        error: (err) => console.error('Error loading resource details:', err),
      });
    }
  }

  initForms(): void {
    this.machineForm = this.fb.group({
      plateNumber: [this.resource.plateNumber, Validators.required],
      price: [this.resource.price, [Validators.required, Validators.min(0)]],
      monthsPay: [this.resource.monthsPay, [Validators.required, Validators.min(0)]],
      nbWorkHours: [this.resource.nbWorkHours, [Validators.required, Validators.min(0)]]
    });

    this.toolMaterialForm = this.fb.group({
      price: [this.resource.price, [Validators.required, Validators.min(0)]],
      quantity: [this.resource.quantity, [Validators.required, Validators.min(0)]]
    });
  }

  openUpdateForm(): void {
    this.showUpdateForm = true;
  }

  updateResource(): void {
    if (this.resource.rtype === 'MACHINE' && this.machineForm.valid) {
      const updatedResource = { ...this.resource, ...this.machineForm.value };
      this.submitUpdate(updatedResource);
    } else if ((this.resource.rtype === 'TOOL' || this.resource.rtype === 'MATERIALS') && this.toolMaterialForm.valid) {
      const updatedResource = { ...this.resource, ...this.toolMaterialForm.value };
      this.submitUpdate(updatedResource);
    } else {
      alert('Please fill in all required fields correctly.');
    }
  }

  submitUpdate(updatedResource: Resource): void {
    this.resourceService.updateResource(updatedResource).subscribe({
      next: () => {
        alert('Resource updated successfully!');
        this.showUpdateForm = false;
        this.loadResource(); // Reload data
      },
      error: (err) => console.error('Error updating resource:', err),
    });
  }

  deleteResource(): void {
    if (confirm('Are you sure you want to delete this resource?')) {
      this.resourceService.removeResource(this.resource.id).subscribe({
        next: () => {
          alert('Resource deleted successfully!');
          this.router.navigate(['/admin/resource']);
        },
        error: (err) => console.error('Error deleting resource:', err),
      });
    }
  }
  cancelUpdate() {
    this.showUpdateForm = false;
  }
}
