package flight.reservation.plane;

/**
 * Aircraft interface providing a common contract for all aircraft types.
 * Applying the polymorphism principle to remove instanceof type checks.
 */
public interface Aircraft {
    String getModel();
    int getPassengerCapacity();
    int getCrewCapacity();
}
