import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { EquipeRoutingModule } from './equipe-routing.module';
import { EquipeComponent } from './equipe.component';
import { EquipeListComponent } from './equipe-list/equipe-list.component';
import { EquipeFormComponent } from './equipe-form/equipe-form.component';


@NgModule({
  declarations: [
    EquipeComponent,
    EquipeListComponent,
    EquipeFormComponent
  ],
  imports: [
    CommonModule,
    EquipeRoutingModule
  ]
})
export class EquipeModule { }
