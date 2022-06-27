# CS5011 AI Practice

This repository demonstrates work completed as part of the **CS5011 AI Practice** module.
Specifically, four different AI practice domains were considered: search, logic, learning, and uncertainty.

# Search:

Several pathfinding/search algorithms have been implemented to find solutions for a _Coastguard Rescue Simulation_. In this simulation, a robot is required to search for a path from a person who requires rescuing to a position of safety amongst the [Giant’s Causeway](https://en.wikipedia.org/wiki/Giant%27s_Causeway).

Uninformed (depth-first and breadth-first) and informed (best-first and A*) search algorithms have been implemented to find solutions to various configurations of this problem. Additionally, the bidirectional search (BDS) algorithm has been implemented for comparison. An alternate/extension heuristic has also been implemented for heuristic comparisons with the informed search algorithms. In all cases, experiments have been performed to evaluate and compare these search algorithms.

# Logic:

Logical (procedural and declarative) techniques have been used to implement a solver and hint system for the [Easy As ABC](http://puzzlepicnic.com/genre id=8) puzzle using constraint programming. A rudimentary hint system for this puzzle has also been developed, which follows a similar chain of logic a person would usually follow when completing the puzzle. A brief evaluation comparing the procedural and declarative techniques is performed, as well as a critical discussion of the developed hint system.

# Learning:

Artificial neural networks (NNs) have been constructed and used to solve a water-pump classification problem with real-world data. The problem and data presents various challenges, which have been addressed individually through incremental tasks.

The real-world dataset is about the operating conditions of water pumps collected at several waterpoints across Tanzania. The data was provided by Taarifa and the Tanzanian Ministry of Water, and is downloadable from the DrivenData’s competition “[Pump it Up: Data Mining the Water Table](https://www.drivendata.org/competitions/7/pump-it-up-data-mining-the-water-table)”. Each example in the competition dataset represents various pieces of information about a water pump (input features) and the pump’s status (output label). There are three possible statuses: _functional_, _functional needs repair_, and _non functional_. The task is to predict the status of a pump. This dataset comes with a number of challenging characteristics of real-world data. The input features are of mixed types (numerical, categorical, datetime, etc.). Some parts of the data is missing. The dataset is also imbalanced (i.e., the amounts of data among output classes are uneven).

# Uncertainty:


