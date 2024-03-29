# Contents

- Elevator-System-Simulation
- Set Up and Test Instructions
- Authors
- Iterations
  - Iteration 1
    - Folders/Files included
    - Team Responsibilities
    - Detailed Set Up
  - Iteration 2
    - Folders/Files included
    - Team Responsibilities
    - Detailed Set Up
  - Iteration 3
    - Folders/Files included
    - Team Responsibilities
    - Detailed Set Up
  - Iteration 4
    - Folders/Files included
    - Team Responsibilities
    - Detailed Set Up
  - Iteration 5
    - Folders/Files included
    - Team Responsibilities
    - Detailed Set Up

# Elevator-System-Simulation

This project is a design and implementation of an elevator control system and simulator. The system consist of an elevator controller (the Scheduler), a simulator for the elevator cars (which includes, the lights, buttons, doors and motors) and a simulator for the floors (which includes, buttons, lights and last, but not least, people who are too lazy to take the stairs). The elevator controller is multi-threaded since it will handle more than one car at a time. The simulation is configurable in terms of the number of floors, the number number of elevators, the time it takes to open and close the doors, and the time it takes to move between floors. The simulation will run on multiple computers as part of the project involves running the controller on its own machine with the simulator(s) running on a separate computer. The code is written in Java, using the Eclipse IDE.

# Set Up and Test Instructions

1.  Download zip file and unzip
2.  Open unzipped file through the Eclipse file system
3.  Locate the 3 `Main.java` files: `scheduler/Main.java`, `elevator/Main.java`, and `floor/Main.java`. Each `Main.java` represents one part of the Elevator-System, they are meant to be running simultaneously.
4.  Launch each `Main.java` by right-clicking -> Run as Java application. Start with `scheduler/Main.java`, the order of the other two does not matter.
5.  Configure the system using the configuration UI and press start when ready. Note: the start button will be disabled until the other two applications have sent their handshake packets.

Tests are written and run with JUnit 5. The 'test' folder contains a test package for each subsystem

1.  Upon opening the test folder (In Eclipse), select subsystem package
2.  Open .java file and run as 1 JUnit test

# Configuration

Configurable parameters are located in `src/common/Constants.java`. 
This includes configuring the number of floors and elevators, the various ports, the input file path and the elevator movement durations.

# Authors

- Aubin Musingya
- Chris Nguyen
- James La Novara-Gsell
- Liya Abebe
- Noah Mank

# Iterations

<details><summary>Iteration 1</summary>

# Iteration 1

## Folders/Files included

    * resources
        + input.txt

    * src
        - elevator
            + Direction.java
            + ElevatorSubsystem.java
            + Elevator.java
        - floor
            + FloorSubsystem.java
            + InputData.java
            + InputParser.java
        - main
            + Main.java
            + SystemBuilder
        - scheduler
            + Scheduler.java
            + Buffer.java
        - UML Diagrams - Iteration 1
            + UML-Class-Diagram-IT1 .png
            + UML-Class-Diagram-IT1 .violet
            + UML-Sequence-Diagram-IT1 .png
            + UML-Sequence-Diagram-IT1 .violet

    * test
        - elevator
            + ElevatorSubsystemTest.java
            + Elevator.java
        - floor
            + FloorSubsystemTest.java
            + InputDataTest.java
            + InputParserTest.java
        - scheduler
            + SchedulerTest.java
            + BufferTest.java

## Team Responsibilities

Aubin

- UML class diagram
- UML sequence diagram
- Updating README file

Chris

- Implementing ElevatorSubsystem class
- Writing ElevatorSubsystem test
- Implementing Elevator class

James

- Implementing InputParser class
- Writing InputParser test
- Integration testing

Liya

- Implementing FloorSubsystem class
- Writing FloorSubsystem Test

Noah

- Implementing Buffer class
- Writing Buffer test
- Implementing Scheduler class
- Writing Scheduler test

## Detailed Set Up

The three thread classes created are FloorSubsystem, Scheduler and ElevatorSystem. A template Buffer object class was created from which multiple buffer objects are created. The FloorSubsystem class reads input data from the provided file (input.txt) and puts each line of data into a buffer object shared with the Scheduler class. Following that, the Scheduler class gets the data from the shared buffer object and processes it. The Scheduler class will contain a buffer object for each elevatorSubsystem that exists. These buffer objects are shared with the ElevatorSubsystem. Once the data is processed, the scheduler will put elevator schedules into the corresponding buffer objects and notify the corresponding ElevatorSubsystem. Since each elevator will have its own subsystem, they will be instructed to move by the corresponding ElevatorSubsystem. The elevator will then be responsible for indicating which floors it leaves and which floors in arrives on.

</details>

<details><summary>Iteration 2</summary>

# Iteration 2

