import { APP_INITIALIZER,NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './front_off/Client/home/home.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { PaymentComponent } from './front_off/Client/payment/payment/payment.component';
import { ProjectComponent } from './front_off/Client/project/project/project.component';
import { SheduleComponent } from './front_off/Client/schedule/shedule/shedule.component';
import { NavclComponent } from './front_off/Client/navcl/navcl.component';
import { ResourceComponent } from './front_off/Manager/resource/resource/resource.component';
import { DocumentComponent } from './front_off/Client/document/document/document.component';
import { EquipmentDetailsComponent } from './front_off/Manager/resource/equipment-details/equipment-details.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HTTP_INTERCEPTORS,HttpClient, HttpClientModule} from "@angular/common/http";
import {KeycloakService} from "./services/keycloak/keycloak.service";
import {HttpTokenInterceptor} from './services/interceptor/http-token.interceptor';
import { NavadminComponent } from './back_off/admin/navadmin/navadmin.component';
import { AddresourceComponent } from './back_off/admin/Resource/addresource/addresource.component';
import { ResourcedetailsComponent } from './front_off/Manager/resource/resourcedetails/resourcedetails.component';
import { ShowadminresourceComponent } from './back_off/admin/Resource/showadminresource/showadminresource.component';
import { ResourceadmindetailsComponent } from './back_off/admin/Resource/resourceadmindetails/resourceadmindetails.component';
import { NavManagerComponent } from './front_off/Manager/nav-manager/nav-manager.component';
import { ManagersheduleComponent } from './front_off/Manager/shedule/managershedule/managershedule.component';
import { ManagertaskComponent } from './front_off/Manager/shedule/managertask/managertask.component';
import { AdminViewComponent } from './back_off/admin/blog/admin-view/admin-view.component';
import { RouterModule } from '@angular/router';
import { CommonModule, DatePipe } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatCardModule } from '@angular/material/card';
import { MatChipsModule } from '@angular/material/chips';
import { CreatePostComponent } from './front_off/Client/blog/create-post/create-post.component';
import { ViewAllComponent } from './front_off/Client/blog/view-all/view-all.component';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { NgxPaginationModule } from 'ngx-pagination';
import { EditPostComponent } from './front_off/Client/blog/edit-post/edit-post.component';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatGridListModule } from '@angular/material/grid-list';
import { AngularMaterialModule } from './angular-material.module';

import { ToastrModule } from 'ngx-toastr';
import { MatProgressBarModule } from '@angular/material/progress-bar';

import { MatSelectModule } from '@angular/material/select';
import { ViewPostComponent } from './front_off/Client/blog/view-post/view-post.component';
import { PostDetailComponent } from './back_off/admin/blog/post-detail/post-detail.component';
export function kcFactory(kcService:KeycloakService){
  return () => kcService.init();
}
@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    PaymentComponent,
    ProjectComponent,
    SheduleComponent,
    NavclComponent,
    ResourceComponent,
    DocumentComponent,
    EquipmentDetailsComponent,
    NavadminComponent,
    AddresourceComponent,
    ResourcedetailsComponent,
    ShowadminresourceComponent,
    ResourceadmindetailsComponent,
    NavManagerComponent,
    ManagersheduleComponent,
    ManagertaskComponent,
   
    AdminViewComponent,
    CreatePostComponent,
    ViewAllComponent,
    ViewPostComponent,
    PostDetailComponent,
  
   
 
    EditPostComponent
   
   
   
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule,
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    ReactiveFormsModule,  // Ajouté pour les formulaires réactifs
    FormsModule,          // Ajouté pour les formulaires
    HttpClientModule,
     MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatChipsModule,
    MatIconModule,
    MatToolbarModule,     // Ajouté pour mat-toolbar
    MatButtonModule,      // Ajouté pour les boutons
    MatSnackBarModule,    // Ajouté si vous utilisez MatSnackBar
   MatGridListModule,
   NgxPaginationModule,
    CommonModule,
    RouterModule,
    AngularMaterialModule,
    MatProgressBarModule,
    ToastrModule.forRoot(),
    MatSelectModule,
    

    
  ],
  providers: [/*HttpClient,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpTokenInterceptor,
      multi: true
    },
    {
      provide:APP_INITIALIZER,
      deps:[KeycloakService],
      useFactory:kcFactory,
      multi:true
    }
  */
    [DatePipe],
  ],
  
  bootstrap: [AppComponent]
})
export class AppModule { }
