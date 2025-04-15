import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { EquipeService } from '../equipe.service';
import { Router, ActivatedRoute } from '@angular/router';
import { Equipe } from 'src/app/models/Equipe.model';

@Component({
  selector: 'app-equipe-form',
  templateUrl: './equipe-form.component.html',
  styleUrls: ['./equipe-form.component.css']
})
export class EquipeFormComponent implements OnInit {
  equipeForm: FormGroup;
  isEditMode = false;
  equipeId?: number;

  constructor(
    private fb: FormBuilder,
    private equipeService: EquipeService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.equipeForm = this.fb.group({
      nomEquipe: ['', Validators.required],
      niveau: ['JUNIOR', Validators.required]
    });
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      if (params['id']) {
        this.isEditMode = true;
        this.equipeId = +params['id'];
        this.loadEquipe(this.equipeId);
      }
    });
  }

  loadEquipe(id: number): void {
    this.equipeService.getEquipeById(id).subscribe(equipe => {
      this.equipeForm.patchValue({
        nomEquipe: equipe.nomEquipe,
        niveau: equipe.niveau
      });
    });
  }

  onSubmit(): void {
    if (this.equipeForm.valid) {
      const equipe: Equipe = this.equipeForm.value;
      
      if (this.isEditMode && this.equipeId) {
        equipe.idEquipe = this.equipeId;
        this.equipeService.updateEquipe(equipe).subscribe({
          next: () => this.router.navigate(['/equipes']),
          error: (error) => console.error('Error updating equipe:', error)
        });
      } else {
        this.equipeService.addEquipe(equipe).subscribe({
          next: () => this.router.navigate(['/equipes']),
          error: (error) => console.error('Error adding equipe:', error)
        });
      }
    }
  }
}