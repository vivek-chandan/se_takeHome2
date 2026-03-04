package flight.reservation.payment;

/**
 * Strategy Pattern: concrete payment strategy that charges a credit card.
 */
public class CreditCardPaymentStrategy implements PaymentStrategy {

    private final CreditCard creditCard;

    public CreditCardPaymentStrategy(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    @Override
    public boolean pay(double amount) {
        if (creditCard == null || !creditCard.isValid()) {
            throw new IllegalStateException("Payment information is not set or not valid.");
        }
        double remainingAmount = creditCard.getAmount() - amount;
        if (remainingAmount < 0) {
            System.out.printf("Card limit reached - Balance: %f%n", remainingAmount);
            throw new IllegalStateException("Card limit reached");
        }
        System.out.println("Paying " + amount + " using Credit Card.");
        creditCard.setAmount(remainingAmount);
        return true;
    }
}
