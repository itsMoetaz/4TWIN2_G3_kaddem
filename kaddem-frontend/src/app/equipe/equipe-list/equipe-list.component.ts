import { Component, OnInit } from '@angular/core';
import { EquipeService } from '../equipe.service';
import { catchError } from 'rxjs/operators';
import { of } from 'rxjs';
import { Equipe } from 'src/app/models/Equipe.model';

@Component({
  selector: 'app-equipe-list',
  templateUrl: './equipe-list.component.html',
  styleUrls: ['./equipe-list.component.css']
})
export class EquipeListComponent implements OnInit {
  equipes: Equipe[] = [];
  error: string = '';

  constructor(private equipeService: EquipeService) {}

  ngOnInit(): void {
    this.getEquipes();
  }

  getEquipes(): void {
    this.equipeService.getEquipes()
      .pipe(
        catchError(error => {
          this.error = 'Failed to load equipes. Please try again later.';
          console.error('Error:', error);
          return of([]);
        })
      )
      .subscribe(data => {
        this.equipes = data;
      });
  }

  deleteEquipe(id: number): void {
    this.equipeService.deleteEquipe(id)
      .pipe(
        catchError(error => {
          this.error = 'Failed to delete equipe. Please try again later.';
          console.error('Error:', error);
          return of(null);
        })
      )
      .subscribe(() => {
        this.getEquipes();
      });
  }
}