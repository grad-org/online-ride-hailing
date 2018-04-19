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

12. `/api/hailingService/car/uploadCarLocation`
>description:  
>>The diver upload his/her car location internally,  
>>then the passenger who is idle will draw nearby cars' driving route by the car location.  

>usage:
>>1. `stompClient.send("/api/hailingService/car/uploadCarLocation", {}, JSON.stringify({carId, lng, lat}))`
>>2. `stompClient.subscribe("/topic/hailingService/car/uploadCarLocation", function(data))`

13. `/api/hailingService/trip/publishTrip`
>description:  
>>The passenger publish the trip through WebSocket,  
>>and the driver who is listening the trip order will subscribe it.  

>usage:
>>1. `stompClient.send("/api/hailingService/trip/publishTrip", {}, JSON.stringify({departure, destination, departureTime, tripType, passengerId}))`
>>2. `stompClient.subscribe("/topic/hailingService/trip/publishTrip", function(data))`

14. `/api/hailingService/tripOrder/acceptTripOrder`
>usage: POST {tripId,driverId}

>description:
>>The driver with driverId accept the trip order and notify the passenger,  
>>return "Not Found" result if the trip is not found,  
>>return "Bad Request" result if the trip could not be accepted.  

15. `queue/hailingService/tripOrder/acceptance-notification`
>description:  
>>When the driver accept the trip order,  
>>the server will provide a queue to make the passenger receive a acceptance notification(1 to 1 communication).  

>usage:
>>1. `stompClient.send("/queue/hailingService/tripOrder/acceptance-notification/{passengger's username}", {}, JSON.stringify({...}))` **(Not available temporarily!)**
>>2. `stompClient.subscribe("/user/queue/hailingService/tripOrder/acceptance-notification", function(data))`

