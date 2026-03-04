package flight.reservation.payment;

/**
 * Concrete Strategy for credit card payment.
 * Implements the Strategy design pattern for CreditCard-based payment.
 */
public class CreditCardPayment implements PaymentStrategy {

    private final CreditCard card;

    public CreditCardPayment(CreditCard card) {
        this.card = card;
    }

    @Override
    public boolean pay(double amount) {
        if (card == null || !card.isValid()) {
            throw new IllegalStateException("Payment information is not set or not valid.");
        }
        System.out.println("Paying " + amount + " using Credit Card.");
        double remainingAmount = card.getAmount() - amount;
        if (remainingAmount < 0) {
            System.out.printf("Card limit reached - Balance: %f%n", remainingAmount);
            throw new IllegalStateException("Card limit reached");
        }
        card.setAmount(remainingAmount);
        return true;
    }
}