## Folders/Files included

    * resources
        + input.txt

    * src
        - elevator
            + Direction.java
            + Elevator.java
            + ElevatorEvent.java
            + ElevatorState.java
            + ElevatorSubsystem.java
        - floor
            + FloorSubsystem.java
            + InputData.java
            + InputParser.java
        - main
            + Main.java
        - scheduler
            + Scheduler.java
            + Buffer.java
            + SchedulerState.java

    * UML Diagrams - Iteration 1
        - UML-Class-Diagram-IT1 .png
        - UML-Class-Diagram-IT1 .violet
        - UML-Sequence-Diagram-IT1 .png
        - UML-Sequence-Diagram-IT1 .violet

    * test
        - elevator
            + ElevatorSubsystemTest.java
            + Elevator.java
        - floor
            + FloorSubsystemTest.java
            + InputDataTest.java
            + InputParserTest.java
        - scheduler
            + SchedulerTest.java
            + BufferTest.java

## Team Responsibilities

Aubin

- UML class diagram
- UML sequence diagram
- Converting elevator to state machine

Chris

- ElevatorState enums

James

- Converting scheduler to state machine
- State diagram

Liya

- ElevatorEvent.java and integrating buffers to this class
- Updating README file

Noah

- SchedulerState enums

## Detailed Set Up

In this iteration, state machines for the scheduler and elevator subsystems were added assuming that there is only one elevator. A state enum class was created for both Elevator and Scheduler classes. An Elevator Event class was also created which is passes from the elevators to the elevatorSubsystems when an elevator arrives at a floor. This object is then passed from the elevatorSubsystems to the scheduler then to the floorSubsystem where it is printed out.

</details>

<details><summary>Iteration 3</summary>

# Iteration 3

## Folders/Files included

    * resources
        + input.txt

    * src
        - common
            + Buffer.java
            + Constants.java
            + IBufferInput.java
            + IBufferOutput.java
        - elevator
            + Direction.java
            + Elevator.java
            + ElevatorCommand.java
            + ElevatorCommunicator.java
            + ElevatorEvent.java
            + ElevatorState.java
            + ElevatorSubsystem.java
            + Main.java
        - floor
            + FloorReceiver.java
            + FloorSubsystem.java
            + InputData.java
            + InputParser.java
            + Main.java
        - main
            + Main.java
            + SystemBuilder
        - scheduler
            + Main.java
            + ScheduledJob.java
            + Scheduler.java
            + SchedulerElevator.java
            + SchedulerMessage.java
            + SchedulerState.java

    * UML Diagrams
            + StateDiagram.mdj
            + StateDiagram.png
            + UML-Class-Diagram-IT1 .png
            + UML-Class-Diagram-IT1 .violet
            + UML-Class-Diagram-IT3 .png
            + UML-Class-Diagram-IT3 .violet
            + UML-Sequence-Diagram-IT1 .png
            + UML-Sequence-Diagram-IT1 .violet

    * test
        - elevator
            + ElevatorSubsystemTest.java
            + Elevator.java
        - floor
            + FloorSubsystemTest.java
            + InputDataTest.java
            + InputParserTest.java
        - scheduler
            + SchedulerTest.java
            + BufferTest.java

## Team Responsibilities

Aubin

- Updating InputData
- UML sequence diagram
- Writing ElevatorSubsystemTest

Chris

- Updating ElevatorSubsystem
- Implementing FloorReceiver
- Writing FloorReceiver test

James

- Updating Scheduler
- Writing statemachine tests for Scheduler
- Updating Statemachine diagram

Liya

- Implementing ElevatorCommunicator
- Updating README and UML diagram

Noah

- Implementing ElevatorCommand
- Implementing ElevatorEvent
- Updating FloorSubsystem

## Detailed Set Up

The floor, scheduler and elevator subsystems now communicate using UDP. The `floorSubsystem.java` class reads requests from the input.txt file and sends the requests (in the form of InputData bytes) to `FloorReceiver.java` which is at port 70. Then FloorReceiver changes the InputData into SchedulerMessage and puts it in an IBufferInput which can be accessed by the `Scheduler.java` class.

`Scheduler.java` schedules the request and puts an ElevatorCommand into a IBufferInput which can be accessed by `ElevatorCommunicator.java` class. This class then sends the ElevatorCommand it received to the respective `ElevatorSubsystem.java` class which is at (port 50 + elevatorId received in the command).

The ElevatorSubsystem moves its elevator according to the `ElevatorCommand` it received then sends an `ElevatorEvent` to the ElevatorCommunicator. The scheduler is notified that that the elevator has moved when the ElevatorCommunicator puts this ElevatorEvent in an IBufferOutput connected to the scheduler.

</details>

<details><summary>Iteration 4</summary>

# Iteration 4

