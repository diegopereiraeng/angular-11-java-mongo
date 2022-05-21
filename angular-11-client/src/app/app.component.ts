import { Component, SimpleChanges, OnInit } from '@angular/core';

import { AppService } from './app.service';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

import { finalize } from "rxjs/operators";

// Feature Flags
import {  Event  } from '@harnessio/ff-javascript-client-sdk'
import { FFService } from 'src/app/services/ff.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  title = 'Angular 11 Crud';
  appRepositoryEnabled = false;
  isTracking = true;
  currentLat = 0;
  currentLong = 0;
  loggedIn = false;
  list: any = [];

  
  authenticated() { 
    this.loggedIn = this.app.authenticated;
    if (this.loggedIn) {
      
    }
    return this.app.authenticated;
  }


  constructor(private app: AppService, private http: HttpClient, private router: Router, private ff: FFService) {
    //this.app.authenticate(undefined, undefined);

    this.registerFlag = this.registerFlag.bind(this);


    // New implementation ff -> lot better than
    ff.SetFlags('App_Title',"Harness");
  }

  getTitle(): string {
    return String(this.ff.GetFlags('App_Title'));
  }

  setRepositoryEnable(value: boolean){
    this.appRepositoryEnabled = value;
  }

  allowRepository(){
    console.log("allowRepository = "+this.appRepositoryEnabled)
    return this.appRepositoryEnabled;
  }

  logout() {
    //this.http.post('http://localhost:8080/logout', {}).pipe(
    this.http.post('http://harness-demo.site/spring-boot-server/logout', {}).pipe(
      finalize(() => {
        this.app.authenticated = false;
        this.router.navigateByUrl('/login');
      })
      ).subscribe();
  }

  ngOnChanges(changes: SimpleChanges) {
    this.authenticated()
    // only run when property "data" changed
    console.log("Changes ocurred "+ changes)
    if (changes['appRepositoryEnabled']) {
      console.log("user logged in")
      console.log(this.appRepositoryEnabled)


    }
  }


  async ngOnInit(): Promise<void> {
    console.log("App Components: FF Starting")
    
    this.appRepositoryEnabled = this.ff.GetFlags("repositoryEnabled")
    console.log("flag repository enabled. value = "+ this.appRepositoryEnabled);

    this.ff.registerEvent(Event.CHANGED, this.registerFlag);

  }

  registerFlag(flag: any): void {
    console.log("registerFlag event");
    console.log("flag: "+flag.flag+" value: "+flag.value);
    if (flag.flag === "Repositories") {
      console.log("flag: "+flag.flag+" value: "+flag.value);
      const value: boolean = flag.value;
      
      this.setRepositoryEnable(value);
    } 
  }
}
