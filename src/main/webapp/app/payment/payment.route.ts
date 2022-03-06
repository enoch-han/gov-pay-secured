import { Authority } from '../config/authority.constants';
import { Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PaymentConfirmationComponent } from './paymentConfirmation/payment-confirmation';
import { PaymentReviewComponent } from './paymentReview/payment-review.component';

export const paymentConfirmationRoute: Routes = [
  {
    path: 'payment-confirmation',
    component: PaymentConfirmationComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'payment',
    },
    canActivate: [UserRouteAccessService],
  },
];

export const paymentReviewRoute: Routes = [
  {
    path: 'payment-review',
    component: PaymentReviewComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'payment',
    },
    canActivate: [UserRouteAccessService],
  },
];
