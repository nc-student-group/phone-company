import {Component, OnInit} from "@angular/core";
import {UserService} from "../../shared/user.service";

@Component({
  templateUrl: 'users-list.component.html'
})
export class UsersListComponent implements OnInit {

  private users: any[];

  constructor(private userService: UserService) {
  }

  ngOnInit(): void {
    this.userService.getUsers()
      .subscribe((users: any[]) => {
        console.log('Users from the server: ' + users);
        this.users = users;
      })
  }
}
