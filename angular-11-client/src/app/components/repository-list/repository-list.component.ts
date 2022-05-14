import { Component, OnInit } from '@angular/core';
import { Repository } from 'src/app/models/repository.model';
import { RepositoryService } from 'src/app/services/repository.service';

// Feature Flags
import { initialize, Event } from '@harnessio/ff-javascript-client-sdk'
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

  constructor(private repositoryService: RepositoryService) { 
    console.log("App Starting")
  }

  SetFlags(flag: String, value: boolean ): void {
    this.repositoryEnabled = value;
  }

  

  async ngOnInit(): Promise<void> {
    //dotenv.config();
    console.log("FF Starting")
    let sdkKey = "e2148669-99aa-42e8-9a29-bd339c7ca5a8"; //process.env.FF_SDK_KEY || "e2148669-99aa-42e8-9a29-bd339c7ca5a8"
    const cf = initialize(sdkKey, {
      identifier: "Global",      // Target identifier
      name: "Global",                  // Optional target name
      attributes: {                            // Optional target attributes
        email: 'global@harness.io'
      }
    });

    cf.on(Event.READY, flags => {
      console.log(JSON.stringify(flags, null, 2))
      console.log(flags["Repositories"]);
      this.repositoryEnabled = Boolean(flags["Repositories"]);
      if (this.repositoryEnabled) {
        this.retrieveRepositories();
      }
      else {
        console.log("Not Authorized");
      }
      
    })
    
    /* console.log("FF Initialized")
    const repositories = cf.variation('Repositories', false);
    
    const result = JSON.stringify(repositories);
    console.log("FF working! -> Result = "+repositories);
    console.log("FF working! -> Result = "+Boolean(repositories)); */
    
    /* if (repositories) {
      this.retrieveRepositories();
    }   */
    
    
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
