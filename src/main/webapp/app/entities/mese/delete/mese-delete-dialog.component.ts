import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMese } from '../mese.model';
import { MeseService } from '../service/mese.service';

@Component({
  templateUrl: './mese-delete-dialog.component.html',
})
export class MeseDeleteDialogComponent {
  mese?: IMese;

  constructor(protected meseService: MeseService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.meseService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
