package parking;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ParkingSlotTest {

    @Test
    void newSlotShouldBeActiveWithEmptyBookingsAndWallet() {
        ParkingSlot slot = new ParkingSlot("S1", ParkingSlotType.REGULAR);

        assertEquals("S1", slot.getSlotId());
        assertEquals(ParkingSlotType.REGULAR, slot.getSlotType());
        assertTrue(slot.isActive());
        assertTrue(slot.getBookings().isEmpty());
        assertEquals(0.0, slot.getBalance(), 0.0001);
    }

    @Test
    void deactivateShouldMakeSlotInactiveAndActivateShouldRestoreIt() {
        ParkingSlot slot = new ParkingSlot("S1", ParkingSlotType.REGULAR);

        slot.deactivate();
        assertFalse(slot.isActive());

        slot.activate();
        assertTrue(slot.isActive());
    }

    @Test
    void inactiveSlotShouldNotBeCompatible() {
        ParkingSlot slot = new ParkingSlot("S1", ParkingSlotType.REGULAR);
        LocalDateTime start = LocalDateTime.of(2026, 1, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2026, 1, 1, 11, 0);
        slot.deactivate();

        assertFalse(slot.isCompatible(VehicleType.CAR, start, end));
    }

    @Test
    void truckShouldNotFitAnySlot() {
        LocalDateTime start = LocalDateTime.of(2026, 1, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2026, 1, 1, 11, 0);

        for (ParkingSlotType type : ParkingSlotType.values()) {
            ParkingSlot slot = new ParkingSlot("x", type);
            assertFalse(slot.isCompatible(VehicleType.TRUCK, start, end));
        }
    }

    @Test
    void cancelledBookingShouldNotBlockTheSlot() {
        ParkingSlot slot = new ParkingSlot("S1", ParkingSlotType.REGULAR);
        Vehicle vehicle = new Vehicle(1, VehicleType.CAR, 100.0);
        LocalDateTime start = LocalDateTime.of(2026, 1, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2026, 1, 1, 12, 0);
        Booking booking = new Booking(1, vehicle, slot, start, end, 20.0);
        slot.getBookings().add(booking);

        booking.cancelBooking();

        assertTrue(slot.isAvailable(start, end));
    }

    @Test
    void emptySlotShouldBeAvailable() {
        ParkingSlot slot = new ParkingSlot("S1", ParkingSlotType.REGULAR);
        LocalDateTime start = LocalDateTime.of(2026, 1, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2026, 1, 1, 11, 0);

        assertTrue(slot.isAvailable(start, end));
    }
}