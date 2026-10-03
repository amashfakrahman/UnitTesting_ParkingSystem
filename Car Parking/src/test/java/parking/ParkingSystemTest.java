package parking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ParkingSystemTest {

    private ParkingSystem system;

    @BeforeEach
    void setUp() {
        system = ParkingSystem.getInstance();
        system.resetForTesting();
    }

    @Test
    void getInstanceShouldAlwaysReturnSameObject() {
        assertSame(ParkingSystem.getInstance(), ParkingSystem.getInstance());
    }

    @Test
    void addVehicleAndAddParkingSlotShouldStoreThem() {
        Vehicle vehicle = new Vehicle(1, VehicleType.CAR, 10.0);
        ParkingSlot slot = new ParkingSlot("S1", ParkingSlotType.REGULAR);

        system.addVehicle(vehicle);
        system.addParkingSlot(slot);

        assertEquals(1, system.getVehicles().size());
        assertSame(vehicle, system.getVehicles().get(0));
        assertEquals(1, system.getParkingSlots().size());
        assertSame(slot, system.getParkingSlots().get(0));
    }

    @Test
    void bookShouldChargeNinetyMinutesAsOneAndHalfHours() {
        Vehicle vehicle = new Vehicle(1, VehicleType.CAR, 100.0);
        ParkingSlot slot = new ParkingSlot("S1", ParkingSlotType.REGULAR);
        LocalDateTime start = LocalDateTime.of(2026, 1, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2026, 1, 1, 11, 30);

        Booking booking = system.book(vehicle, slot, start, end);

        assertEquals(15.0, booking.getAmount(), 0.0001);
    }

    @Test
    void bookShouldAllowBookingShorterThanOneHour() {
        Vehicle vehicle = new Vehicle(1, VehicleType.CAR, 100.0);
        ParkingSlot slot = new ParkingSlot("S1", ParkingSlotType.REGULAR);
        LocalDateTime start = LocalDateTime.of(2026, 1, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2026, 1, 1, 10, 30);

        Booking booking = system.book(vehicle, slot, start, end);

        assertEquals(5.0, booking.getAmount(), 0.0001);
    }

    @Test
    void bookShouldRejectIncompatibleSlot() {
        Vehicle bus = new Vehicle(1, VehicleType.BUS, 500.0);
        ParkingSlot slot = new ParkingSlot("S1", ParkingSlotType.REGULAR);
        LocalDateTime start = LocalDateTime.of(2026, 1, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2026, 1, 1, 11, 0);

        assertThrows(IllegalArgumentException.class, () -> system.book(bus, slot, start, end));
        assertEquals(500.0, bus.getBalance(), 0.0001);
        assertTrue(system.getBookings().isEmpty());
    }

    @Test
    void bookShouldRejectVehicleWithoutEnoughMoney() {
        Vehicle vehicle = new Vehicle(1, VehicleType.CAR, 9.99);
        ParkingSlot slot = new ParkingSlot("S1", ParkingSlotType.REGULAR);
        LocalDateTime start = LocalDateTime.of(2026, 1, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2026, 1, 1, 11, 0);

        assertThrows(InsufficientFundsException.class, () -> system.book(vehicle, slot, start, end));
        assertEquals(9.99, vehicle.getBalance(), 0.0001);
    }

    @Test
    void motorcyclePriceShouldBeHalfOfBase() {
        Vehicle vehicle = new Vehicle(1, VehicleType.MOTORCYCLE, 100.0);
        ParkingSlot slot = new ParkingSlot("S1", ParkingSlotType.REGULAR);
        LocalDateTime start = LocalDateTime.of(2026, 1, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2026, 1, 1, 11, 0);

        assertEquals(5.0, system.book(vehicle, slot, start, end).getAmount(), 0.0001);
    }

    @Test
    void failedPaymentShouldNotLeaveBookingInSystem() {
        Vehicle poor = new Vehicle(1, VehicleType.CAR, 1.0);
        ParkingSlot slot = new ParkingSlot("S1", ParkingSlotType.REGULAR);
        LocalDateTime start = LocalDateTime.of(2026, 1, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2026, 1, 1, 11, 0);

        assertThrows(InsufficientFundsException.class, () -> system.book(poor, slot, start, end));

        assertTrue(system.getBookings().isEmpty());
    }

    @Test
    void microcarPriceShouldBeOnePointFiveTimesBase() {
        Vehicle vehicle = new Vehicle(1, VehicleType.MICROCAR, 100.0);
        ParkingSlot slot = new ParkingSlot("S1", ParkingSlotType.REGULAR);
        LocalDateTime start = LocalDateTime.of(2026, 1, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2026, 1, 1, 11, 0);

        assertEquals(15.0, system.book(vehicle, slot, start, end).getAmount(), 0.0001);
    }


    @Test
    void completeBookingAfterCancelShouldNotPaySlot() {
        Vehicle vehicle = new Vehicle(1, VehicleType.CAR, 100.0);
        ParkingSlot slot = new ParkingSlot("S1", ParkingSlotType.REGULAR);
        Booking first = system.book(vehicle, slot, LocalDateTime.of(2026, 1, 1, 10, 0), LocalDateTime.of(2026, 1, 1, 11, 0));
        system.book(vehicle, slot, LocalDateTime.of(2026, 1, 1, 11, 0), LocalDateTime.of(2026, 1, 1, 12, 0));
        system.cancelBooking(first);

        try {
            system.completeBooking(first);
        } catch (RuntimeException e) {
        }

        assertEquals(0.0, slot.getBalance(), 0.0001);
    }

    @Test
    void largeSlotShouldCostFiftyPercentMore() {
        Vehicle vehicle = new Vehicle(1, VehicleType.CAR, 100.0);
        ParkingSlot slot = new ParkingSlot("S1", ParkingSlotType.LARGE);
        LocalDateTime start = LocalDateTime.of(2026, 1, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2026, 1, 1, 11, 0);

        assertEquals(15.0, system.book(vehicle, slot, start, end).getAmount(), 0.0001);
    }

    @Test
    void handicappedSlotShouldCostTwentyPercentMore() {
        Vehicle vehicle = new Vehicle(1, VehicleType.BICYCLE, 100.0);
        ParkingSlot slot = new ParkingSlot("S1", ParkingSlotType.HANDICAPPED);
        LocalDateTime start = LocalDateTime.of(2026, 1, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2026, 1, 1, 11, 0);

        assertEquals(2.4, system.book(vehicle, slot, start, end).getAmount(), 0.0001);
    }

    @Test
    void completeBookingTwiceShouldNotPaySlotTwice() {
        Vehicle vehicle = new Vehicle(1, VehicleType.CAR, 100.0);
        ParkingSlot slot = new ParkingSlot("S1", ParkingSlotType.REGULAR);
        Booking first = system.book(vehicle, slot, LocalDateTime.of(2026, 1, 1, 10, 0), LocalDateTime.of(2026, 1, 1, 11, 0));
        system.book(vehicle, slot, LocalDateTime.of(2026, 1, 1, 11, 0), LocalDateTime.of(2026, 1, 1, 12, 0));

        system.completeBooking(first);
        try {
            system.completeBooking(first);
        } catch (RuntimeException e) {
        }

        assertEquals(8.0, slot.getBalance(), 0.0001);
    }

    @Test
    void cancelBookingAfterCompleteShouldNotRefund() {
        Vehicle vehicle = new Vehicle(1, VehicleType.CAR, 100.0);
        ParkingSlot slot = new ParkingSlot("S1", ParkingSlotType.REGULAR);
        Booking first = system.book(vehicle, slot, LocalDateTime.of(2026, 1, 1, 10, 0), LocalDateTime.of(2026, 1, 1, 11, 0));
        system.book(vehicle, slot, LocalDateTime.of(2026, 1, 1, 11, 0), LocalDateTime.of(2026, 1, 1, 12, 0));
        system.completeBooking(first);

        try {
            system.cancelBooking(first);
        } catch (RuntimeException e) {
        }

        assertEquals(80.0, vehicle.getBalance(), 0.0001);
    }

    @Test
    void cancelledBookingShouldFreeTheSlotAgain() {
        Vehicle vehicle = new Vehicle(1, VehicleType.CAR, 100.0);
        ParkingSlot slot = new ParkingSlot("S1", ParkingSlotType.REGULAR);
        LocalDateTime start = LocalDateTime.of(2026, 1, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2026, 1, 1, 12, 0);
        Booking booking = system.book(vehicle, slot, start, end);
        system.cancelBooking(booking);

        assertNotNull(system.book(vehicle, slot, start, end));
    }
}