package parking;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BookingTest {

    @Test
    void constructorShouldStoreAllValues() {
        Vehicle vehicle = new Vehicle(1, VehicleType.CAR, 100.0);
        ParkingSlot slot = new ParkingSlot("S1", ParkingSlotType.REGULAR);
        LocalDateTime start = LocalDateTime.of(2026, 1, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2026, 1, 1, 11, 0);

        Booking booking = new Booking(5, vehicle, slot, start, end, 10.0);

        assertEquals(5, booking.getBookingId());
        assertSame(vehicle, booking.getVehicle());
        assertSame(slot, booking.getParkingSlot());
        assertEquals(start, booking.getStartTime());
        assertEquals(end, booking.getEndTime());
        assertEquals(10.0, booking.getAmount(), 0.0001);
    }

    @Test
    void newBookingShouldBeActive() {
        Vehicle vehicle = new Vehicle(1, VehicleType.CAR, 100.0);
        ParkingSlot slot = new ParkingSlot("S1", ParkingSlotType.REGULAR);
        LocalDateTime start = LocalDateTime.of(2026, 1, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2026, 1, 1, 11, 0);

        Booking booking = new Booking(1, vehicle, slot, start, end, 10.0);

        assertEquals(BookingStatus.ACTIVE, booking.getBookingStatus());
    }

    @Test
    void completeBookingShouldChangeStatusToCompleted() {
        Vehicle vehicle = new Vehicle(1, VehicleType.CAR, 100.0);
        ParkingSlot slot = new ParkingSlot("S1", ParkingSlotType.REGULAR);
        LocalDateTime start = LocalDateTime.of(2026, 1, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2026, 1, 1, 11, 0);
        Booking booking = new Booking(1, vehicle, slot, start, end, 10.0);

        booking.completeBooking();

        assertEquals(BookingStatus.COMPLETED, booking.getBookingStatus());
    }

    @Test
    void cancelBookingShouldChangeStatusToCancelled() {
        Vehicle vehicle = new Vehicle(1, VehicleType.CAR, 100.0);
        ParkingSlot slot = new ParkingSlot("S1", ParkingSlotType.REGULAR);
        LocalDateTime start = LocalDateTime.of(2026, 1, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2026, 1, 1, 11, 0);
        Booking booking = new Booking(1, vehicle, slot, start, end, 10.0);

        booking.cancelBooking();

        assertEquals(BookingStatus.CANCELLED, booking.getBookingStatus());
    }

    @Test
    void toStringShouldContainIdAmountAndStatus() {
        Vehicle vehicle = new Vehicle(1, VehicleType.CAR, 100.0);
        ParkingSlot slot = new ParkingSlot("S1", ParkingSlotType.REGULAR);
        LocalDateTime start = LocalDateTime.of(2026, 1, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2026, 1, 1, 11, 0);
        Booking booking = new Booking(3, vehicle, slot, start, end, 10.0);

        String text = booking.toString();

        assertTrue(text.contains("bookingId=3"));
        assertTrue(text.contains("amount=10.0"));
        assertTrue(text.contains("bookingStatus=ACTIVE"));
    }

    @Test
    void constructorShouldRejectNegativeAmount() {
        Vehicle vehicle = new Vehicle(1, VehicleType.CAR, 100.0);
        ParkingSlot slot = new ParkingSlot("S1", ParkingSlotType.REGULAR);
        LocalDateTime start = LocalDateTime.of(2026, 1, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2026, 1, 1, 11, 0);

        assertThrows(IllegalArgumentException.class,
                () -> new Booking(1, vehicle, slot, start, end, -10.0));
    }

    @Test
    void constructorShouldRejectEndBeforeStart() {
        Vehicle vehicle = new Vehicle(1, VehicleType.CAR, 100.0);
        ParkingSlot slot = new ParkingSlot("S1", ParkingSlotType.REGULAR);
        LocalDateTime start = LocalDateTime.of(2026, 1, 1, 11, 0);
        LocalDateTime end = LocalDateTime.of(2026, 1, 1, 10, 0);

        assertThrows(RuntimeException.class,
                () -> new Booking(1, vehicle, slot, start, end, 10.0));
    }
}