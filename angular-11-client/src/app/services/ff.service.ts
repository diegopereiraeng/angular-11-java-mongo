import { Injectable, SimpleChanges } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { FF } from '../models/ff.model';
import { FFClient } from '../models/ffClient.model';
import { AppService } from 'src/app/app.service';
// Feature Flags
import { initialize, Event, Result } from '@harnessio/ff-javascript-client-sdk'





//const baseUrl = 'http://localhost:8080'; //process.env.REPOSITORY_BACKEND||'http://localhost:8080/api/repositories';
const baseUrl = 'http://harness-demo.site/spring-boot-server/';



@Injectable({
  providedIn: 'root'
})
export class FFService {

  flags: Array<FF> = [];
  ffSdk = 'e2148669-99aa-42e8-9a29-bd339c7ca5a8';
  cfClient = {} as Result;
  loggedIn = false;
  clients: FFClient[] = [];

  constructor(private http: HttpClient,private app: AppService) {
    this.authenticated()

    // Setup initials flags
    this.flags.push(new FF('repositoryEnabled',false));
    this.flags.push(new FF('harnessOnboardingEnabled',false));

    this.renitializeSDK()

  }
  registerEvent(event: Event,callback: any){ 
    console.log("Registering event subcription")
    this.clients.push( new FFClient(event,callback) );

    this.cfClient.on(event,callback)
  }

  authenticated() {
    if (this.loggedIn != this.app.authenticated) {
      this.loggedIn = this.app.authenticated;
      console.log("FF Service: Authenticated")
      this.renitializeSDK();
    }
    return this.loggedIn
  }

  renitializeSDK() {
    console.log("Restarting FF Service SDK");
    console.log("Restarting username = "+this.app.currentUser.username)
    console.log("Restarting "+this.app.currentUser.username+ " auth="+this.app.authenticated)
    console.log("Restarting FF Service: Registering new target, restarting SDK");
    this.cfClient.off;
    this.cfClient.close;

    this.authenticated()

    // Setup initials flags
    this.flags.push(new FF('repositoryEnabled',false));
    //this.flags.push(new FF('harnessOnboardingEnabled',false));

    // Initialize Client
    this.cfClient = initialize(this.ffSdk, {
      identifier: this.app.currentUser.username!,      // Target identifier
      name: this.app.currentUser.name!,                  // Optional target name
      attributes: {                            // Optional target attributes
        email: this.app.currentUser.email!,
        userType: this.app.currentUser.type!,
        platform: navigator.platform,
        language: navigator.language,
        timezone: (Intl.DateTimeFormat().resolvedOptions().timeZone)
      }
    });
    console.log(this.clients.length)
    for (let clientID = 0; clientID < this.clients.length; clientID++) {
      const client:FFClient = this.clients[clientID];
      this.cfClient.on(client.event!,client.callback!)
      console.log(clientID)
    }

    this.cfClient.on(Event.READY, flags => {
      console.log(JSON.stringify(flags, null, 2))
      console.log(flags["Repositories"]);
      this.SetFlags("repositoryEnabled",Boolean(flags["Repositories"]));
      //this.SetFlags("harnessOnboardingEnabled",Boolean(flags["Harness_Onboarding"]));
      
      
    })

    this.cfClient.on(Event.CHANGED, flagInfo => {
      console.log("Flag:"+flagInfo.flag+" changed")
      console.log(JSON.stringify(flagInfo, null, 2))
      if (flagInfo.flag === "Repositories") {
        this.SetFlags("repositoryEnabled",Boolean(flagInfo.value));
      }
      else if (flagInfo.flag === "Repository_Filter") {
        this.SetFlags("Repository_Filter",Boolean(flagInfo.value));
      }
      else{
        console.log("Flag:"+flagInfo.flag+" changed to "+Boolean(flagInfo.value))
        this.SetFlags(flagInfo.flag,Boolean(flagInfo.value));
      }
      
    })
  }


  SetFlags(flag: string, value: boolean ): void {
    
    this.flags.filter(flagObj => flagObj.flag == flag)
    let ffToUpdate = new FF(flag,value);
    let updateItem = this.flags.find(this.findIndexToUpdate, ffToUpdate.flag);

    let index = this.flags.indexOf(updateItem!);

    if (this.flags[index] !== undefined) {
      this.flags[index].value = value ;
    }else{
      this.flags.push(new FF(flag,value));
    }
    

  }

  findIndexToUpdate(flagObj: any) { 
        return flagObj.flag === this;
  }

  GetFlags(flag: string ): boolean {
    
    this.flags.filter(flagObj => flagObj.flag == flag)
    let ffToUpdate = new FF(flag,false);
    let updateItem = this.flags.find(this.findIndexToUpdate, ffToUpdate.flag);
    let index = this.flags.indexOf(updateItem!);
    this.flags[index].value = this.cfClient.variation(flag,this.flags[index].value);
    console.log("Get flags "+flag+": " + this.flags[index].value)
    
    return this.flags[index].value
  }

  ngOnChanges(changes: SimpleChanges) {

    console.log("FF Service: Authenticated="+this.loggedIn)
    console.log("FF Service: Authenticated="+this.app.currentUser)
    // only run when property "data" changed
    if (changes['loggedIn']) {
      
      console.log("FF Service");
      console.log("Registering new target, restarting SDK");
      this.renitializeSDK()
      
    }
  }

  async ngOnInit(): Promise<void> {
    //dotenv.config();
    console.log("FF Starting")
    
    this.renitializeSDK();
    
  }

  
}
