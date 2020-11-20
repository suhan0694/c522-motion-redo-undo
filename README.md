# c522-motion-redo-undo
Implementing Undo/Redo on a textview with Accelerometer and Gyroscope.

## System Requirements
- Android Studio 3.6+ (On Mac, Windows or Linux)
- An Android Phone running Android 6.0+ with USB Debugging enabled or an Android Virtual Device with Android 6.0+. (Use the extended control options to simulate tilt or shake).

## How to Run
- Download the zip or clone the git repo. [Link](https://github.com/suhan0694/c522-motion-redo-undo)
- Open the project in Android Studio.
- Connect an Android Phone, enable USB debugging and Run the project on the application.
- Alternatively, create a AVD with Android SDK 6.0+ and run the project.

## Instructions
- Tilt the phone to the left to undo a text input operation.
- Tilt the phone to the right to redo a text input operation.
- Shake the phone vigoriously to clear all the text from the text input.
- After every operation recenter your device. The progress bar will provide a visual cue of your current roll/tilt of the device.

## Demo 

![Demo Gif](demo_gif.gif)

## References

- Code for Undo/Redo. [Google Issues](https://issuetracker.google.com/issues/36913735#c123), [Source Code](https://www.programmersought.com/article/61722948894/)
- Library for Shake implementation. [Square/Seismic](https://github.com/square/seismic)
- Android developer tutorial for Sensor-based Orientation. [Link](https://developer.android.com/codelabs/advanced-android-training-sensor-orientation#0)
- Spalshscreen Icons used from flaticon.com [Link](https://www.flaticon.com/)
