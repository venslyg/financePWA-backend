import dayjs from 'dayjs/esm';

import { IDonationTracker, NewDonationTracker } from './donation-tracker.model';

export const sampleWithRequiredData: IDonationTracker = {
  id: 17177,
};

export const sampleWithPartialData: IDonationTracker = {
  id: 8650,
  branchCode: 'skateboard happy',
  branchId: 'beard far-off',
  date: dayjs('2026-06-12'),
  donorNameOrOrg: 'tromp',
  receivedViaMode: 'CASH',
  notes: 'before impeccable',
  createdDate: dayjs('2026-06-11T20:08'),
};

export const sampleWithFullData: IDonationTracker = {
  id: 30043,
  branchCode: 'pfft pessimistic watery',
  branchId: 'tapioca modulo',
  donationIdCode: 'er sternly harvest',
  date: dayjs('2026-06-11'),
  donorNameOrOrg: 'drat',
  contactDetails: 'bus for',
  amount: 21252.02,
  purpose: 'now duh',
  receivedViaMode: 'BANK',
  notes: 'disconnection',
  createdBy: 'huddle honestly',
  createdDate: dayjs('2026-06-11T11:26'),
  lastModifiedBy: 'warlike',
  lastModifiedDate: dayjs('2026-06-11T13:57'),
};

export const sampleWithNewData: NewDonationTracker = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
