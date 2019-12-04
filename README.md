# Icarus
## City-Scale Simulation of Traffic Events

This project models an agents trips throughout a network, over a period of time, in an attempt to create a realistic simulation of traffic dynamics.

Modeled modes include (but are not limited to) car, rideshare, taxis, transit (bus, light-rail, train), biking, walking, and freight.

Meso-scale resolution indicates that each "action" on the network is calculated and recorded for every agent. Every action is guaranteed to have a time, exact location, action type, and agent. Each of these network actions is stored in the MySQL database to facilite large-scale processing. 

This allows for a high-resolution understanding of the network, which would be lost in a macro-scale simulation, without the extreme computational load of micro-scale simulations.

## Purpose and Use-case for Icarus
For the sake of ChesterLab, this data was used for the following tasks:
* Modeling the effect on agent travel of various road closures/slow-downs
* Modeling the temperature exposure of each agent based on travel time and mode, as well as temperature values from sources such as DAYMET
* Characterizing network load and possible modifications to alleviete real-life slowdowns

*Processing is done by [DataProcessing](https://github.com/ChesterIcarus/DataProcessing) which is a suite of scripts written in Python 3.7*

## Dependencies and Inputs

The project is built using Java 8 with Maven 3.6.3 and MySQL 5.7

For the sake of the ChesterLab's purposes the inputs were as follows:
* **ABM** - Activity based model of Maricopa as provided by regional government organizations (eg. MAG, SCAG)
* **Network** - Road network data of Arizona as provided by OpenStreetMaps
* **Residences/Commerces** - Parcel data (residential and commercial respectively) as provided by the regional government organizations

## Running Icarus
The project can be ran usign the following command:
```
java -Djava.awt.headless=true -Xms1g -Xmx12g \
    -cp <Icarus Jar Location> \
    InMemMatsim.Model.Model \
    <Icarus Configuration File Location>
```
While the flags ```-Xms1g``` & ```-Xmx12g``` are not required, they are suitable for most small to mid scale simulations (They denote a minimum RAM allocation of 1gb and maximum RAM allocation of 12gb)
Simulations as a whole can be quite resource intensive due to the nature of MATsim and the amount of data that is processed in each iteration, and as such processing in AWS was the standard choice for ChesterLab.


## MATsim Modifications to allow for more effecient co-simulation.
The following details the set of modifications made by @amichne, which allow the full running of multiple "scenarios", utilizing at least a single shared resource, with massively reduced overhead for both computation and storage of results.

This is achieved by processing the intermediary data at run-time, and retaining only what has been defined as critical by the configuration file. 

In addition, these changes optimize the usage of shared resources for concurrent simulations, such as the plans for each agents travel on the network.

This is done by force-loading the plans outside of the MATsim "Scenario" class, and inserting them into the respective fields, rather than successive calls to the "load<Resource>()" method. Note that this does constitute a divergence from the MATsim codebase, as we must modify otherwise *final* classes to acheieve this behaviour

Ultimately, we allow users to load resources a single time, and reuse them for multiple simulation scenarios, without forcing a recreation of the objects used in simulations. 

The result is saving on average, between **20%-30% of computation time**, as well as a linear reduction in storage space, scaling with number of simulations.

*Any questions or concerns regarding this process can be directed to austinmichne@gmail.com*

**Below is an Example Configuration for a run.**

*Note that all MATsim options are parsed and available for use, though the MATsim syntax is not used.*

```
<?xml version="1.0" encoding="UTF-8"?>
<model>
    <plans path="Your Path Here"/>
    <network path="Your Path Here"/>
    <config path="Your Path Here"/>
    <events path="Your Path Here"/>
    <global>
        <timeVariant value="true"/>
        <threads>
            <simulation value="128"/>
            <planning value="56"/>
        </threads>
    </global>
    <controler>
        <overwriteFileSetting value="deleteDirectoryIfExists"/>
    </controler>
    <planParameters>
        <activities>
            <activity>
                <type value="home"/>
                <closingTime value="15.0"/>
                <earliestEndTime value="4000.0"/>
            </activity>
            <activity>
                <type value="work"/>
            </activity>
            <activity>
                <type value="university"/>
                <scoringThisActivityAtAll value="false"/>
            </activity>
        </activities>
        <modes>
            <mode>
                <type value="car"/>
                <constant value="1.2"/>
            </mode>
            <mode>
                <type value="sov"/>
            </mode>
        </modes>
    </planParameters>
</model>
```

*Note: All sensitive data has been removed, much of which is required to run the project. Contact austinmichne@gmail.com if running the project is something you'd like to pursue.*

