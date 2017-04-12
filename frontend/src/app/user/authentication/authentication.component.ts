import {Component, Inject, OnInit} from '@angular/core'
import {FormGroup, FormControl, Validators, Form} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  templateUrl: './authentication.component.html',
  styleUrls: ['./authentication.css']
})
export class AuthenticationComponent implements OnInit {
  active: string = 'login';
  loginForm: FormGroup;
  registerForm: FormGroup;
  loginFormUserName: FormControl;
  loginFormPassword: FormControl;
  registerFormUserName: FormControl;
  registerFormPassword: FormControl;
  email: FormControl;
  registrationSuccess: boolean = false;

  constructor(private router: Router,
              private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.loginFormUserName = new FormControl('', Validators.required);
    this.loginFormPassword = new FormControl('', Validators.required);

    this.loginForm = new FormGroup({
      userName: this.loginFormUserName,
      password: this.loginFormPassword
    });

    this.registerFormUserName = new FormControl('', Validators.required);
    this.registerFormPassword = new FormControl('', Validators.required);
    this.email = new FormControl('', Validators.required);

    this.registerForm = new FormGroup({
      userName: this.registerFormUserName,
      password: this.registerFormPassword,
      email: this.email
    })
  }

  isFieldValid(field: any) {
    return field.valid || field.pristine;
  }
}

