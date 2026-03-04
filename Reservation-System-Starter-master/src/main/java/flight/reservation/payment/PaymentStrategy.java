package flight.reservation.payment;

/**
 * Strategy interface for payment processing.
 * Implements the Strategy design pattern to decouple payment methods from order processing.
 */
public interface PaymentStrategy {
    boolean pay(double amount);
}
