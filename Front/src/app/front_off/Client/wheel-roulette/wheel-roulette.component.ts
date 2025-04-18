import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { loadStripe, Stripe } from '@stripe/stripe-js';
import { KeycloakService } from '../../../services/keycloak/keycloak.service';
import { ResourceService } from '../../../services/ResourceService/resource.service';
import { environment } from '../../../services/environments/environment';

@Component({
  selector: 'app-wheel-roulette',
  templateUrl: './wheel-roulette.component.html',
  styleUrls: ['./wheel-roulette.component.css']
})
export class WheelRouletteComponent implements OnInit {
  prizes = ['House', 'Apartment', '$1000', '$500', '$200', 'Nothing'];
  spinning = false;
  result: string | null = null;
  rotation = 0;
  stripe: Stripe | null = null;
  hasPaid = false;
  hasSpun = false;
  paymentError: string | null = null;
  hasAttemptedPayment = false;

  constructor(
    private keycloakService: KeycloakService,
    private resourceService: ResourceService,
    private http: HttpClient
  ) {}

  async ngOnInit() {
    console.log('WheelRouletteComponent ngOnInit, current URL:', window.location.href);

    // Initialize Stripe
    this.stripe = await loadStripe(environment.stripePublishableKey);

    // Restore payment and spin state from localStorage
    const userId = this.keycloakService.tokenParsed?.sub;
    if (userId) {
      const storedState = localStorage.getItem(`wheel_state_${userId}`);
      if (storedState) {
        const { hasPaid, hasSpun, hasAttemptedPayment } = JSON.parse(storedState);
        this.hasPaid = hasPaid;
        this.hasSpun = hasSpun;
        this.hasAttemptedPayment = hasAttemptedPayment || false;
        console.log('Restored state from localStorage:', { hasPaid, hasSpun, hasAttemptedPayment });
      }

      // Check for stored session_id to retry verification
      const storedSessionId = localStorage.getItem(`wheel_session_id_${userId}`);
      if (storedSessionId && !this.hasPaid && !this.hasSpun) {
        console.log('Found stored session_id, retrying verification:', storedSessionId);
        this.verifyPayment(storedSessionId);
      }
    }

    // Check if redirected back with session_id
    const urlParams = new URLSearchParams(window.location.search);
    const sessionId = urlParams.get('session_id');
    if (sessionId) {
      console.log('Found URL session_id, verifying payment:', sessionId);
      if (userId) {
        localStorage.setItem(`wheel_session_id_${userId}`, sessionId);
      }
      this.hasAttemptedPayment = true;
      this.verifyPayment(sessionId);
    } else if (this.hasPaid && !this.hasSpun) {
      console.log('No session_id, but hasPaid=true from localStorage, wheel should be active');
    }
  }

  async initiatePayment() {
    if (!this.keycloakService.tokenParsed?.sub) {

      return;
    }

    try {
      console.log('Initiating payment for user:', this.keycloakService.tokenParsed.sub);
      const response = await this.resourceService
        .createCheckoutSession(this.keycloakService.tokenParsed.sub)
        .toPromise();
      if (response && response.sessionId && this.stripe) {
        console.log('Received sessionId, redirecting to Stripe:', response.sessionId);
        this.stripe.redirectToCheckout({ sessionId: response.sessionId });
      } else {
        this.paymentError = 'Failed to initiate payment: Invalid response from server.';
        console.log('initiatePayment: Invalid response:', response);
      }
    } catch (error: unknown) {
      const errorMessage = error instanceof Error ? error.message : 'Unknown error occurred';
      this.paymentError = 'Error initiating payment: ' + errorMessage;
      this.hasAttemptedPayment = true;
      this.hasSpun = false;
      console.log('initiatePayment error:', errorMessage);
      this.updateLocalStorage();
    }
  }

