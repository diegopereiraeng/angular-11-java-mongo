import { Component, OnInit } from '@angular/core';
import { AppService } from '../../app.service';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { LoginService } from 'src/app/services/login.service';
import { FFService } from 'src/app/services/ff.service';
import { Token } from 'src/app/models/token.model';

@Component({
  templateUrl: './login.component.html'
})
export class LoginComponent {

  credentials = {username: '', password: ''};

  error = false;
  
  token: Token = new Token;

  constructor(private app: AppService, private http: HttpClient, private router: Router,private loginService: LoginService, private ff: FFService) {
  }

  login() {

    const data = this.loginService.login(this.credentials)
      .subscribe(
        data => {
          this.token = data;
          this.app.setToken(this.token);
          this.app.authenticate(this.credentials, () => {
            this.router.navigateByUrl('/');
            this.ff.renitializeSDK();
          } );
        },
        error => {
          console.log(error);
        });

    return false;
  }

}