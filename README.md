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

>usage:
>>1. GET return users with page is 1 and rows is 10 by default.
>>2. GET?page=m&rows=n  
7. `/api/user/{id}`
>Return the user with id, if not found return "Not Found" result.  
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
10. `/api/hailingService/passenger/publishTrip`
>description:  
>>The passenger publish the trip through WebSocket,  
>>and the driver who is listening the trip order will subscribe it.  

>usage:
>>1. `stompClient.send("/api/hailingService/passenger/publishTrip", {}, JSON.stringify({departure,destination,departureTime,tripType,passengerId}))`
>>2. `stompClient.subscribe("/topic/hailingService/passenger/publishTrip", function(data))`

>>key|hint
>>------ | ------
>>departure|出发地
>>destination|目的地
>>departureTime|出发时间(yyyy-MM-dd HH:mm:ss)
>>tripType|REAL_TIME(实时),RESERVED(预约)
>>passengerId|
11. `/api/hailingService/acceptTripOrder`
>usage: POST {tripId,driverId}

>description:
>>The driver with driverId accept the trip order,  
>>return "Not Found" result if the trip is not found,  
>>return "Bad Request" result if the trip could not be accepted.  

12. `/api/trip/published`
>description:
>> Return published trips which its departure time is greater than now.  

>usage:
>>1. GET return published trips with page is 1 and rows is 10 by default.
>>2. GET?page=m&rows=n
