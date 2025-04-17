import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
export interface Resource {
  id: number;
  plateNumber: string;
  brande: string;
  price: number;
  rtype: Rtype;
  quantity:number;
  monthsPay:number;
  status:number;
 nbWorkHours:number;

}
export enum Rtype {
  TOOL = "TOOL",
  MACHINE = "MACHINE",
  MATERIALS = "MATERIALS"
}
export enum Mtype {
  OIL = "OIL",
  AIR_FILER = "AIR_FILER",
  OIL_FILTER = "OIL_FILTER",
  TIRES = "TIRES"
}
export interface Maintenance {
  id:number;
  description: string;
  cost: number;
  maintainDate: Date|'';
  nbWHMainatain: number;
  mtype: Mtype|'';
}

@Injectable({
  providedIn: 'root'
})
export class ResourceService {

  private brandImages: { [key: string]: string } = {
    //image for machine by brand
    'Backhoe Loader': 'assets/images/Backhoe Loader.jpg',
    'Excavator': 'assets/images/Excavator.jpg',
    'cement truck': 'assets/images/cement truck.jpg',
    'Dozer': 'assets/images/Dozer.jpg',
    'bobcat': 'assets/images/bobcat.jpg',
    'tower' : 'assets/images/tower crane.jpg',

    //images for tools
    'Bucket' : 'assets/images/Bucket.jpg',
    'Drill' : 'assets/images/Drill.png',
    'Hammer' : 'assets/images/Hammer.jpg',
    'Level' : 'assets/images/Level.jpg',
    'Meter' : 'assets/images/Meter.jpg',
    'Trowel' : 'assets/images/Trowel.jpg',
   //images for materials
    'Steel' : 'assets/images/Steel.jpg',
    'bricks' : 'assets/images/brick.jpg',
    'concrete' : 'assets/images/ciment.jpg',
    'concrete stones' : 'assets/images/concrete stones.jpg',
    'sand' : 'assets/images/sand.jpg',
  };
  private apiUrl = 'http://localhost:8089/resource';

  constructor(private http: HttpClient) {}

  getResources(): Observable<Resource[]> {
    return this.http.get<Resource[]>(`${this.apiUrl}/getallresource`);
  }
  getResourceById(id: number): Observable<Resource> {
    return this.http.get<Resource>(`${this.apiUrl}/getresourcebyid/${id}`);
  }

  addResource(resource: Resource): Observable<Resource> {
    return this.http.post<Resource>(`${this.apiUrl}/addresource`, resource);
  }
  getBrandImage(brand: string): string {
    return this.brandImages[brand] || 'assets/images/default-car.jpg';
  }
  updateResource(resource: Resource): Observable<Resource> {
    return this.http.put<Resource>(`${this.apiUrl}/updateresource`, resource);
  }

  removeResource(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/removeresource/${id}`);
  }
  getAllMaintenanceOfMachine(id: number): Observable<Maintenance[]> {
    return this.http.get<Maintenance[]>(`${this.apiUrl}/getAllMainatainOfMachine/${id}`);
  }
  addMaintenance(id: number, maintenance: Maintenance): Observable<Maintenance> {
    return this.http.post<Maintenance>(`${this.apiUrl}/affectMaintain/${id}`, maintenance);
  }
  removeMaintenance(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/removeMaintain/${id}`);
  }
}
