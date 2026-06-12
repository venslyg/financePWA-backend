import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IBinCardLine } from '../bin-card-line.model';

@Component({
  selector: 'jhi-bin-card-line-detail',
  templateUrl: './bin-card-line-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class BinCardLineDetailComponent {
  binCardLine = input<IBinCardLine | null>(null);

  previousState(): void {
    window.history.back();
  }
}
