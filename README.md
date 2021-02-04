# Elevator-System-Simulation
This project is a design and implementation of an elevator control system and simulator. The system consist of an elevator controller (the Scheduler), a simulator for the elevator cars (which includes, the lights, buttons, doors and motors) and a simulator for the floors (which includes, buttons, lights and last, but not least, people who are too lazy to take the stairs). The elevator controller is multi-threaded since it will handle more than one car at a time. The simulation is configurable in terms of the number of floors, the number number of elevators, the time it takes to open and close the doors, and the time it takes to move between floors. The simulation will run on multiple computers as part of the project involves running the controller on its own machine with the simulator(s) running on a separate computer. The code is written in Java, using the Eclipse IDE.


# Iteration 1

## Folders/Files included:

    * resources
        + input.txt
    * src
        - elevator
            + Direction.java
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
        - UML Diagrams - Iteration 1
            + UML-Class-Diagram-IT1 .png
            + UML-Class-Diagram-IT1 .violet
            + UML-Sequence-Diagram-IT1 .png
            + UML-Sequence-Diagram-IT1 .violet
    
    * test
        - elevator
            + ElevatorSubsystemTest.java
        - floor
            + FloorSubsystemTest.java
            + InputDataTest.java
            + InputParserTest.java
        - scheduler
            + SchedulerTest.java
            + BufferTest.java

## Responsibilities:

    * Aubin
        + UML class diagram
        + UML sequence diagram
        + Updating README file
    * Chris
        + Implementing ElevatorSubsystem class
        + Writing ElevatorSubsystem test
        + Implementing Elevator class
    * James
        + Implementing InputParser class
        + Writing InputParser test
    * Liya
        + Implementing FloorSubsystem class
        + Writing FloorSubsystem Test
    * Noah
        + Implementing Buffer class
        + Writing Buffer test
        + Implementing Scheduler class
        + Writing Scheduler test

## Detailed set up:

    The three thread classes created are FloorSubsystem, Scheduler and ElevatorSystem. A template Buffer object class was created from which multiple buffer objects are created. The FloorSubsystem class reads input data from the provided file (input.txt) and puts each line of data into a buffer object shared with the Scheduler class. Following that, the Scheduler class gets the data from the shared buffer object and processes it. The Scheduler class will contain a buffer object for each elevatorSubsystem that exists. These buffer objects are shared with the ElevatorSubsystem. Once the data is processed, the scheduler will put elevator schedules into the corresponding buffer objects and notify the corresponding ElevatorSubsystem. Since each elevator will have its own subsystem, they will be instructed to move by the corresponding ElevatorSubsystem. The elevator will then be responsible for indicating which floors it leaves and which floors in arrives on.