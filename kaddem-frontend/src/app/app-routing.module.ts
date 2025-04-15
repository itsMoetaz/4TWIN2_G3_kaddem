import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { EquipeListComponent } from './equipe/equipe-list/equipe-list.component';
import { EquipeFormComponent } from './equipe/equipe-form/equipe-form.component';

const routes: Routes = [
  { path: '', redirectTo: 'equipes', pathMatch: 'full' },
  { path: 'equipes', component: EquipeListComponent },
  { path: 'ajouter-equipe', component: EquipeFormComponent },
  { path: 'equipes/edit/:id', component: EquipeFormComponent },

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
