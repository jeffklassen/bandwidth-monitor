
# Bandwidth Monitor

## Introduction

Bandwidth Monitor is an application that allows administrators to monitor traffic trends in real time on their network.
The application consists of two parts:

1. **Traffic Inspector** - A NodeJS application that watches a linux network interface for packets, interprets packet metadata, and inserts the information into Elasticsearch in near real time.
2. **Web Application** - This is where the administrators go to view the traffic. The application is a Java war built on JAXRS with an AngularJS front end. The web application has two main views
  1. The historical data viewer -
  
     ![Historical](http://i.imgur.com/HkeifBY.png "Historical")
     With Summaries (click any of the bars on the summaries page -
     ![summaries](http://i.imgur.com/bLcOG4k.png "Historical")
  2. The realtime view -
   
    ![realtime](http://i.imgur.com/4aSSu1Y.png "realtime")
     Also with summaries. Accessible by selecting any two spots on the scrolling graph.

