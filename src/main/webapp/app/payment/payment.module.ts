import { PaymentResolver } from './payment-resolver.service';
import { PaymentService } from './payment.service';
import { PaymentConfirmationComponent } from './paymentConfirmation/payment-confirmation';
import { PaymentReviewComponent } from './paymentReview/payment-review.component';
import { PaymentFormComponent } from './paymentForm/payment-form.component';
import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { paymentConfirmationRoute, paymentReviewRoute } from './payment.route';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

const ROUTES = [...paymentReviewRoute, ...paymentConfirmationRoute];

@NgModule({
  imports: [RouterModule.forRoot(ROUTES), FormsModule, CommonModule, ReactiveFormsModule, HttpClientModule],
  declarations: [PaymentFormComponent, PaymentReviewComponent, PaymentConfirmationComponent],
  entryComponents: [PaymentFormComponent, PaymentReviewComponent, PaymentConfirmationComponent],
  providers: [PaymentService, PaymentResolver],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  exports: [PaymentFormComponent, PaymentReviewComponent, PaymentConfirmationComponent],
})
export class PaymentModule {}
