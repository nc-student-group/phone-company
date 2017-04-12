import {CreateUserComponent} from "./user/create-user/create-user.component";
import {Routes} from "@angular/router";
import {AuthenticationComponent} from "./user/authentication/authentication.component";

export const appRoutes: Routes = [
  {path: '', redirectTo: 'authenticate', pathMatch: 'full'},
  {path: 'create-user', component: CreateUserComponent},
  {path: 'authenticate', component: AuthenticationComponent}
];
