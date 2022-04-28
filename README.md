# Easy Team Up
By Bridget Bell, Jarret Spino, and Isaac Gerstmann

### 2.5 Updates 
#### Improved Date Time Picker Dialogs for Hosts & Attendees 
A major thing we felt lacking in our 2.3 submission was not only the lack of user-friendliness, but also the buggy-ness of the date time pickers for event due time, host availability time, and attendee availability time. There were issues with the calendar in that the default Android pickers allowed people to pick times that had already past, or perhaps weren't resitricted by due time and/or host availability time. There was also a bug that when due time was set by a host, the current seconds were appended, and if it wasn't exactly 00, then the the event would never be scheduled. 
So in 2.5, we completely removed the existing date-time picker ingrastructure and started anew. While I used a GitHub directory I found as a template, much of it was customized, especially due to the fact the source code was written in Java. Although it is completely new and hasn't undergone as rigorous testing, we feel it is a vast improvement, and better conforms to the original project guidelines since now attendee time is limited by host availability. 

#### Notifications
The other big gap in our 2.3 submission was the lack of notifications. While we did provide notifcations to attendees when the due date passed and a time had been scheduled, we did not provide notifications of any other kind. In 2.5, we significantly improved the number of notifications, and designed a RESTful API that would allow for speed and future expandability. In general, most of the heavy-lifting for notifcations occurs on the server, with the app itself simply initializing notifcations and handling them upon receipt. Here is a complete list of notifcations that our app now handles:
* All attendees are notified when a final time for an event has been scheduled
* All attendees are notified when event details are notified
* Host is notified when a user RSVPs for an event
* Host is notified when a user withdraws from an event
* Host is notified when a user accepts an invitation
* Host is notified when a user rejects an invitation

#### Bug Fixes
* Fixed an error with `getEventsByEventIds()` where `null` would be returned if more than 10 ids were provided

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