  verifyPayment(sessionId: string, retryCount = 0) {
    console.log('Verifying payment for sessionId:', sessionId, 'Retry count:', retryCount);
    this.resourceService.verifyPayment(sessionId).subscribe({
      next: (response) => {
        console.log('verifyPayment response:', response);
        this.hasPaid = response.isPaid;
        this.hasSpun = false;
        if (this.hasPaid) {
          this.paymentError = 'Payment not completed.';
          console.log('verifyPayment: Payment not completed');
          if (retryCount < 1) {
            console.log('Retrying payment verification in 2 seconds...');
            setTimeout(() => this.verifyPayment(sessionId, retryCount + 1), 2000);
          }
        } else {
          this.paymentError = null;
          console.log('Payment verified, stored state:', { hasPaid: this.hasPaid, hasSpun: this.hasSpun });
        }
        this.updateLocalStorage();
        window.history.replaceState({}, document.title, window.location.pathname);
      },
      error: (err: unknown) => {
        const errorMessage = err instanceof Error ? err.message : 'Unknown error occurred';
        this.paymentError = 'Error verifying payment: ' + errorMessage;
        this.hasPaid = false;
        this.hasSpun = false;
        console.log('verifyPayment error:', errorMessage);
        if (retryCount < 1) {
          console.log('Retrying payment verification in 2 seconds due to error...');
          setTimeout(() => this.verifyPayment(sessionId, retryCount + 1), 2000);
        }
        this.updateLocalStorage();
      }
    });
  }

  spin() {
    if (!this.hasAttemptedPayment) {
      this.paymentError = 'Please attempt payment to spin the wheel.';
      console.log('spin: Payment attempt required');
      return;
    }
    if (this.hasSpun) {
      this.paymentError = 'You have already spun the wheel. Please pay again to spin.';
      console.log('spin: Already spun');
      return;
    }
    if (this.spinning) return;

    console.log('Starting spin');
    this.spinning = true;
    this.result = null;

    const randomSpins = 5 + Math.random() * 5;
    const segmentAngle = 360 / this.prizes.length;
    const randomPrizeIndex = Math.floor(Math.random() * this.prizes.length);
    this.rotation = randomSpins * 360 + randomPrizeIndex * segmentAngle + segmentAngle / 2;

    setTimeout(() => {
      this.spinning = false;
      this.result = this.prizes[randomPrizeIndex];
      this.hasSpun = true;
      this.updateLocalStorage();
      console.log('Spin completed, updated state:', { hasPaid: this.hasPaid, hasSpun: this.hasSpun, hasAttemptedPayment: this.hasAttemptedPayment, result: this.result });

      // Send email if a prize was won (not "Nothing")
      if (this.result && this.result !== 'Nothing') {
        this.sendWinEmail(this.result);
      }
    }, 3000);
  }

  private sendWinEmail(prize: string) {
    const userEmail = this.keycloakService.tokenParsed?.email;
    if (!userEmail) {
      console.error('No user email found for sending win notification');
      return;
    }

    const emailRequest = {
      to: userEmail,
      subject: 'Congratulations! You Won a Prize on Wheel Roulette!',
      body: `Dear User,\n\nCongratulations! You have won ${prize} by spinning the Wheel Roulette.\n\nThank you for participating!\nBest regards,\nThe Wheel Roulette Team`
    };

    this.http.post('http://localhost:8089/api/email/send', emailRequest).subscribe({
      next: () => console.log('Win email sent successfully to', userEmail),
      error: (err) => console.error('Failed to send win email:', err)
    });
  }

  reset() {
    this.rotation = 0;
    this.result = null;
    this.spinning = false;
    this.paymentError = null;
    console.log('Wheel reset');
    this.updateLocalStorage();
  }

  private updateLocalStorage() {
    const userId = this.keycloakService.tokenParsed?.sub;
    if (userId) {
      localStorage.setItem(
        `wheel_state_${userId}`,
        JSON.stringify({ hasPaid: this.hasPaid, hasSpun: this.hasSpun, hasAttemptedPayment: this.hasAttemptedPayment })
      );
    }
  }

  getSegmentColor(index: number): string {
    const colors = [
      '#ffcc5c',
      '#d4a5a5',
      '#ff6f61',
      '#6b5b95',
      '#feb236',
      '#88d8b0'
    ];
    return colors[index % colors.length];
  }
}
