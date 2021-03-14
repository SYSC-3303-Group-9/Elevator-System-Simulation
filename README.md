# Contents

- Elevator-System-Simulation
- Set Up and Test Instructions
- Authors
- Iteration 1
  - Folders/Files included
  - Team Responsibilities
  - Detailed Set Up
- Iteration 2
  - Folders/Files included
  - Team Responsibilities
  - Detailed Set Up

# Elevator-System-Simulation

This project is a design and implementation of an elevator control system and simulator. The system consist of an elevator controller (the Scheduler), a simulator for the elevator cars (which includes, the lights, buttons, doors and motors) and a simulator for the floors (which includes, buttons, lights and last, but not least, people who are too lazy to take the stairs). The elevator controller is multi-threaded since it will handle more than one car at a time. The simulation is configurable in terms of the number of floors, the number number of elevators, the time it takes to open and close the doors, and the time it takes to move between floors. The simulation will run on multiple computers as part of the project involves running the controller on its own machine with the simulator(s) running on a separate computer. The code is written in Java, using the Eclipse IDE.

# Set Up and Test Instructions

1.  Download zip file and unzip
2.  Open unzipped file through the Eclipse file system
3.  Locate the 3 `Main.java` files: `elevator/Main.java`, `scheduler/Main.java`, and `floor/Main.java`. Each `Main.java` represents one part of the Elevator-System, they are meant to be running simultaneously.
4.  Launch each `Main.java` by right-clicking -> Run as Java application. **They must be launched in the order given in step 3**.

Tests are written and run with JUnit 5. The 'test' folder contains a test package for each subsystem

1.  Upon opening the test folder (In Eclipse), select subsystem package
2.  Open .java file and run as 1 JUnit test

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
