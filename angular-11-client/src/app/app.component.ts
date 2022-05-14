import { Component } from '@angular/core';
// Feature Flags
import { initialize, Event } from '@harnessio/ff-javascript-client-sdk'


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Angular 11 Crud';
  appRepositoryEnabled = false
  isTracking = true
  currentLat = 0;
  currentLong = 0;

  

  async ngOnInit(): Promise<void> {
    //dotenv.config();
    
    console.log("FF Starting")
    let sdkKey = "e2148669-99aa-42e8-9a29-bd339c7ca5a8"; //process.env.FF_SDK_KEY || "e2148669-99aa-42e8-9a29-bd339c7ca5a8"
    const cf = initialize(sdkKey, {
      identifier: "GlobalFront",      // Target identifier
      name: "Global Front",                  // Optional target name
      attributes: {                            // Optional target attributes
        email: 'global@harness.io',
        platform: navigator.platform,
        language: navigator.language,
        timezone: (Intl.DateTimeFormat().resolvedOptions().timeZone)
      }
    });

    cf.on(Event.READY, flags => {
      console.log(JSON.stringify(flags, null, 2))
      console.log(flags["Repositories"]);
      this.appRepositoryEnabled = Boolean(flags["Repositories"]);
      if (this.appRepositoryEnabled) {
        console.log("Success")
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
}
