import dayjs from 'dayjs/esm';

import { IDonationTracker, NewDonationTracker } from './donation-tracker.model';

export const sampleWithRequiredData: IDonationTracker = {
  id: 17177,
};

export const sampleWithPartialData: IDonationTracker = {
  id: 29382,
  branchCode: 'geez',
  donationIdCode: 'happy finally reborn',
  donorNameOrOrg: 'knuckle',
  contactDetails: 'aside splurge serene',
  notes: 'colligate',
  createdBy: 'yahoo',
  lastModifiedBy: 'valuable unabashedly nervously',
};

export const sampleWithFullData: IDonationTracker = {
  id: 30043,
  branchCode: 'pfft pessimistic watery',
  donationIdCode: 'tapioca modulo',
  date: dayjs('2026-06-12'),
  donorNameOrOrg: 'request whoa',
  contactDetails: 'abaft drat',
  amount: 15223.73,
  purpose: 'bench bah',
  receivedViaMode: 'BANK',
  notes: 'zowie',
  createdBy: 'blah',
  createdDate: dayjs('2026-06-12T04:49'),
  lastModifiedBy: 'nor solidly',
  lastModifiedDate: dayjs('2026-06-12T04:26'),
};

export const sampleWithNewData: NewDonationTracker = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
