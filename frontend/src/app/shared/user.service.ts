import {Injectable} from "@angular/core";
import {RequestOptions, Headers, Http, Response} from "@angular/http";
import {Observable} from "rxjs";

@Injectable()
export class UserService {

  constructor(private http: Http) {
  }

  getUsers(): Observable<any> {
    return this.http.get('/api/users').map((response: Response) => {
      console.log('Receiving response: ' + response.json());
      return response.json();
    }).catch(this.handleError);
  }

  handleError(error: Response) {
    return Observable.throw(error.statusText);
  }

  saveUser(user): Observable<any> {
    console.log("Received user with userName:" + user.userName);

    let headers = new Headers({'Content-Type': 'application/json'});
    let options = new RequestOptions({headers: headers});
    return this.http.post('/api/projects/', JSON.stringify(user), options)
      .map((response: Response) => {
        console.log(response.json());
        return response.json();
      }).catch(this.handleError);
  }
}
