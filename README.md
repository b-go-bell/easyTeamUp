# Easy Team Up
By Bridget Bell, Jarret Spino, and Isaac Gerstmann

### Testing Instructions
* Please run your emulator before starting the tests.
* Please choose an android image that is running version 32 or later. One of the developers is running on Apple silicon which limits backward compatibility. Ensure the emulator has location services enabled. Any screen size works, but it is recommended to use a larger phone screen size; the lead frontend developer used a Pixel 4 for reference. Also, ensure that your emulator has enough RAM allocated.
* All build configurations are stored in a `.run` file in the project's root directory. At the top of Android Studio (next to the green hammer), you'll find a couple of different testing configurations based on what you want to test:
    - **if you’d like to run all tests,** please select the **AllTests** configuration, then run.
    - **if you’d like to run all whitebox tests,** please select the **BackendTests** configuration, then run.
    - **if you’d like to run all blackbox tests,** please select the **FrontendTests** configuration, then run.
    - **if you’d like to run a specific subset of blackbox tests,** there are four subsets of blackbox tests (as described below)
        1. **UserAccountTests** 
        2. **HostEventTests** 
        3. **AttendPublicEventsTest** 
        4. **ViewEventTests**
* When you are ready, run the tests. The test results will display at the bottom of the screen and, for the `Espresso` tests, you will be able to see the actions on the emulator as they are performed.

### Video Walkthrough
We have also recorded video walkthroughs of the tests. To watch, go to this link: https://drive.google.com/drive/folders/1l0mOhEVj42IiaT-4ToIqxxzIXU-PnRBl.