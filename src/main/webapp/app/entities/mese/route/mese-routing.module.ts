import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MeseComponent } from '../list/mese.component';
import { MeseDetailComponent } from '../detail/mese-detail.component';
import { MeseUpdateComponent } from '../update/mese-update.component';
import { MeseRoutingResolveService } from './mese-routing-resolve.service';

const meseRoute: Routes = [
  {
    path: '',
    component: MeseComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MeseDetailComponent,
    resolve: {
      mese: MeseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MeseUpdateComponent,
    resolve: {
      mese: MeseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MeseUpdateComponent,
    resolve: {
      mese: MeseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(meseRoute)],
  exports: [RouterModule],
})
export class MeseRoutingModule {}
