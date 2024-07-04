package com.example.cassandra;

import java.io.FileWriter;
import java.time.LocalDate;

import com.datastax.oss.driver.api.core.ConsistencyLevel;
import com.datastax.oss.driver.api.core.DefaultConsistencyLevel;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;
import com.datastax.oss.driver.api.core.cql.*;
import com.datastax.oss.driver.api.core.CqlSession;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.QueryTrace;

import java.util.Optional;

public class App {

    public static void saveTrace(ResultSet resultSet) {
        if (resultSet != null) {
            QueryTrace trace = resultSet.getExecutionInfo().getQueryTrace();

            if (!trace.getEvents().isEmpty()) {
                try (FileWriter writer = new FileWriter("traces/" + trace.getTracingId() + ".txt")) {
                    writer.write("Trace ID: " + trace.getTracingId() + "\n");
                    trace.getEvents().forEach(event -> {
                        try {
                            writer.write(event.getSource() + " " + event.getActivity() + " " + event.getSourceElapsedMicros() + "ms\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static List<Flight> getFlightsFromAirport(CqlSession session, String airportCode){
        SimpleStatement query = SimpleStatement.newInstance("SELECT * FROM flight WHERE departure='" + airportCode + "';").setTracing(true);
        return getFlights(session, query);
    }

    public static Airport getAirportFromIATA(CqlSession session, String airportCode) {
        SimpleStatement query = SimpleStatement.newInstance("SELECT * FROM airport WHERE IATA_code='" + airportCode + "';").setTracing(true);
        ResultSet rs = session.execute(query);

        saveTrace(rs);

        Row row = rs.one();
        Airport airport = null;
        if (row != null) {
            String iata_code = row.getString("iata_code");
            String country = row.getString("country");
            String country_code = row.getString("country_code");
            String geo_point = row.getString("geo_point");
            String icao_code = row.getString("icao_code");
            String name = row.getString("name");
            String name_en = row.getString("name_en");
            String name_fr = row.getString("name_fr");
            String operator = row.getString("operator");
            String phone = row.getString("phone");
            int size = row.getInt("size");
            String website= row.getString("website");

            airport = new Airport(iata_code, country, country_code, geo_point, icao_code, name, name_en, name_fr, operator, phone, size, website);
        }

        return airport;
    }

    public static List<Flight> getFlightsFromDepartureAndDestination(CqlSession session, String departureCode, String destinationCode) {
        SimpleStatement query = SimpleStatement.newInstance("SELECT * FROM flight WHERE departure='" + departureCode + "' AND destination='" + destinationCode + "';").setTracing(true);
        return getFlights(session, query);
    }

    private static List<Flight> getFlights(CqlSession session,SimpleStatement query) {
        ResultSet rs = session.execute(query);

        saveTrace(rs);

        List<Flight> flightList = new ArrayList<>();

        for (Row row : rs) {
            String departure = row.getString("departure");
            String day = "" + row.getLocalDate("day");
            String destination = row.getString("destination");
            String hour = "" + row.getLocalTime("hour");
            String id = row.getString("id");
            String duration = row.getString("duration");
            int number_of_seats = row.getInt("number_of_seats");
            String operator = row.getString("operator");
            int price_per_person = row.getInt("price_per_person");

            Flight airport = new Flight(departure, day, destination, hour, id, duration, number_of_seats, operator, price_per_person);
            flightList.add(airport);
        }

        return flightList;
    }

    public static List<Seat> getSeats(CqlSession session, String flightCode) {
        SimpleStatement query = SimpleStatement.newInstance("SELECT * FROM seat WHERE flight='" + flightCode + "';").setTracing(true);
        ResultSet rs = session.execute(query);

        saveTrace(rs);

        List<Seat> seatList = new ArrayList<>();

        for (Row row : rs) {
            String flight = row.getString("flight");
            String id = row.getString("id");
            int balance = row.getInt("balance");
            String date_of_birth = "" + row.getLocalDate("date_of_birth");
            String document_info = row.getString("document_info");
            String name = row.getString("name");
            String status = row.getString("status");
            String surname = row.getString("surname");

            Seat seat = new Seat(flight, id, balance, date_of_birth, document_info, name, status, surname);
            seatList.add(seat);
        }

        return seatList;
    }

    public static List<Seat> getAvailableSeats(CqlSession session, String flightCode) {
        List<Seat> seats = getSeats(session, flightCode);
        List<Seat> availableSeats = new ArrayList<>();

        for (Seat seat : seats) {
            if(seat.getStatus().equals("Vacant")) {
                availableSeats.add(seat);
            }
        }

        return availableSeats;
    }

    public static String getSeatStatus(CqlSession session, String flightCode, String idSeat) throws Exception {
        SimpleStatement query = SimpleStatement.newInstance("SELECT * FROM seat WHERE flight='" + flightCode + "' AND id='"+ idSeat +"';").setTracing(true);
        ResultSet rs = session.execute(query);

        saveTrace(rs);

        Row row = rs.one();

        if (row != null) {
            return row.getString("status");
        }

        throw new Exception("This seat does not exist");
    }

    public static Flight getFirstAvailableFlight(CqlSession session, String departureCode, String destinationCode) throws Exception {
        List<Flight> flights = getFlightsFromDepartureAndDestination(session, departureCode, destinationCode);

        if (flights.isEmpty()) {
            throw new Exception("No flights found");
        }

        return flights.get(0);
    }

    public static Seat getFirstAvailableSeat(CqlSession session, String flightCode) throws Exception {
        List<Seat> seats = getAvailableSeats(session, flightCode);

        if (seats.isEmpty()) {
            throw new Exception("No seats available");
        }

        return seats.get(0);
    }

    public static void transaction(CqlSession session, User user, String departureCode, String destinationCode) {
        Flight flight;
        Seat seat;
        try {
            flight = getFirstAvailableFlight(session, departureCode, destinationCode);
            seat = getFirstAvailableSeat(session, flight.getId());
            System.out.println(execute(session, user, flight, seat));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void parrallelTransaction(CqlSession session, User user1, User user2, String departureCode, String destinationCode) {
        Flight flight;
        Seat seat;
        try {
            flight = getFirstAvailableFlight(session, departureCode, destinationCode);
            seat = getFirstAvailableSeat(session, flight.getId());

            Thread thread1 = new Thread(() -> { System.out.println(execute(session, user1, flight, seat)); });

            Thread thread2 = new Thread(() -> { System.out.println(execute(session, user2, flight, seat)); });

            thread1.start();
            thread2.start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public static String execute(CqlSession session, User user, Flight flight, Seat seat) {
        if (flight.getPrice_per_person() > user.getBalance()) {
            return "Insufficient balance";
        }

        user.setBalance(user.getBalance() - flight.getPrice_per_person());

        String updateQuery = "UPDATE seat SET status = 'Occupied', balance = ?, date_of_birth = ?, document_info = ?, name = ?, surname = ? WHERE flight = ? AND id = ? IF status = 'Vacant'";
        PreparedStatement updateStmt = session.prepare(updateQuery);

        LocalDate birthDate = LocalDate.of(
                Integer.parseInt(user.getDate_of_birth().substring(0, 4)),
                Integer.parseInt(user.getDate_of_birth().substring(5, 7)),
                Integer.parseInt(user.getDate_of_birth().substring(8, 10))
        );

        ResultSet resultSet = session.execute(updateStmt.bind(user.getBalance(), birthDate, user.getDocument_info(), user.getName(), user.getSurname(), seat.getFlight(), seat.getId()).setTracing(true).setConsistencyLevel(ConsistencyLevel.QUORUM));

        saveTrace(resultSet);

        if (!resultSet.wasApplied()) {
            return user.getName() + " " + user.getSurname() + ": seat reservation " + seat.getId() + " failed. It might already be 'Occupied'.";
        }

        return user.getName() + " " + user.getSurname() + ": seat " + seat.getId() + " successfully booked up.";
    }

    public static void insert(CqlSession session, String table) {
        List<List<String>> dati = new ArrayList<>();

        String[] fileName = {"airport.txt", "flights.txt", "seats.txt"}; // Nome del tuo file txt

        for (String s: fileName) {
            try (BufferedReader br = new BufferedReader(new FileReader(s))) {
                String line;
                List<String> list = new ArrayList<>();

                while ((line = br.readLine()) != null) {
                    list.add(line.trim());
                }

                dati.add(list);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(table.equals("airport")) {
            System.out.println("Insert Airport");
            for (String l1: dati.get(0)) {
                try {
                    SimpleStatement query = SimpleStatement.newInstance(
                            "INSERT INTO airport (Size, Country, Country_code, Geo_Point, IATA_code, ICAO_code, Name, Name_en, Name_fr, Operator, Phone, Website) VALUES " + l1 + ";");
                    query = query.setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM);
                    session.execute(query);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        } else {
            if(table.equals("flight")) {
                System.out.println("Insert Flight");
                for (String l2: dati.get(1)) {
                    try {
                        SimpleStatement query = SimpleStatement.newInstance("INSERT INTO flight (ID, Departure, Destination, Number_of_Seats, Day, Hour, Operator, Duration, Price_per_Person) VALUES " + l2 + ";");
                        query = query.setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM);
                        session.execute(query);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            } else {
                System.out.println("Insert Seat");
                for (String l3: dati.get(2)) {
                    try {
                        SimpleStatement query = SimpleStatement.newInstance("INSERT INTO seat (Flight, ID, Status, Name, Surname, Document_Info, Date_of_Birth, Balance) VALUES " + l3 + ";");
                        query = query.setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM);
                        session.execute(query);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
    }

    public static void main( String[] args ) {

        DriverConfigLoader loader = DriverConfigLoader.fromString("datastax-java-driver.basic.request.timeout = 10 seconds");

        try (CqlSession session = CqlSession.builder()
                .addContactPoint(new InetSocketAddress("localhost", 9042))
                .addContactPoint(new InetSocketAddress("localhost", 9043))
                .withLocalDatacenter("datacenter1")
                .withConfigLoader(loader)
                .build()) {

            System.out.println("Connected to Cassandra!");

            session.execute("CREATE KEYSPACE IF NOT EXISTS my_airport WITH replication = "
                    + "{'class':'SimpleStrategy','replication_factor':1};");
            session.execute("USE my_airport;");

            ResultSet rs = session.execute("SELECT table_name FROM system_schema.tables " +
                    "WHERE keyspace_name = 'my_airport' ");

            List<String> list_before = new ArrayList<>();
            for (Row row : rs) {
                list_before.add(row.getString("table_name"));
            }

            session.execute("CREATE TABLE IF NOT EXISTS airport (Name_en text, Country text, Website text, Geo_Point text, ICAO_code text, Country_code text, Phone text, Name_fr text, Name text, IATA_code text, Size int, Operator text, PRIMARY KEY (IATA_code));");
            session.execute("CREATE TABLE IF NOT EXISTS flight (ID text, Number_of_Seats int, Day date, Hour time, Operator text, Duration text, Price_per_Person int, Destination text, Departure text, PRIMARY KEY ((Departure), Destination, Day, Hour, id));");
            session.execute("CREATE TABLE IF NOT EXISTS seat (Flight text, Status text, ID text, Name text, Surname text, Document_Info text, Date_of_Birth date, Balance int, PRIMARY KEY ((Flight), ID));");

            rs = session.execute("SELECT table_name FROM system_schema.tables " +
                    "WHERE keyspace_name = 'my_airport' ");

            List<String> list_after = new ArrayList<>();
            for (Row row : rs) {
                list_after.add(row.getString("table_name"));
            }

            if (list_before.size() != list_after.size()) {
                for (String s : list_after) {
                    if (!list_before.contains(s)) {
                        insert(session, s);
                    }
                }
            }

            //Test Parallel Transaction
            System.out.println("parrallelTransaction");
            User user1 = new User(200, "2001-11-14", "CA11111XT", "Mario", "Verdi");
            User user2 = new User(300, "2000-12-01", "CAAAAAAAA", "Luca", "Rossi");
            parrallelTransaction(session, user1, user2, "BGY", "AAL");

            try {
                Thread.sleep(5000); //Just to "free" the knots
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            //Test Query getFlightsFromAirport
            System.out.println("getFlightsFromAirport");
            System.out.println(getFlightsFromAirport(session, "BGY"));

            //Test Query getAirportFromIATA
            System.out.println("getAirportFromIATA");
            System.out.println(getAirportFromIATA(session, "BGY"));

            //Test Query getFlightsFromDepartureAndDestination
            System.out.println("getFlightsFromDepartureAndDestination");
            System.out.println(getFlightsFromDepartureAndDestination(session, "BGY", "AAL"));

            //Test Query getAvailableSeats
            System.out.println("getAvailableSeats");
            System.out.println(getAvailableSeats(session, "6677f9a27acf35542d8ef4df"));

            //Test Query getSeatStatus
            System.out.println("getSeatStatus");
            try {
                System.out.println(getSeatStatus(session, "6677f9a27acf35542d8ef4df", "10B"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            //Test Simple Transaction
            user1.setBalance(300);
            System.out.println("transaction");
            transaction(session, user1, "BGY", "AAL");

            //Test Failed Transaction
            System.out.println("transaction with no balance");
            user1.setBalance(0);
            transaction(session, user1, "BGY", "AAL");
        }
    }
}
