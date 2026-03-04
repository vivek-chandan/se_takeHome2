package flight.reservation;

import flight.reservation.flight.ScheduledFlight;

import java.util.List;

/**
 * Observer Pattern: interface for observers that want to be notified when passengers
 * are added to a scheduled flight.
 */
public interface FlightBookingObserver {
    void onPassengersAdded(ScheduledFlight flight, List<Passenger> passengers);
}
