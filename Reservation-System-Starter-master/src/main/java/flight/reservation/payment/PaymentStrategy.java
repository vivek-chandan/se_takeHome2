package flight.reservation.payment;

/**
 * Strategy Pattern: defines a common interface for all payment strategies.
 * Concrete strategies (CreditCardPaymentStrategy, PaypalPaymentStrategy) implement this interface.
 */
public interface PaymentStrategy {
    boolean pay(double amount);
}
