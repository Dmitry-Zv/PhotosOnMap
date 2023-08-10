# PhotosOnMap
PhotosOnMap is an application that consists of 5 screens. The first screen is the Regisration and Logging screens.
The second screen is the Photo display screen. With a short press on an image element, we go to the Details screen.
When you long press on an image element, a dialog appears to confirm the deletion of this element. 
The third screen is the Google maps screen which displays image coordinate markers and tracks your location. 
The fourth screen is the Camera screen, which takes a photo and then sends it to the server, after which this photo becomes available on the Photo screen.
The fifth screen is  the Detailed screen which contains the image of photo, the time when the photo was taken, comments of the photo, as well as a field for adding a comment.
To delete a comment, swipe the comment element to the left.
Screens:
![login](https://github.com/Dmitry-Zv/PhotosOnMap/assets/70663257/69d5be68-9e61-4f65-83b7-175daa8fe6d9)
![regiser](https://github.com/Dmitry-Zv/PhotosOnMap/assets/70663257/1471691b-836e-4dca-a722-8cfcc6b7ec02)
![photos](https://github.com/Dmitry-Zv/PhotosOnMap/assets/70663257/cd8b1531-a0f5-410a-9660-fb133f0a8efb)
![delete_photo](https://github.com/Dmitry-Zv/PhotosOnMap/assets/70663257/95661420-ce2d-4de1-af07-e09e1902a36d)
![navigation_drawer](https://github.com/Dmitry-Zv/PhotosOnMap/assets/70663257/ecc9032d-3461-4bf5-a2a3-9f68d200de45)
![google_maps](https://github.com/Dmitry-Zv/PhotosOnMap/assets/70663257/f53f2b74-84ca-4f8f-9615-bb78df961407)
![google_maps2](https://github.com/Dmitry-Zv/PhotosOnMap/assets/70663257/e8ad231b-c077-413b-86c4-cba5dd5b61c0)
![camera](https://github.com/Dmitry-Zv/PhotosOnMap/assets/70663257/aaedb9d2-f1a8-4473-9d8f-800795bcc203)
![detailed](https://github.com/Dmitry-Zv/PhotosOnMap/assets/70663257/ac96c51f-792b-4f57-aaf6-2e40d19f41a7)

The technology stack includes the Android Jetpack libraries (ViewModel, Room, Paging3, Navigation). The Retrofit library is used to work with the network. 
The Paging3 library was used to implement lazy data loading. ORM Room was used for data caching, as it already supports PagingSource out of the box. 
The CameraX library was used to work with the camera.
To work with the map, the libraries gsm:play-service-location (for location tracking), gsm:play-service-maps (for working with the map itself) were used.
Hilt was used as DI.







