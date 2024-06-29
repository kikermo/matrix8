# Matrix8

Matrix8 is an open-source Kotlin Multiplatform (KMP) project designed to run on a Raspberry Pi. It is a guitar pedal switcher based on the ADG2188 chip, providing control over your pedal setup. Matrix8 also runs a Bluetooth Low Energy (BLE) service, allowing remote control via BLE-enabled devices. Additionally, it can render the switcher's state on a connected display using Compose Desktop.

## Features

- **Kotlin Multiplatform**: Leverage the power of Kotlin to build and run on multiple platforms.
- **Raspberry Pi Compatible**: Designed specifically to run on Raspberry Pi hardware.
- **Guitar Pedal Switching**: Control your guitar pedals effortlessly using the ADG2188 chip.
- **BLE Service**: Remotely control the switcher via BLE.
- **Display Rendering**: Visualize the state of your switcher on a connected display using Compose Desktop.
- **Bluetooth Libraries**: Utilizes Bluez and BLE-java libraries for BLE functionality.
- **I2C Communication**: Uses pi4j library for I2C communication with the ADG2188 chip.

## Getting Started

### Prerequisites

To get started with Matrix8, you will need the following:

- A Raspberry Pi (any model with GPIO support)
- ADG2188 chip
- Display compatible with Raspberry Pi (e.g., HDMI or GPIO-based display)
- BLE-enabled device for remote control

### Installation

1. **Clone the repository**:
    ```sh
    git clone https://github.com/kikermo/matrix8.git
    cd matrix8
    ```

2. **Set up your Raspberry Pi**:
    - Ensure your Raspberry Pi is running the latest version of Raspbian.
    - Connect the ADG2188 chip to the GPIO pins of the Raspberry Pi.
    - Connect your display to the Raspberry Pi.
    - Install the Bluez library:
        ```sh
        sudo apt-get install bluez
        ```

3. **Run the project**:
    ```sh
    ./gradlew :composeApp:run
    ```

4. **Generate the project JAR for linux_arm64**:
    To generate the project JAR specifically for the `linux_arm64` architecture, run the following command:
    ```sh
    ./gradlew :composeApp:packageReleaseUberJarForCurrentOS
    ```
    This command would generate the JAR package for the machine building the project if you want to build for the Raspberry Pi from another device, you need to  change the compose desktop dependency from `implementation(compose.desktop.currentOs)` to `implementation(compose.desktop.linux_arm64)` 

### Deployment

To deploy Matrix8 to your Raspberry Pi, follow these steps:

1. **Build the project**:
    ```sh
    ./gradlew build
    ```

2. **Copy the JAR file to your Raspberry Pi**:
    - Use `scp` to copy the built JAR file to your Raspberry Pi:
        ```sh
        scp build/libs/matrix8.jar pi@<your-raspberry-pi-ip>:/home/pi/
        ```

3. **SSH into your Raspberry Pi**:
    ```sh
    ssh pi@<your-raspberry-pi-ip>
    ```

4. **Run the application on the Raspberry Pi**:
    ```sh
    java -jar /home/pi/matrix8.jar
    ```

### Usage

Once the application is running on your Raspberry Pi:

- **Switching Pedals**: Use the connected display to navigate and control your pedal setup using the UI. UI is WIP.
- **BLE Control**: Use any BLE app like [BLE scanner](https://play.google.com/store/apps/details?id=com.macdom.ble.blescanner&hl=en) or [LightBlue](https://play.google.com/store/apps/details?id=com.punchthrough.lightblueexplorer&hl=en). You can also build your own mobile app. KMM sample app WIP.

## Contributing

We welcome contributions to Matrix8! To contribute, follow these steps:

1. **Fork the repository**.
2. **Create a new branch**: `git checkout -b feature/your-feature-name`
3. **Commit your changes**: `git commit -m 'Add some feature'`
4. **Push to the branch**: `git push origin feature/your-feature-name`
5. **Open a pull request**.

Please ensure your code adheres to our coding standards and include appropriate tests.

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- [JetBrains](https://www.jetbrains.com/) for Kotlin and the KMP framework.
- The Raspberry Pi Foundation for the Raspberry Pi.
- [Analog Devices](https://www.analog.com/) for the ADG2188 chip.
- [Bluez](http://www.bluez.org/) for the Bluetooth stack.
- [BLE-java](https://github.com/sputnikdev/bluetooth-gatt-parser) library for BLE functionality.
- [pi4j](https://pi4j.com/) for I2C communication.
- [Compose Desktop](https://www.jetbrains.com/lp/compose/) for rendering the UI.

## Contact

For any questions or support, please open an issue on the GitHub repository or contact the project maintainer at kikermo@gmail.com. Feel free to reach out with any questions about the software or the hardware. Any feedback is welcomed. 
