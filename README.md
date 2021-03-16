#Quantexa Coding Exercise
#Zak Bibi

The following repo is my solution to the technical task provided to me by Quantexa. The task was to evaluate a file of transactions and to perform data aggregation.  

#Design Overview

##Simple Design Diagram

![DesignDiagram](https://cdn.discordapp.com/attachments/777309166343290920/821405852015132712/Screenshot_from_2021-03-16_15-32-02.png)

There are separate classes for each report generator. This solution provides a CSV writer, but it could be extended with a JSON writer. Having separate classes for reader report generaters and writers makes this a flexible system and is easier to test. For example: you could provide a different report generator in the future for different reports.
##Design Commentary

When tackling this coding exercise I came across several challenges. Naturally, to complete the exercise, addressing these challenges was a must. I will outline below what those problems were, and how I solved them.

###Missing Data
It became apparent that there was missing data for various transaction types while calculating the averages. I noticed that the report data was missing fields, and so I addressed this by creating a filler. This meant that I could account for missing transactions, and fill the transaction category fields with 0.0 where needed. The trick was to apply the filler data after calculating the averages for the transaction categories, so that the averages would be correct. 

###Sorting
As with all reports, it is important to display the data in the correct fashion. Using Maps made sorting through the data much easier: key-value pairs enable quick access to what's needed, and using `.groupBy` on a `Transaction` field enabled easy navigation. However, `Map` in Scala is not sorted and has no ordering, so `ListMap` was needed. You can sort by key in `ListMap`s with ease.

###Floating Points
I decided not to round floating point values as this was not specified.

###Drawbacks
There is coupling. The report generators can only take in a `List[Transaction]`. If the `Transaction` class changes, all the report generators potentially have to be rewritten. I could not think of an easy way to decouple them.


##How to Build and Run

Each report has its own App, and each can be run on the commandline. This means there are separate commands for each. 

###To Build
`sbt compile` 
`sbt assembly`

###To Run Averages Per Account
`sbt "runMain com.quantexa.transaction.report.averagesperaccount.AverageValueOfTransactionsPerAccountApp [path to transactions.txt] [destination/path to filename]`

###To Run Rolling Windows
`sbt "runMain com.quantexa.transaction.report.rollingwindows.RollingWindowApp [path to transactions.txt] [destination/path to filename]"`

###To Run Totals Per Day
`sbt runMain com.quantexa.transaction.report.totalsperday.TotalTransactionsPerDayApp [path to transactions.txt] [destination/path to filename]"`
