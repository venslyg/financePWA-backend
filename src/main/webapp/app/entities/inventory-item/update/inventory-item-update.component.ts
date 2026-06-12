import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IInventoryItem } from '../inventory-item.model';
import { InventoryItemService } from '../service/inventory-item.service';
import { InventoryItemFormGroup, InventoryItemFormService } from './inventory-item-form.service';

@Component({
  selector: 'jhi-inventory-item-update',
  templateUrl: './inventory-item-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class InventoryItemUpdateComponent implements OnInit {
  isSaving = false;
  inventoryItem: IInventoryItem | null = null;

  protected inventoryItemService = inject(InventoryItemService);
  protected inventoryItemFormService = inject(InventoryItemFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: InventoryItemFormGroup = this.inventoryItemFormService.createInventoryItemFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ inventoryItem }) => {
      this.inventoryItem = inventoryItem;
      if (inventoryItem) {
        this.updateForm(inventoryItem);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const inventoryItem = this.inventoryItemFormService.getInventoryItem(this.editForm);
    if (inventoryItem.id !== null) {
      this.subscribeToSaveResponse(this.inventoryItemService.update(inventoryItem));
    } else {
      this.subscribeToSaveResponse(this.inventoryItemService.create(inventoryItem));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInventoryItem>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(inventoryItem: IInventoryItem): void {
    this.inventoryItem = inventoryItem;
    this.inventoryItemFormService.resetForm(this.editForm, inventoryItem);
  }
}
