Spacecraft Reference Example Application
=======================

Matt Luckcuck <ml881@york.ac.uk> 2014
9th of February 2015
-------------------------------------

DISCLAIMER
----------

The memory parameters in this reference example are defaults and therefore it does not run correctly. 

Becasue there is no Level 2 implementation, this application *will not* run. It is provided *only* as a reference of the structure of an application using the multiple mode pattern.

The program was compiled using the [Icecap ]{http://icelab.dk} implementation of SCJ provided in this directory (as the provided version is no longer available from the Icecap website).

Description
-----------

This example application shows the multiple mode pattern from the ["Safety-critical Java level 2: motivations, example applications and issues"]{http://dl.acm.org/citation.cfm?id=2512991} paper. The pattern describes how to construct applications which have distinct modes of behaviour with mode-specific tasks. This involves mode changer component controlling the execution of several modes, where each mode has a set of mode-specific tasks.

In SCJ each mode is a `Mission` and changing between them is accomplished by a `MissionSequencer`. Each task is represented by an appropriate managed schedulable object. An SCJ Level 2 implementation has the benefit, over implementing this pattern at Level 1, of allowing persistent schedulable objects alongside the mode-specific schedulables.

The Spacecraft is a brief example of this pattern that has three modes: Launch, Cruise, and Land; and two persistent schedulables for: monitoring the craft's internal environment, and handling the craft's controls. The functionality of this program is not fully complete. Its intention is to illustrate the possible structure of programs at Level 2.

