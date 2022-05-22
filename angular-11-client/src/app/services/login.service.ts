import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Token } from '../models/token.model';
import { AppService } from 'src/app/app.service';

//import * as dotenv from 'dotenv';

//const baseUrl = 'http://localhost:8080'; //process.env.REPOSITORY_BACKEND||'http://localhost:8080/api/repositories';
const baseUrl = 'http://harness-demo.site/spring-boot-server';


@Injectable({
  providedIn: 'root'
})
export class LoginService {


  constructor(private http: HttpClient,private app: AppService) {

  }
  
  //http://ip-api.com/json
  
  getLocation(): Observable<Object> {
  
    return this.http.get<Object>(`http://ip-api.com/json`);
  
  }
  
  login(data: any): Observable<Token> {

    return this.http.post<Token>(`${baseUrl}/signin`,data);
  }



  
}
