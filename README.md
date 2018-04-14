# online-ride-hailing
the project for our graduation design - "online-ride-hailing".

## api-reference
**"OK", "Success"** => 200  
**"Bad Request"** => 400  
**"Unauthorized"** => 401  
1. `/api/auth/login`:  
>usage: POST {username, password}
>description:  
>>if login success, return the token,  
>>else return "Unauthorized" result.  
2. `/api/auth/user`:  
>usage: GET with token header  
>description:  
>>return the user if token is valid,  
>>else return "Unauthorized" result.        
3. `/api/auth/refresh`:  
>usage: GET with token header  
>description:  
>>if refresh success, return the new token,  
>>else return "Bad Request" result.  
4. `/api/auth/verify`:  
>usage: GET ?username=xxx  
>description:  
>>if username is not existed, return "OK" result,  
>>else return "Bad Request" result.  
5. `/api/auth/register`:  
>usage: POST {username,password}  
>description:  
>>if register success, return the complete user JSON,  
>>else return "Bad Request" result.  
6. `/api/user`:  
>return all users.  
7. `/api/user/{id}`:  
>return the user with id.  
8. `/api/user/update/{id}`:  
>usage: POST "multipart/form-data" form ==> means the form's enctype is 'multipart/form-data'
>>key|value
>>------ | ------ 
>>nickname|
>>gender|
>>age|
>>userImage|picture file
>description:  
>>if update success, return the complete user JSON,  
>>else return "Bad Request" result.  



9. `/images/user/{id}.jpg`:  
>description:  
>>The static content url about user image,  
>>you can place this url to show the user image directly.  
