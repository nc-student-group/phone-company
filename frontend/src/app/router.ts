import {CreateUserComponent} from "./user/create-user/create-user.component";
import {Routes} from "@angular/router";

export const appRoutes: Routes = [
  {path: '', redirectTo: 'create-user', pathMatch: 'full'},
  {path: 'create-user', component: CreateUserComponent}
];
