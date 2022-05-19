import { Component, OnInit } from '@angular/core';
import { AppService } from '../../app.service';
import { HttpClient } from '@angular/common/http';
import { Message } from 'src/app/models/message.model';
import { MessageService } from 'src/app/services/message.service';

@Component({
    templateUrl: './home.component.html'
})
export class HomeComponent {

    title = 'Demo';
    greeting: Message = {};

    constructor(private app: AppService, private http: HttpClient, private messageService: MessageService) {
        //http.get('http://localhost:8080/api/').subscribe(data => this.greeting = data);
        http.get('http://harness-demo.site/spring-boot-server/api/').subscribe(data => this.greeting = data);
    }

    authenticated() { return this.app.authenticated; }

}