import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpModule} from '@angular/http';
import {CreateUserComponent} from "./user/create-user/create-user.component";
import {appRoutes} from "./router";
import {RouterModule} from "@angular/router";
import {PhoneCompanyComponent} from "./phone-company.component";
import {AuthenticationComponent} from "./user/authentication/authentication.component";

@NgModule({
  imports: [
    BrowserModule,
    RouterModule.forRoot(appRoutes),
    HttpModule,
    FormsModule,
    ReactiveFormsModule
  ],
  declarations: [
    PhoneCompanyComponent,
    CreateUserComponent,
    AuthenticationComponent
  ],
  providers: [],
  bootstrap: [PhoneCompanyComponent]
})
export class AppModule {
}
