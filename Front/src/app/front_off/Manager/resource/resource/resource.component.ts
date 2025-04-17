import {Component, OnInit} from '@angular/core';
import {Resource, ResourceService, Rtype} from "../../../../services/ResourceService/resource.service";

@Component({
  selector: 'app-resource',
  templateUrl: './resource.component.html',
  styleUrls: ['./resource.component.css']
})
export class ResourceComponent implements OnInit {
  resources: Resource[] = [];
  selectedType: Rtype = Rtype.MACHINE;
  constructor(public resourceService: ResourceService) {
  }

  ngOnInit(): void {
   this.loadResources();
  }
  loadResources(): void {
    this.resourceService.getResources().subscribe({
      next: (data) => this.resources = data,
      error: (err) => console.error('Error loading resources:', err)
    });

  }
  filterByType(type: Rtype): void {
    this.selectedType = type;
  }

  get filteredResources(): Resource[] {
    return this.resources.filter(resource => resource.rtype === this.selectedType);
  }

  isActiveType(type: Rtype): boolean {
    return this.selectedType === type;
  }

  protected readonly Rtype = Rtype;
}
