import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMese } from '../mese.model';
import { MeseService } from '../service/mese.service';
import { MeseDeleteDialogComponent } from '../delete/mese-delete-dialog.component';

@Component({
  selector: 'jhi-mese',
  templateUrl: './mese.component.html',
})
export class MeseComponent implements OnInit {
  mese?: IMese[];
  isLoading = false;

  constructor(protected meseService: MeseService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.meseService.query().subscribe(
      (res: HttpResponse<IMese[]>) => {
        this.isLoading = false;
        this.mese = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IMese): number {
    return item.id!;
  }

  delete(mese: IMese): void {
    const modalRef = this.modalService.open(MeseDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.mese = mese;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
