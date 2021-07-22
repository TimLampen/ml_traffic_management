# Traffic Management in Vancouver, BC, Canada <WIP>

## Install
1. `git clone` the repo
2. Set your `AZURE_SASTOKEN` and `AZURE_CONTAINER` environmental variables for the scraper service.
3. Can start the scraper service by building maven into executable then running `java -jar <jar file name>.jar` or python labeller using Jupyter.
  
## Description
With the boom of the canadian real estate industry, more and more houses are being build everyday. As time goes on, population density distributions change, however our traffic systems do not - causing delays with routes which used to be free flowing.
  
###  Heres an example:
  Lets say in our area there are four cities: Northcity, Eastcity, Southcity, and Westcity, and there is only one intersection in the town. When the intersection was built, the majority of the traffic flowed Northcity -> Southcity and so the green light in that direction was set to be on twice as long as the other direction (to reduce congestion). However, fastforward 20 years and with new housing developments, workspaces, etc, the majority of the traffic flows from Westcity -> Eastcity. However, because North-South is twice as long, this route is extremely congested.
  
  With one intersection, this problem is easy to find and solve, but for an entire city it can be extremely difficult. Therefore, this project sets out to combine video processing, AI, and software magic to help identify congested intersections to improve traffic flow in cities.
  
## Functionality
At the time of writing, the main functionality is as follows:
1. Scrape traffic intersection images for an area (Vancouver)
2. Label key objects in the image using ML
3. Count number of cars in the image
4. Complete data analysis to determine traffic trends in different intersections.
  
In the future, the following additional features are planned:
1. Display the gathered information and analytics on a dashboard that is accessible online.
2. Determine problem intersections (ones where traffic is higher than it should be)
3. Recommend different intersection timings for problem areas.

 ## Framework
The general framework for gathering the data looks like this:
![framework](https://i.imgur.com/2cepn2Z.png)
  
### scrapper_runner
The city of vancouver has a nice website where a user can go and look at different live intersection feeds (exciting!) of which look similar to the ones below:
  
![north](https://trafficcams.vancouver.ca/cameraimages/GranvilleNorth_Dunsmuir.jpg) | ![east](https://trafficcams.vancouver.ca/cameraimages/DunsmuirEast_Granville.jpg) |
--- | --- |
![south](https://trafficcams.vancouver.ca/cameraimages/GranvilleSouth_Dunsmuir.jpg) | ![west](https://trafficcams.vancouver.ca/cameraimages/DunsmuirWest_Granville.jpg)
  
This service downloads the images at several intersections every 10min (the feeds refresh every ~10min) so a historical archive of images can slowly be built. These images are then sent to an Azure BLOB Container, indexed by the intersection the image resides on.
  
  
### ml_traffic_proc
This service is responsible in running the ml algorithm and counting the number of cars in the image. Specifically, this service uses a TensorFlow model that was based off of `Faster R-CNN ResNet50 V1 640x640`. Here is an example of the ML in action:
  
<img src="https://i.imgur.com/4PKGpd9.jpg" width="400"><img src="https://i.imgur.com/X11sXHE.png" width="400">
  
Its hard to see but there are are 9 cars in the image and the AI identified 7 of them (including the truck), which is not bad for a proof of concept. In an ideal world, there would be a 360-degree camera set up in the middle of each intersection which was constantly livestreaming the data.
