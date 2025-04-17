package tn.esprit.springproject.Services;

import org.springframework.stereotype.Service;

@Service
public class StripeService {
   /* @Value("${stripe.api.secret-key}") // ✅ Ensure this is correctly set in application.properties
    private String stripeApiKey;

    public Session createCheckoutSession(double amount) throws StripeException {
        Stripe.apiKey = stripeApiKey; // ✅ Ensure API key is set correctly

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD) // ✅ Correct method
                .setMode(SessionCreateParams.Mode.PAYMENT) // ✅ One-time payment
                .setSuccessUrl("https://yourwebsite.com/success") // ✅ Redirect on success
                .setCancelUrl("https://yourwebsite.com/cancel") // ✅ Redirect on cancel
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L) // ✅ Must be Long, not int
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("usd")
                                                .setUnitAmount((long) (amount * 100)) // ✅ Convert to cents
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("Your Product Name")
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )
                .build();

        return Session.create(params); // ✅ Create Stripe Checkout Session
    }


    */
}