## Folders/Files included

    * resources
        + input.txt

    * src
        - common
            + Buffer.java
            + Clock.java
            + Constants.java
            + IBufferInput.java
            + IBufferOutput.java
        - elevator
            + Direction.java
            + ElevatorCommand.java
            + ElevatorCommunicator.java
            + ElevatorDoor.java
            + ElevatorDoorCommand.java
            + ElevatorEvent.java
            + ElevatorMotor.java
            + ElevatorMoveCommand.java
            + ElevatorState.java
            + ElevatorSubsystem.java
            + Fault.java
            + Main.java
        - floor
            + FloorReceiver.java
            + FloorSubsystem.java
            + InputData.java
            + InputParser.java
            + Main.java
        - main
            + Main.java
            + SystemBuilder
        - scheduler
            + Main.java
            + ScheduledJob.java
            + Scheduler.java
            + SchedulerElevator.java
            + SchedulerMessage.java
            + SchedulerState.java
            + SystemSync.java

    * UML Diagrams
            + StateDiagram.mdj
            + StateDiagram.png
            + UML-Class-Diagram-IT1 .png
            + UML-Class-Diagram-IT1 .violet
            + UML-Class-Diagram-IT3 .png
            + UML-Class-Diagram-IT3 .violet
            + UML-Sequence-Diagram-IT1 .png
            + UML-Sequence-Diagram-IT1 .violet

    * test
        - elevator
            + ElevatorCommandTest.java
            + ElevatorDoorCommandTest.java
            + ElevatorDoorTest.java
            + ElevatorEventTest.java
            + ElevatorMotorTest.java
            + ElevatorMoveCommandTest.java
            + ElevatorSubsystemTest.java
            + Elevator.java
        - floor
            + FloorSubsystemTest.java
            + InputDataTest.java
            + InputParserTest.java
        - scheduler
            + BufferTest.java
            + SchedulerTest.java
            + SystemSyncTest.java

## Team Responsibilities

Aubin

- Updating ElevatorEvent
- UML Sequence and Class Diagram


Chris

- Implementing Clock
- Updating README file


James

- Implementing Clock
- Updating Input data
- Updating Constant
- Updating Statemachine diagram
- Updating Scheduler to handle faults
- Implementing ElevatorDoor to open and close

Liya

- Implementing SystemSync
- Updating ElevatorSunsystem

Noah

- Implementing ElevatorMotor
- Implementing ElevatorDoor
- Updating ElevatorCommand

## Detailed Set Up

The Elevator System now simulates real time and the floor, scheduler and elevator subsystems now detects and handles faults. The scheduler instantiates  a `SystemSync` which is utilized to synchronize all three subsystems. Before the `Clock` can starts, the `SystemSync` must wait until the elevator and floor subsystem send a message using UDP to notify it that they're ready to start.
Once both ready messages have been received, the `SystemSync` will start the Clock and the `FloorSubsystem` can begin reading requests from the input.txt file. The last parameter in a request will determine the fault type. Parameter value of 1 will have no fault, 2 will has a transient fault and 3 will be a permanent fault. The `FloorSubsystem` sends the requests in the form of `InputData` 
to the `FloorReceiver` on port 70.

The `InputData` is then turned into a `ScheduleMessage` and placed into an IBufferInput where the `Scheduler` can access it. The `Scheduler` creates a `ScheduledJob` and assigns it to an appropriate elevator by creating an `ElevatorMoveCommand` and putting it into a buffer where the `ElevatorCommunicator` can access it. The `ElevatorCommunicator` will then 
send this command to the appropriate `ElevatorSubsystem` on its distinct port.

The `ElevatorSubsystem` determines if the command is an `ElevatorMoveCommand` or an `ElevatorDoorCommand`. If the command contains a parameter indicating it has a permanent fault, the `ElevatorSubsystem` will set its state to `DISABLED`. In the case of a `ElevatorMoveCommand`, the `ElevatorMotor` will move the elevator 
1 floor in real time in the appropriate direction if it does not contain a fault. Whereas a transient fault will delay the elevator in the time required to overcome a transient fault and move one floor. In the case of a `ElevatorDoorCommand`, the `ElevatorDoor` will wait the appropriate time to open/close doors if there is no fault. If there is a transient fault, the elevator will wait until the 
transient fault has been overcome and the time required to open/close the doors. Once the command has been processed, a `ElevatorEvent` is created and sent to the `ElevatorCommunicator` to notify the `Scheduler` that the elevator has processed the task. If a permanent fault occurs to an elevator with `ScheduledJob`s, the jobs are then reassigned and the elevator is removed to prevent scheduling jobs 
to an out of service elevator.


</details>

<details><summary>Iteration 5</summary>

# Iteration 5

