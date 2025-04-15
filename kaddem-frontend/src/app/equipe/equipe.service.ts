
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Equipe } from '../models/Equipe.model';

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json'
  })
};

@Injectable({
  providedIn: 'root'
})
export class EquipeService {
  private apiUrl = 'http://localhost:8089/kaddem/equipe';

  constructor(private http: HttpClient) { }

  getEquipes(): Observable<Equipe[]> {
    return this.http.get<Equipe[]>(`${this.apiUrl}/retrieve-all-equipes`, httpOptions);
  }

  deleteEquipe(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/remove-equipe/${id}`, httpOptions);
  }


  addEquipe(equipe: Equipe): Observable<Equipe> {
    return this.http.post<Equipe>(`${this.apiUrl}/add-equipe`, equipe);
  }

  updateEquipe(equipe: Equipe): Observable<Equipe> {
    return this.http.put<Equipe>(`${this.apiUrl}/update-equipe`, equipe);
  }



  getEquipeById(id: number): Observable<Equipe> {
    return this.http.get<Equipe>(`${this.apiUrl}/retrieve-equipe/${id}`);
  }
}
