# online-ride-hailing
the project for our graduation design - "online-ride-hailing".

## api-reference
**"OK", "Success"** => 200  
**"Bad Request"** => 400  
**"Unauthorized"** => 401  
1. `/api/auth/login`:  
>usage: POST {username, password}
>description:  
>>If login success, return the token,  
>>else return "Unauthorized" result.  
2. `/api/auth/user`:  
>usage: GET with token header  
>description:  
>>Return the user if token is valid,  
>>else return "Unauthorized" result.        
3. `/api/auth/refresh`:  
>usage: GET with token header  
>description:  
>>If refresh success, return the new token,  
>>else return "Bad Request" result.  
4. `/api/auth/verify`:  
>usage: GET ?username=xxx  
>description:  
>>If username is not existed, return "OK" result,  
>>else return "Bad Request" result.  
5. `/api/auth/registerPassenger`:  
>usage: POST {username,password}  
>description:  
>>If register passenger success, return the complete user JSON,  
>>else return "Bad Request" result.  
6. `/api/user`:  
>Return all users.  
7. `/api/user/{id}`:  
>Return the user with id, if not found return 404 response.  
8. `/api/user/{id}`:  
>usage: POST "multipart/form-data" form ==> means the form's enctype is 'multipart/form-data'
>>key|value
>>------ | ------ 
>>nickname|
>>gender|
>>age|
>>userImage|picture file
>description:  
>>If update success, return the complete user JSON,  
>>else return "Bad Request" result.  
9. `/images/user/{id}.jpg`:  
>description:  
>>The static content url about user image,  
>>you can place this url to show the user image directly.  
8. `/api/hailingService/passenger/{id}/trip`:  
>usage: POST {departure,destination,departureTime}  
>>key|hint
>>------ | ------ 
>>departure|出发地
>>destination|目的地
>>departureTime|出发时间(yyyy-MM-dd HH:mm:ss)
>description:  
>>The passenger with id publish his/her trip,  
>>and return the trip JSON.  
