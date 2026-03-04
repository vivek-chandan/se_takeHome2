package flight.reservation;

import flight.reservation.flight.Flight;

import java.util.List;

public class Airport {

    private static final String[] DEFAULT_ALLOWED_AIRCRAFTS = {"A380", "A350", "Embraer 190", "Antonov AN2", "H1", "H2", "HypaHype"};

    private final String name;
    private final String code;
    private final String location;
    private List<Flight> flights;
    private String[] allowedAircrafts;

    public Airport(String name, String code, String location) {
        this.name = name;
        this.code = code;
        this.location = location;
        this.allowedAircrafts = DEFAULT_ALLOWED_AIRCRAFTS.clone();
    }

    public Airport(String name, String code, String location, String[] allowedAircrafts) {
        this.name = name;
        this.code = code;
        this.location = location;
        this.allowedAircrafts = allowedAircrafts;
    }

    /**
     * Builder for Airport, supporting optional fields beyond the required name, code, and location.
     */
    public static class Builder {
        private final String name;
        private final String code;
        private final String location;
        private String[] allowedAircrafts = DEFAULT_ALLOWED_AIRCRAFTS.clone();

        public Builder(String name, String code, String location) {
            this.name = name;
            this.code = code;
            this.location = location;
        }

        public Builder allowedAircrafts(String[] allowedAircrafts) {
            this.allowedAircrafts = allowedAircrafts.clone();
            return this;
        }

        public Airport build() {
            return new Airport(name, code, location, allowedAircrafts);
        }
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getLocation() {
        return location;
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }

    public String[] getAllowedAircrafts() {
        return allowedAircrafts;
    }
}
