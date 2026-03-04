package flight.reservation;

import flight.reservation.flight.Flight;
import flight.reservation.flight.Schedule;
import flight.reservation.flight.ScheduledFlight;
import flight.reservation.order.FlightOrder;
import flight.reservation.payment.CreditCard;
import flight.reservation.payment.CreditCardPaymentStrategy;
import flight.reservation.payment.PaypalPaymentStrategy;
import flight.reservation.plane.PassengerPlane;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Design Pattern Tests")
public class DesignPatternTest {

    @Nested
    @DisplayName("Builder Pattern – Airport.Builder")
    class AirportBuilderTest {

        @Test
        @DisplayName("builds an airport with default allowed aircrafts")
        void buildsWithDefaults() {
            Airport airport = new Airport.Builder("Heathrow", "LHR", "London").build();
            assertEquals("Heathrow", airport.getName());
            assertEquals("LHR", airport.getCode());
            assertEquals("London", airport.getLocation());
            assertTrue(airport.getAllowedAircrafts().length > 0);
        }

        @Test
        @DisplayName("builds an airport with custom allowed aircrafts")
        void buildsWithCustomAircrafts() {
            String[] allowed = {"A380", "A350"};
            Airport airport = new Airport.Builder("Heathrow", "LHR", "London")
                    .allowedAircrafts(allowed)
                    .build();
            assertArrayEquals(allowed, airport.getAllowedAircrafts());
        }
    }

    @Nested
    @DisplayName("Factory Pattern – PassengerPlane.create()")
    class PassengerPlaneFactoryTest {

        @Test
        @DisplayName("creates an A380 via factory method")
        void createsA380() {
            PassengerPlane plane = PassengerPlane.create("A380");
            assertEquals("A380", plane.model);
            assertEquals(500, plane.passengerCapacity);
            assertEquals(42, plane.crewCapacity);
        }

        @Test
        @DisplayName("factory method throws for unknown model")
        void throwsForUnknownModel() {
            assertThrows(IllegalArgumentException.class, () -> PassengerPlane.create("Unknown"));
        }
    }

    @Nested
    @DisplayName("Strategy Pattern – PaymentStrategy")
    class PaymentStrategyTest {

        private Schedule schedule;
        private Customer customer;
        private FlightOrder order;
        private ScheduledFlight scheduledFlight;

        @BeforeEach
        void setup() {
            schedule = new Schedule();
            Airport start = new Airport("Berlin Airport", "BER", "Berlin, Berlin");
            Airport dest = new Airport("Frankfurt Airport", "FRA", "Frankfurt, Hesse");
            Flight flight = new Flight(1, start, dest, PassengerPlane.create("A380"));
            Date departure = TestUtil.addDays(Date.from(Instant.now()), 3);
            schedule.scheduleFlight(flight, departure);
            customer = new Customer("Max Mustermann", "amanda@ya.com");
            scheduledFlight = schedule.searchScheduledFlight(1);
            order = customer.createOrder(Arrays.asList("Max"), Arrays.asList(scheduledFlight), 100);
        }

        @Test
        @DisplayName("PayPal strategy pays successfully")
        void paypalStrategySucceeds() {
            boolean result = order.processOrder(new PaypalPaymentStrategy("amanda@ya.com", "amanda1985"));
            assertTrue(result);
            assertTrue(order.isClosed());
        }

        @Test
        @DisplayName("PayPal strategy throws for invalid credentials")
        void paypalStrategyThrowsForInvalidCredentials() {
            assertThrows(IllegalStateException.class,
                    () -> order.processOrder(new PaypalPaymentStrategy("wrong@email.com", "wrongpassword")));
            assertFalse(order.isClosed());
        }

        @Test
        @DisplayName("CreditCard strategy pays successfully")
        void creditCardStrategySucceeds() {
            CreditCard card = Mockito.mock(CreditCard.class, Mockito.CALLS_REAL_METHODS);
            Mockito.when(card.isValid()).thenReturn(true);
            card.setAmount(500.0);
            boolean result = order.processOrder(new CreditCardPaymentStrategy(card));
            assertTrue(result);
            assertTrue(order.isClosed());
        }

        @Test
        @DisplayName("CreditCard strategy throws when card limit is reached")
        void creditCardStrategyThrowsWhenLimitReached() {
            CreditCard card = Mockito.mock(CreditCard.class, Mockito.CALLS_REAL_METHODS);
            Mockito.when(card.isValid()).thenReturn(true);
            Mockito.when(card.getAmount()).thenReturn(10.0);
            assertThrows(IllegalStateException.class,
                    () -> order.processOrder(new CreditCardPaymentStrategy(card)));
            assertFalse(order.isClosed());
        }
    }

    @Nested
    @DisplayName("Observer Pattern – FlightBookingObserver")
    class FlightBookingObserverTest {

        @Test
        @DisplayName("observer is notified when passengers are added")
        void observerNotifiedOnPassengersAdded() {
            Airport start = new Airport("Berlin Airport", "BER", "Berlin, Berlin");
            Airport dest = new Airport("Frankfurt Airport", "FRA", "Frankfurt, Hesse");
            ScheduledFlight scheduledFlight = new ScheduledFlight(1, start, dest,
                    PassengerPlane.create("A380"), Date.from(Instant.now()));

            AtomicReference<List<Passenger>> notifiedPassengers = new AtomicReference<>();
            FlightBookingObserver observer = (flight, passengers) -> notifiedPassengers.set(passengers);
            scheduledFlight.addObserver(observer);

            List<Passenger> toAdd = Arrays.asList(new Passenger("Alice"), new Passenger("Bob"));
            scheduledFlight.addPassengers(toAdd);

            assertNotNull(notifiedPassengers.get());
            assertEquals(2, notifiedPassengers.get().size());
        }

        @Test
        @DisplayName("removed observer is no longer notified")
        void removedObserverNotNotified() {
            Airport start = new Airport("Berlin Airport", "BER", "Berlin, Berlin");
            Airport dest = new Airport("Frankfurt Airport", "FRA", "Frankfurt, Hesse");
            ScheduledFlight scheduledFlight = new ScheduledFlight(1, start, dest,
                    PassengerPlane.create("A380"), Date.from(Instant.now()));

            int[] callCount = {0};
            FlightBookingObserver observer = (flight, passengers) -> callCount[0]++;
            scheduledFlight.addObserver(observer);
            scheduledFlight.removeObserver(observer);

            scheduledFlight.addPassengers(Arrays.asList(new Passenger("Alice")));
            assertEquals(0, callCount[0]);
        }
    }
}
