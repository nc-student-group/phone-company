import {Component, Inject, OnInit} from '@angular/core'
import {FormGroup, FormControl, Validators, Form} from "@angular/forms";
import {Router} from "@angular/router";

@Component({
  templateUrl: './create-user.component.html',
  styleUrls: ['./create-user.css']
})
export class CreateUserComponent implements OnInit {
  createUserForm: FormGroup;
  userName: FormControl;
  password: FormControl;
  email: FormControl;

  constructor(private router: Router) {
  }

  ngOnInit(): void {
    this.userName = new FormControl('', Validators.required);
    this.password = new FormControl('', Validators.required);

    this.createUserForm = new FormGroup({
      userName: this.userName,
      password: this.password
    });
  }

  createUser(formValues) {
    console.log('User service stub');
  }

  isFieldValid(field: any) {
    return field.valid || field.pristine;
  }
}
