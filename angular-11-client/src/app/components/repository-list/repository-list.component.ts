import { Component, OnInit, SimpleChanges } from '@angular/core';
import { Repository } from 'src/app/models/repository.model';
import { FF } from 'src/app/models/ff.model';
import { RepositoryService } from 'src/app/services/repository.service';
import { FFService } from 'src/app/services/ff.service';
import { AppService } from 'src/app/app.service';
import { Router } from '@angular/router';

// Feature Flags
import { Event } from '@harnessio/ff-javascript-client-sdk'
//import * as dotenv from 'dotenv';




@Component({
  selector: 'app-repository-list',
  templateUrl: './repository-list.component.html',
  styleUrls: ['./repository-list.component.css']
})
export class RepositoriesListComponent implements OnInit {
  repositories?: Repository[];
  currentRepository?: Repository;
  currentIndex = -1;
  name = '';
  repositoryEnabled = false;

  constructor(private app: AppService,private repositoryService: RepositoryService, private ff: FFService, private router: Router) { 
    console.log("App Starting")
    ff.SetFlags('Harness_Onboarding',false);
    ff.SetFlags('Repository_Filter',false);
    /* ff.flags.push(new FF('Harness_Onboarding',false));
    ff.flags.push(new FF('Repository_Filter',false)); */
  }

  authenticated() { return this.app.authenticated; }

  SetFlags(flag: String, value: boolean ): void {
    this.repositoryEnabled = value;
  }

  onboardingEnabled(){
    console.log("onboarding enabled: "+this.ff.GetFlags('Harness_Onboarding'))
    return Boolean(this.ff.GetFlags('Harness_Onboarding'));
  }
  repositoryFilterEnabled(){
    console.log("repositoryFilter enabled: "+this.ff.GetFlags('Repository_Filter'))
    return Boolean(this.ff.GetFlags('Repository_Filter'));
  }

  ngOnChanges(changes: SimpleChanges) {
    this.authenticated()
    // only run when property "data" changed
    if (changes['repositoryEnabled']) {
      console.log("FF Service");
      console.log("Registering for realtime changes");
      if (this.repositoryEnabled) {
      
        this.retrieveRepositories();
  
      } else {
        console.log("Not Authorized to list repositories");
        this.repositories = [];
      }
      
    }
  }

  async ngOnInit(): Promise<void> {

    this.repositoryEnabled = this.ff.GetFlags("repositoryEnabled")
    console.log("flag repository enabled. value = "+ this.repositoryEnabled);
    console.log("flag repository enabled");
    if (this.repositoryEnabled) {
      
      this.retrieveRepositories();

    } else {
      console.log("Not Authorized to list repositories");
    }
    
    this.ff.cfClient.on(Event.CHANGED, flagInfo => {
      console.log("Repositories: flag changed "+flagInfo.flag);
      if (flagInfo.flag === "Repositories") {
        this.repositoryEnabled = Boolean(flagInfo.value);
      }
      
    })
  }

  retrieveRepositories(): void {
    this.repositoryService.getAll()
      .subscribe(
        data => {
          this.repositories = data;
          console.log(data);
        },
        error => {
          console.log(error);
        });
  }

  refreshList(): void {
    this.retrieveRepositories();
    this.currentRepository = undefined;
    this.currentIndex = -1;
  }

  setActiveRepository(repository: Repository, index: number): void {
    this.currentRepository = repository;
    this.currentIndex = index;
  }

  removeAllRepositories(): void {
    this.repositoryService.deleteAll()
      .subscribe(
        response => {
          console.log(response);
          this.refreshList();
        },
        error => {
          console.log(error);
        });
  }
  removeOneRepository(): void {
    this.repositoryService.delete(this.currentIndex)
      .subscribe(
        response => {
          console.log(response);
          this.refreshList();
        },
        error => {
          console.log(error);
        });
  }

  getShortName(name : string){
    let shortName:string = name;
    console.log(name);
    return shortName.substring(0, 15);
  }

  onboarding(){
    this.router.navigateByUrl('https://app.harness.io');
    
  }

  searchName(): void {
    this.currentRepository = undefined;
    this.currentIndex = -1;

    this.repositoryService.findByName(this.name)
      .subscribe(
        data => {
          this.repositories = data;
          console.log(data);
        },
        error => {
          console.log(error);
        });
  }

}
