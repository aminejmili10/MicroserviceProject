import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './front_off/Client/home/home.component';

import { PaymentComponent } from './front_off/Client/payment/payment/payment.component';
import { ProjectComponent } from './front_off/Client/project/project/project.component';
import { SheduleComponent } from './front_off/Client/schedule/shedule/shedule.component';
import { ResourceComponent } from './front_off/Manager/resource/resource/resource.component';
import { DocumentComponent } from './front_off/Client/document/document/document.component';
import { EquipmentDetailsComponent } from './front_off/Manager/resource/equipment-details/equipment-details.component';
import {NavadminComponent} from "./back_off/admin/navadmin/navadmin.component";
import {AddresourceComponent} from "./back_off/admin/Resource/addresource/addresource.component";
import {ResourcedetailsComponent} from "./front_off/Manager/resource/resourcedetails/resourcedetails.component";
import {ShowadminresourceComponent} from "./back_off/admin/Resource/showadminresource/showadminresource.component";
import {
  ResourceadmindetailsComponent
} from "./back_off/admin/Resource/resourceadmindetails/resourceadmindetails.component";
import {ManagersheduleComponent} from "./front_off/Manager/shedule/managershedule/managershedule.component";
import {ManagertaskComponent} from "./front_off/Manager/shedule/managertask/managertask.component";
import { AdminViewComponent } from './back_off/admin/blog/admin-view/admin-view.component';

import { CreatePostComponent } from './front_off/Client/blog/create-post/create-post.component';
import { ViewAllComponent } from './front_off/Client/blog/view-all/view-all.component';

import { EditPostComponent } from './front_off/Client/blog/edit-post/edit-post.component';

import { NavManagerComponent } from './front_off/Manager/nav-manager/nav-manager.component';
import { ViewPostComponent } from './front_off/Client/blog/view-post/view-post.component';
import { PostDetailComponent } from './back_off/admin/blog/post-detail/post-detail.component';

const routes: Routes = [
  {
    path: 'client',
    children: [
      { path: 'home', component: HomeComponent },
     // { path: 'blog', component: BlogComponent },
      { path: 'payment', component: PaymentComponent },
      { path: 'project', component: ProjectComponent },
      { path: 'shedule', component: SheduleComponent },
      { path: 'document', component: DocumentComponent },
      {path:'blog/create-post',component:CreatePostComponent},
      {path:'blog/view-all',component:ViewAllComponent},
      {path:'blog/view-post/:id',component:ViewPostComponent},
      { path: 'blog/edit-post/:id', component: EditPostComponent },
     
  
      
      { path: '', redirectTo: 'home', pathMatch: 'full' }
    ]
  },
  
  { path: 'Manager', component: NavManagerComponent},

  { path: 'Manager/schedule', component: ManagersheduleComponent},
  {path: 'Manager/task', component: ManagertaskComponent},
  { path: 'resource', component: ResourceComponent },
  { path: 'equipmentDetails/:id', component: EquipmentDetailsComponent },
  { path: 'resourceDetails/:id', component: ResourcedetailsComponent },
  {path:'admin',component:NavadminComponent},
  {path:'admin/resource',component:ShowadminresourceComponent},
  {path:'admin/resourceDetails/:id',component:ResourceadmindetailsComponent},
  { path: 'admin/blog', component: AdminViewComponent },
  { path: 'admin/post/:id', component: PostDetailComponent },

 { path: '', component: AdminViewComponent },
 

 

  
  {path:'addresource',component:AddresourceComponent},
  { path: '', redirectTo: '/client/home', pathMatch: 'full' },
  { path: '**', redirectTo: '/client/home' },


];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
