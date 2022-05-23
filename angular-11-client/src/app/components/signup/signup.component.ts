import { Component, OnInit } from '@angular/core';
import { AppService } from '../../app.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { FFService } from 'src/app/services/ff.service';
import {FormBuilder, Validators, AbstractControl, ValidationErrors, ValidatorFn, FormGroup } from '@angular/forms';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
//import * as $ from "jquery";

declare var jquery: any;
declare var $: any;

export function createPasswordStrengthValidator(): ValidatorFn {
    return (control:AbstractControl) : ValidationErrors | null => {

        const value = control.value;
        console.log(value)
        if (!value) {
            return null;
        }

        const hasUpperCase = /[A-Z]+/.test(value);

        const hasLowerCase = /[a-z]+/.test(value);

        const hasNumeric = /[0-9]+/.test(value);

        const passwordValid = hasUpperCase && hasLowerCase && hasNumeric;
        
        return !passwordValid ? {passwordStrength:true}: null;
    }
}

@Component({
    templateUrl: './signup.component.html',
    styleUrls: ['./signup.component.css'],
})
export class SinupComponent {

    user = {name: '', username: '', email: '',password: '', password2: ''};

    activeStep = 1;

    signupFormGroup: FormGroup;
    signupFormGroup2: FormGroup;



    

    get email() {
        return this.signupFormGroup.controls['email'];
    }
    
    get password() {
        return this.signupFormGroup.controls['password'];
    }

    
    constructor(private app: AppService, private http: HttpClient, private ff: FFService,private formBuilder: FormBuilder) {
        
        //ff.SetFlags('Sinup_Version',"v2");
        this.signupFormGroup = this.formBuilder.group({
            // *********************************************
            // O valor padrão deste formControl será vazio
            // e os demais vazio
            // *********************************************
            name: ['', Validators.required],
            username: ['', Validators.required],
            email: ['', [
                Validators.required,
                Validators.email
            ]]
        });

        this.signupFormGroup2 = this.formBuilder.group({
            // *********************************************
            // O valor padrão deste formControl será vazio
            // e os demais vazio
            // *********************************************
            password: [null, [Validators.required, Validators.minLength(8)]],
            password2: [null, [Validators.required, Validators.minLength(8), createPasswordStrengthValidator() ]],
        });

        
    }

    

    signup() {
        
        this.next(4);
    }

    next(step: number){
        if (!this.signupFormGroup.valid) {
            console.log("Formulário inválido");
            return;
        }
        if (!this.signupFormGroup2.valid && step >= 4) {
            console.log("Formulário inválido");
            return;
        }
        console.log("Formulário válido", this.signupFormGroup.value);
        
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