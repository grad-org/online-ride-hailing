# online-ride-hailing
the project for our graduation design - "online-ride-hailing".  

## api-reference
**"OK", "Success"** => 200  
**"Bad Request"** => 400  
**"Unauthorized"** => 401  
**"Not Found"** => 404  
1. `/api/auth/login`
>description:
>>If login success, return the token,  
>>else return "Unauthorized" result.

>usage: POST {username, password}  

2. `/api/auth/user`
>description:
>>Return the user if token is valid,  
>>else return "Unauthorized" result.  

>usage: GET with token header  
3. `/api/auth/refresh`
>description:
>>If refresh success, return the new token,  
>>else return "Bad Request" result.  

>usage: GET with token header  

4. `/api/auth/verify`
>description:
>>If username is not existed, return "OK" result,  
>>else return "Not Found" result.  

>usage: GET ?username=xxx  

5. `/api/auth/registerPassenger`
>description:
>>If register passenger success, return the complete user JSON,  
>>else return "Bad Request" result.  

>usage: POST {username,password}  

6. `/api/user`
>description:  
>> Return the users.  
>> By default, page is 1, row is 10.  

>usage:
>> GET [?page,rows]  

7. `/api/user/{id}`
>description:
>>Return the user with id, if not found return "Not Found" result.  

>usage: GET /api/user/{id}  

8. `/api/user/{id}`
>description:
>>If update success, return the complete user JSON,  
>>else return "Bad Request" result.  

>usage: POST "multipart/form-data" form ==> means the form's enctype is 'multipart/form-data'
>>key|value
>>------ | ------ 
>>nickname|
>>gender|
>>age|
>>userImage|the file which ends with .jpg,.png,...

9. `/images/user/{id}.jpg`
>description:
>>The static content url about user image,  
>>you can place this url to show the user image directly.  

10. `/api/trip/search/findPublishedTripByCondition`
>description:
>> Return unexpired and published trips by condition.  
>> By default, page is 1, row is 10.  

>usage:
>> GET [?page,row,tripType,startDate,endDate]

11. `/api/trip/{id}`
>description:
>>Return the trip with id, return "Not Found" result if not found.    

>usage: GET /api/trip/{id}  

12. `/hailingService/car/uploadCarLocation`
>description:  
>>After the diver began to listen the order, his/her car location will be broadcast to the passengers.    

>usage:
>>1. `stompClient.send("/api/hailingService/car/uploadCarLocation", {}, JSON.stringify({carId, lng, lat}))`
>>2. `stompClient.subscribe("/topic/hailingService/car/uploadCarLocation", function(carLocation))`

12. `/hailingService/car/uploadCarLocation/{passengerUsername}`
>description:
>>After the driver accept the order, his/her car location will send to the specific passenger.  

>usage:
>>1. `stompClient.send("/api/queue/hailingService/car/uploadCarLocation/{passenger's username}", {}, JSON.stringify({carId, lng, lat}))`
>>2. `stompClient.subscribe('/user/queue/hailingService/car/uploadCarLocation',function(carLocation))`  

13. `/hailingService/trip/publishTrip`
>description:  
>>The passenger publish the trip through WebSocket,  
>>and the driver who is listening the trip order will subscribe it.  

>usage:
>>1. `stompClient.send("/api/hailingService/trip/publishTrip", {}, JSON.stringify({departure, destination, departureTime, tripType, passengerId}))`
>>2. `stompClient.subscribe("/topic/hailingService/trip/publishTrip", function(trip))`

14. `/api/hailingService/tripOrder/acceptTripOrder`
>usage: POST {tripId,driverId}

>description:
>>The driver with driverId accept the trip order and notify the passenger,  
>>return "Not Found" result if the trip is not found,  
>>return "Bad Request" result if the trip could not be accepted.  

15. `/hailingService/tripOrder/acceptance-notification`
>description:  
>>When the driver accept the trip order,  
>>the server will provide a queue to make the passenger receive a acceptance notification(1 to 1 communication).  

>usage:
>>1. `stompClient.send("/queue/hailingService/tripOrder/acceptance-notification/{passengger's username}", {}, JSON.stringify({...}))` **(Not available temporarily!)**
>>2. `stompClient.subscribe("/user/queue/hailingService/tripOrder/acceptance-notification", function(tripOrder))`

16. `/api/hailingService/tripOrder/pickupPassenger`
>usage: POST {tripOrderId,tripId,driverId}

>description:
>>The driver pick up passenger, update the trip order and trip status.    
>>return "Not Found" result if the trip order is not found,  
>>return "Bad Request" result if the giving trip is not available.  
>>return "Bad Request" result if the trip order could not be processed.    

