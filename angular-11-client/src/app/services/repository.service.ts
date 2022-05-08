import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Repository } from '../models/repository.model';

//const baseUrl = 'http://localhost:8080/api/repositories';
const baseUrl = 'http://harness-demo.site/spring-boot-server/api/repositories';



@Injectable({
  providedIn: 'root'
})
export class RepositoryService {

  constructor(private http: HttpClient) { }

  getAll(): Observable<Repository[]> {
    return this.http.get<Repository[]>(baseUrl);
  }

  get(id: any): Observable<Repository> {
    return this.http.get(`${baseUrl}/${id}`);
  }

  create(data: any): Observable<any> {
    return this.http.post(baseUrl, data);
  }

  update(id: any, data: any): Observable<any> {
    return this.http.put(`${baseUrl}/${id}`, data);
  }

  delete(id: any): Observable<any> {
    return this.http.delete(`${baseUrl}/${id}`);
  }

  deleteAll(): Observable<any> {
    return this.http.delete(baseUrl);
  }

  findByName(title: any): Observable<Repository[]> {
    return this.http.get<Repository[]>(`${baseUrl}?title=${title}`);
  }
}
