package parking;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VehicleTest {

    @Test
    void constructorShouldStoreIdTypeAndWallet() {
        Wallet wallet = new Wallet(40.0);
        Vehicle vehicle = new Vehicle(7, VehicleType.CAR, wallet);

        assertEquals(7, vehicle.getVehicleId());
        assertEquals(VehicleType.CAR, vehicle.getVehicleType());
        assertSame(wallet, vehicle.getWallet());
    }

    @Test
    void constructorWithBalanceShouldCreateWalletWithThatBalance() {
        Vehicle vehicle = new Vehicle(1, VehicleType.BUS, 250.5);

        assertEquals(250.5, vehicle.getWallet().getBalance(), 0.0001);
    }

    @Test
    void getBalanceShouldReturnWalletBalance() {
        Vehicle vehicle = new Vehicle(2, VehicleType.CAR, 10.0);
        vehicle.getWallet().addFunds(5.0);

        assertEquals(15.0, vehicle.getBalance(), 0.0001);
    }

    @Test
    void toStringShouldContainIdTypeAndBalance() {
        Vehicle vehicle = new Vehicle(9, VehicleType.MICROCAR, 12.5);
        String text = vehicle.toString();

        assertTrue(text.contains("vehicleId=9"));
        assertTrue(text.contains("vehicleType=MICROCAR"));
        assertTrue(text.contains("walletBalance=12.5"));
    }
}