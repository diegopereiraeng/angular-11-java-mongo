import { Component, OnInit } from '@angular/core';
import { Repository } from 'src/app/models/repository.model';
import { RepositoryService } from 'src/app/services/repository.service';

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

  constructor(private repositoryService: RepositoryService) { }

  ngOnInit(): void {
    this.retrieveRepositories();
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