## Folders/Files included

    * resources
        + input.txt

    * src
        - common
            + Buffer.java
            + Clock.java
            + ClockSync.java
            + Constants.java
            + IBufferInput.java
            + IBufferOutput.java
            + ISystemSyncListener.java
            + RuntimeConfig.java
            + SystemSync.java
        - elevator
            - gui
                + DirectionLamp.java
                + DirectionLampPanel.java
                + Door.java
                + DoorState.java
                + ElevatorFrame.java
                + ElevatorPanel.java
            + Direction.java
            + ElevatorCommand.java
            + ElevatorCommandSender.java
            + ElevatorDoor.java
            + ElevatorDoorCommand.java
            + ElevatorEvent.java
            + ElevatorEventReceiver.java
            + ElevatorMotor.java
            + ElevatorMoveCommand.java
            + ElevatorState.java
            + ElevatorSubsystem.java
            + Fault.java
            + Main.java
        - floor
            - gui
                + FloorFrame.java
                + FloorLamp.java
                + FloorLampPanel.java
            + FloorReceiver.java
            + FloorSubsystem.java
            + InputData.java
            + InputParser.java
            + Main.java
            + MeasurementReceiver.java
        - scheduler
            - gui
                + ConfigurationFrame.java
            + Main.java
            + ScheduledJob.java
            + Scheduler.java
            + SchedulerElevator.java
            + SchedulerMessage.java
            + SchedulerReceiver.java
            + SchedulerState.java

    * UML Diagrams
            + StateDiagram.mdj
            + StateDiagram-Scheduler.png
            + StateDiagram-ElevatorSubsystem.png
            + UML-Class-Diagram-IT3.png
            + UML-Class-Diagram-IT4.png
            + UML-Class-Diagram-IT4.violet
            + UML-Class-Diagram-IT5.png
            + UML-Class-Diagram-IT5.violet
            + UML-Sequence-Diagram-IT5.png
            + UML-Sequence-Diagram-IT5.violet
            + Permanent Timing Diagram.PNG
            + Transient Timing Diagram.png

    * test
        - common
            + SystemSyncTest.java
        - elevator
            + ElevatorCommandTest.java
            + ElevatorDoorCommandTest.java
            + ElevatorDoorTest.java
            + ElevatorEventTest.java
            + ElevatorMotorTest.java
            + ElevatorMoveCommandTest.java
            + ElevatorSubsystemTest.java
        - floor
            + FloorReceiverTest.java
            + InputDataTest.java
            + InputParserTest.java
        - scheduler
            + BufferTest.java
            + SchedulerTest.java
            + SchedulerTest.java

## Team Responsibilities

Aubin

- Implemented DirectionLamp &DirectionLampPanel
- Implemented FloorLamp & FloorLampPanel
- Implemented MeasurementReceiver
- UML Class Diagram
- UML Sequence Diagram
- Readme & final Report


Chris

- Updated Scheduler to send InputData for measurement.
- Configured Scheduler main to function with RunTimeConfig
- Configured Elevator main to function with RunTimeConfig
- Timing Diagrams
- Readme & final Report

James

- Modify ElevatorSubsystem to set a door event flag on outgoing ElevatorEvents
- Create SchedulerReceiver to receive ElevatorEvents sent from the scheduler application to the floor application
- Modify ElevatorEventReceiver to forward ElevatorEvents with (door flag == true) to SchedulerReceiver
- ConfigurationFrame start button is disabled until handshakes received
- Wait for UIs to build before starting the simulation
- Update statemachine diagram
- Modify Scheduler and ElevatorSubsystem to pass serviceDirection to FloorFrame
- Readme & final Report

Liya

- Implemented RunTimeConfig
- Created Configuration frame
- Integrated Configuration frame with the floor subsystem
- Created Floor frame
- UML Class Diagram
- Readme & final Report

Noah

- Add boolean flag isDoorEvent to ElevatorEvent
- Modify sync() in Clock to return RunTimeConfig
- Split up ElevatorDoor wait timers
- Construct ElevatorFrame GUI
- Readme & final Report

## Detailed Set Up

In this iteration, graphical user interfaces were desgined and implemennted for the subsystems. A configuration frame was implemented to take in values for the number of elevators, number of floors and the location of the input file to read from. To ensure the synchronization of the FloorSubsystem and ElevatorSubsystem, a start button was implemented to only be enabled when both subsystems are ready. 

A floor frame was implemeted to display direction lamps on each floor. Upon receiving a request, the corresponding direction lamp changes color to indicate that a request for an elevator has been made on that floor. An Elevator frame was implemented to display the perspective of each elevator, as it moves from floor to floor. It indicates the state of the elevator as well as the destination of the elevator. It also display doors opening and any occuring faults.

To compute the average time taken for a request to be processed and completed, a running total of the time and number of request is incremented as each request is completed. Once all the requests have been completed, the average is calculated.

</details>
