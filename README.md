# Parking System — Unit Testing & Mutation Analysis

A Java-based parking management system used to practice **unit testing**, **boundary-case testing**, **defect discovery**, and **mutation testing**.

The project models vehicles, parking slots, bookings, wallets, pricing rules, cancellations, and booking completion. Tests are written with **JUnit 5**, while **PIT** is used to evaluate the strength of the test suite through mutation analysis.

## Features

- Register vehicles and parking slots
- Find parking slots compatible with a vehicle and time range
- Create, complete, and cancel bookings
- Manage payments through wallet-to-wallet transfers
- Apply vehicle-based and slot-based pricing multipliers
- Track booking states: `ACTIVE`, `COMPLETED`, and `CANCELLED`
- Test normal cases, boundary cases, invalid input, and transaction behavior
- Run PIT mutation analysis to measure test effectiveness

## Tech Stack

- **Java 17**
- **Maven**
- **JUnit Jupiter 5.10.2**
- **Maven Surefire Plugin 3.2.5**
- **PIT 1.15.8**
- **PIT JUnit 5 Plugin 1.2.1**

## Project Structure

```text
.
├── pom.xml
├── src
│   ├── main
│   │   └── java
│   │       └── parking
│   │           ├── Booking.java
│   │           ├── BookingStatus.java
│   │           ├── ParkingSlot.java
│   │           ├── ParkingSlotType.java
│   │           ├── ParkingSystem.java
│   │           ├── Vehicle.java
│   │           ├── VehicleType.java
│   │           └── Wallet.java
│   └── test
│       └── java
│           └── parking
│               ├── BookingTest.java
│               ├── ParkingSlotTest.java
│               ├── ParkingSystemTest.java
│               ├── VehicleTest.java
│               └── WalletTest.java
└── target
    └── pit-reports
```

## Core Classes

| Class | Responsibility |
|---|---|
| `ParkingSystem` | Coordinates vehicles, slots, bookings, pricing, payments, cancellation, and completion |
| `ParkingSlot` | Stores slot type, active state, wallet, bookings, compatibility, and availability |
| `Booking` | Represents a reservation and its lifecycle state |
| `Vehicle` | Stores vehicle ID, vehicle type, and wallet |
| `Wallet` | Handles balances, deposits, deductions, and transfers |

## Pricing Model

The base parking rate is **10.0 per hour**.

### Vehicle Multipliers

| Vehicle Type | Multiplier |
|---|---:|
| Bicycle | `0.2x` |
| Motorcycle | `0.5x` |
| Car | `1.0x` |
| Microcar | `1.5x` |
| Bus | `2.0x` |
| Truck | `3.0x` |

### Parking Slot Multipliers

| Slot Type | Multiplier |
|---|---:|
| Compact | `0.8x` |
| Regular | `1.0x` |
| Handicapped | `1.2x` |
| Large | `1.5x` |

The intended price calculation is:

```text
price = duration_in_hours
      × base_hourly_rate
      × vehicle_multiplier
      × slot_multiplier
```

## Getting Started

### Prerequisites

Install:

- JDK 17 or newer
- Apache Maven

Verify the installation:

```bash
java -version
mvn -version
```

### Clone the Repository

```bash
git clone <your-repository-url>
cd <your-repository-folder>
```

### Compile

```bash
mvn clean compile
```

## Running Unit Tests

Run the complete test suite:

```bash
mvn test
```

> **Note:** The current repository exposes several defects, so the complete test run is not currently green and Maven may exit with a test-failure status.

### Run an Individual Test Class

For example:

```bash
mvn -Dtest=WalletTest test
```

Other test classes can be executed with:

```bash
mvn -Dtest=BookingTest test
mvn -Dtest=ParkingSlotTest test
mvn -Dtest=ParkingSystemTest test
mvn -Dtest=VehicleTest test
```

## Current Test Results

The latest recorded test run contains **38 tests**.

| Result | Count |
|---|---:|
| Passed | 26 |
| Failed | 10 |
| Errors | 2 |
| **Total** | **38** |
| **Passing Rate** | **68.42%** |

The failing and error tests reveal defects involving input validation, fractional-hour pricing, cancellation behavior, payment atomicity, and repeated completion handling.

## Mutation Testing

This project uses **PIT** to evaluate how effectively the unit tests detect changes in the production code.

Run PIT using:

```bash
mvn test-compile org.pitest:pitest-maven:mutationCoverage
```

After execution, open the generated report:

```text
target/pit-reports/index.html
```

### Latest Mutation Analysis

- **Mutation score:** 5% (`4 / 87`)
- **Line coverage:** 8% (`11 / 133`)
- **Test strength:** 44% (`4 / 9`)
- **PIT version:** 1.15.8

The current mutation score shows that many generated mutants are not reached or detected by the test execution, particularly in `ParkingSlot` and `ParkingSystem`.

## Identified Defects

The tests currently expose the following root-cause issues:

1. `Wallet` accepts a negative initial balance.
2. Transferring funds to a `null` wallet can deduct money before the operation fails.
3. A completed booking can still enter cancellation and refund logic.
4. Cancelled bookings can continue blocking their previous time range.
5. Fractional booking durations are truncated, producing incorrect prices and zero-cost bookings below one hour.
6. Cancelled bookings can still be completed and paid out.
7. Failed payments can leave a booking stored in the system.
8. Completing the same booking multiple times can pay the parking slot repeatedly.
9. `Booking` accepts negative amounts.
10. `Booking` can be created with an invalid time range.

These defects provide targets for improving both the production implementation and the test suite.

## Testing Focus

The unit tests cover:

- Constructors and getters
- String representations
- Wallet deposits, deductions, and transfers
- Exact-balance boundary conditions
- Insufficient-funds handling
- Vehicle and parking-slot compatibility
- Slot activation and deactivation
- Booking status transitions
- Availability after booking cancellation
- Fractional-duration pricing
- Vehicle pricing multipliers
- Parking-slot pricing multipliers
- Payment transaction atomicity
- Idempotent booking completion
- `ParkingSystem` singleton behavior

## Suggested Improvements

Future improvements to the project include:

- Validate initial balances in `Wallet`.
- Validate booking amounts and time ranges.
- Calculate duration using fractional hours rather than whole-hour truncation.
- Make payment and booking creation atomic.
- Ignore cancelled bookings when checking parking-slot availability.
- Restrict cancellation and completion to valid booking states.
- Make booking completion idempotent.
- Expand tests to cover surviving PIT mutants.
- Re-run mutation analysis after fixing each defect.

## Author

**A M Ashfak Rahman**

## License

No license is currently specified for this project. Add a `LICENSE` file if the repository will be publicly distributed or reused.
