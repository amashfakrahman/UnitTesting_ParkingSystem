package parking;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WalletTest {

    @Test
    void defaultWalletShouldStartWithZero() {
        Wallet wallet = new Wallet();

        assertEquals(0.0, wallet.getBalance(), 0.0001);
    }

    @Test
    void addFundsShouldIncreaseBalance() {
        Wallet wallet = new Wallet(10.0);
        wallet.addFunds(5.0);

        assertEquals(15.0, wallet.getBalance(), 0.0001);
    }

    @Test
    void transferFundsToNullShouldNotLoseMoney() {
        Wallet from = new Wallet(100.0);

        try {
            from.transferFunds(null, 40.0);
        } catch (RuntimeException e) {
        }

        assertEquals(100.0, from.getBalance(), 0.0001);
    }


    @Test
    void deductFundsShouldReduceBalance() {
        Wallet wallet = new Wallet(50.0);
        wallet.deductFunds(20.0);

        assertEquals(30.0, wallet.getBalance(), 0.0001);
    }

    @Test
    void deductFundsShouldAllowExactBalance() {
        Wallet wallet = new Wallet(50.0);
        wallet.deductFunds(50.0);

        assertEquals(0.0, wallet.getBalance(), 0.0001);
    }

    @Test
    void constructorShouldRejectNegativeBalance() {
        assertThrows(IllegalArgumentException.class, () -> new Wallet(-20.0));
    }
}