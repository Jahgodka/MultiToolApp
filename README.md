# MultiToolApp

A modular Android application built with Jetpack Compose, designed as a scalable container for various utility tools.

## Architecture
* **UI Framework:** Jetpack Compose (Material Design 3).
* **Structure:** Single-Activity architecture. `MainActivity` acts solely as a navigation router (`Scaffold` with `TopAppBar`). Each tool is encapsulated in its own file (e.g., `DateCalculatorScreen.kt`) for strict isolation and maintainability.
* **Minimum SDK:** 26 (Android 8.0) – required for native `java.time` API support.

## Included Modules

### 1. Date Shift Calculator
![Date Calculator Main Screen](screenshots/date_calculator_main.png)

A utility to safely calculate past or future dates by adding or subtracting days, months, or years.
* **Core:** Driven by `java.time.LocalDate` and a native `DatePickerDialog`.
* **Validation:** Implements strict input validation (limits shifts to +/- 100,000 units) to prevent `DateTimeException` crashes and UI overflow.
* **Formatting:** Era-aware formatting (BCE/CE via `Locale`) to properly handle edge cases for large historical or future shifts.
* **UI/UX:** Custom state-driven color logic applied to disabled/focused states using `colorResource`.

### 2. Date Difference Calculator
![Date Difference Main Screen](screenshots/date_difference_main.png)

A utility to calculate the exact absolute difference between two specific dates and times, breaking down the result into days, hours, minutes, and seconds.
* **Core:** Utilizes `java.time.LocalDateTime` and `java.time.Duration` for highly accurate, millisecond-level time math.
* **UI/UX:** Implements parallel `Row` structures combining `DatePickerDialog` and `TimePickerDialog`. Features a custom "Now" trailing icon button that captures the exact current system time (including seconds, bypassing standard Android UI limitations).
* **Calculation:** Employs absolute duration parsing (`abs()`) and modulo arithmetic to properly separate total time into distinct, readable units without overlap.
