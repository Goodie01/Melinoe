import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router, RouterOutlet} from '@angular/router';
import {HttpClient, HttpClientModule} from "@angular/common/http";
import {FormsModule} from "@angular/forms";
import {LogMessage} from "./LogMessage";
import {NgForOf, NgIf} from "@angular/common";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, HttpClientModule, FormsModule, NgIf, NgForOf],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  logMessage?: LogMessage[] = undefined;
  displayedMessage?: LogMessage[] = undefined;
  paramaters?: number[] = undefined;

  constructor(private http: HttpClient,
              private route: ActivatedRoute,
              private router: Router,
              private activatedRoute: ActivatedRoute,) {
  }

  ngOnInit(): void {
    this.loadAllLogMessages();

    this.route.queryParams
      .subscribe(params => {
        let q: string = params['q'];
        if (q != undefined) {
          this.paramaters = q.split(".").map(value => Number(value))
        } else {
          this.paramaters = []
        }

        this.loadDisplayedMessages()
      })
  };

  loadDisplayedMessages() {
    if (this.logMessage == undefined) {
      return;
    }

    if (this.paramaters == undefined || this.paramaters.length == 0) {
      this.displayedMessage = this.logMessage;
    } else {
      let logMessages: LogMessage[] = this.logMessage;

      for (let i = 0; i < this.paramaters.length; i++) {
        let index = this.paramaters[i];
        let maybeMessages = logMessages[index].subSessionMessages;

        if (maybeMessages != undefined) {
          logMessages = maybeMessages
        }
      }

      this.displayedMessage = logMessages;
    }
  }

  loadAllLogMessages() {
    console.log("Starting")
    this.http.get<LogMessage[]>("assets/log.json").subscribe(value => {
      this.logMessage = value;
      this.loadDisplayedMessages()
    })
  }

  browseMessages(i: number) {
    let newQueryParam: string;

    if(this.paramaters != undefined) {
      this.paramaters.push(i)
      newQueryParam = this.paramaters.join(".");
    } else {
      newQueryParam = String(i)
    }

    this.router.navigate(
      [],
      {
        relativeTo: this.activatedRoute,
        queryParams: {q: newQueryParam},
        queryParamsHandling: 'merge'
      }
    );

  }
}
