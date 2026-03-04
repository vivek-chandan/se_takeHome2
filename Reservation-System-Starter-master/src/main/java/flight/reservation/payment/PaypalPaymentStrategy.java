package flight.reservation.payment;

/**
 * Strategy Pattern: concrete payment strategy that processes a PayPal payment.
 */
public class PaypalPaymentStrategy implements PaymentStrategy {

    private final String email;
    private final String password;

    public PaypalPaymentStrategy(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public boolean pay(double amount) {
        if (email == null || password == null || !email.equals(Paypal.DATA_BASE.get(password))) {
            throw new IllegalStateException("Payment information is not set or not valid.");
        }
        System.out.println("Paying " + amount + " using PayPal.");
        return true;
    }
}
