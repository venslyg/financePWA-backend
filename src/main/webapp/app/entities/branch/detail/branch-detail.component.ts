import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IBranch } from '../branch.model';

@Component({
  selector: 'jhi-branch-detail',
  templateUrl: './branch-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class BranchDetailComponent {
  branch = input<IBranch | null>(null);

  previousState(): void {
    window.history.back();
  }
}
