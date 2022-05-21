import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Token } from 'src/app/models/token.model';
import { Auth } from 'src/app/models/auth.model';
import { User } from 'src/app/models/user.model';


@Injectable()
export class AppService {

    authenticated = false;

    currentUser = new User("Guest","guest","guest@demo.com","GUEST");

    token: Token = new Token;

    authorization: string = this.token.tokenType + ' ' + this.token.accessToken

    constructor(private http: HttpClient) {
        this.token.tokenType = "Bearer"
        this.token.accessToken = this.getData("SessionID") || '';
        this.recoverSessionAuth();
    }

    setToken(token: Token) {
        this.token = token;
    }

    getToken(){
        this.authorization = this.token.tokenType + ' ' + this.token.accessToken
        return this.authorization;
    }

    saveData(key:string , value: any) {
        sessionStorage.setItem(key, value);
    }

    getData(key: string){
    return sessionStorage.getItem(key);
    }

    removeData(key: string) {
    sessionStorage.removeItem(key);
    }

    async recoverSessionAuth(){
        const headers = new HttpHeaders({
            authorization : this.token.tokenType + ' ' + this.token.accessToken
        });
        
        //const response = await this.http.get<Auth>('http://localhost:8080/auth', {headers: headers}).toPromise();
        const response = await this.http.get<Auth>('http://harness-demo.site/spring-boot-server/auth', {headers: headers}).toPromise();

        if (response['name']) {
            console.log("authenticating user: "+response['name'])
            this.authenticated = true;
            console.log(response['name']);
        } else {
            this.authenticated = false;
        }
    }

    async authenticate(credentials: any, callback: any) {
        const headers = new HttpHeaders(credentials ? {
            authorization : this.token.tokenType + ' ' + this.token.accessToken
        } : {});
        
        //const response = await this.http.get<Auth>('http://localhost:8080/auth', {headers: headers}).toPromise();
        const response = await this.http.get<Auth>('http://harness-demo.site/spring-boot-server/auth', {headers: headers}).toPromise();
        /* const result = await this.http.get<Auth>('http://localhost:8080/auth', {headers: headers}).subscribe(response => {
            if (response['name']) {
                console.log("authenticating user: "+response['name'])
                this.authenticated = true;
                console.log(response['name']);
            } else {
                this.authenticated = false;
            }
            return callback && callback();
        }); */
        if (response['name']) {
            console.log("authenticating user: "+response['name'])
            this.authenticated = true;
            console.log(response['name']);
        } else {
            this.authenticated = false;
        }

        if (this.authenticated) {
            console.log("App Service: Getting User info...")
            //this.http.get<User>('http://localhost:8080/users/me', {headers: headers}).subscribe(response => {
            this.http.get<User>('http://harness-demo.site/spring-boot-server/users/me', {headers: headers}).subscribe(response => {
            this.currentUser.username = response.username;
            this.currentUser.name = response.name;
            this.currentUser.email = response.email;
            this.currentUser.type = response.type;
            this.saveData("username",response.username)
            this.saveData("name",response.name)
            this.saveData("email",response.email)
            this.saveData("type",response.type)

            console.log("App Service: user "+response.username);
            console.log("App Service: user "+this.getData("name"));

            return callback && callback();
        });
        }
        

    }

}