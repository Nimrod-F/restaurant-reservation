import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MeseComponent } from './list/mese.component';
import { MeseDetailComponent } from './detail/mese-detail.component';
import { MeseUpdateComponent } from './update/mese-update.component';
import { MeseDeleteDialogComponent } from './delete/mese-delete-dialog.component';
import { MeseRoutingModule } from './route/mese-routing.module';

@NgModule({
  imports: [SharedModule, MeseRoutingModule],
  declarations: [MeseComponent, MeseDetailComponent, MeseUpdateComponent, MeseDeleteDialogComponent],
  entryComponents: [MeseDeleteDialogComponent],
})
export class MeseModule {}
