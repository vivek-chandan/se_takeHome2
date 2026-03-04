package flight.reservation.payment;

/**
 * Concrete Strategy for PayPal payment.
 * Implements the Strategy design pattern for PayPal-based payment.
 */
public class PayPalPayment implements PaymentStrategy {

    private final String email;
    private final String password;

    public PayPalPayment(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public boolean pay(double amount) {
        if (email == null || password == null
                || !Paypal.DATA_BASE.containsKey(password)
                || !email.equals(Paypal.DATA_BASE.get(password))) {
            throw new IllegalStateException("Payment information is not set or not valid.");
        }
        System.out.println("Paying " + amount + " using PayPal.");
        return true;
    }
}
