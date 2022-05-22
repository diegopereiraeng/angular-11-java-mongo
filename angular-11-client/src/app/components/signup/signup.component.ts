import { Component, OnInit } from '@angular/core';
import { AppService } from '../../app.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { FFService } from 'src/app/services/ff.service';
//import * as $ from "jquery";

declare var jquery: any;
declare var $: any;



@Component({
    templateUrl: './signup.component.html',
    styleUrls: ['./signup.component.css'],
})
export class SinupComponent {

    user = {name: '', username: '', email: '',password: '', password2: ''};

    activeStep = 1;
    constructor(private app: AppService, private http: HttpClient, private ff: FFService) {
        
        //ff.SetFlags('Sinup_Version',"v2");
        
    }

    signup() {
        this.next(4);
    }

    next(step: number){
        this.activeStep = step;
    }

    stepEnabled(value: number){
        if (this.activeStep === value) {
            return true;
        } else {
            return false;
        }
    }
    stepActived(value: number){
        if (this.activeStep === value) {
            return "is-active";
        } else {
            return "";
        }
    }



    authenticated() { return this.app.authenticated; }

}