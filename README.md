# online-ride-hailing
毕业设计-网约车系统  

## API手册
**"OK"， "Success"** => 200  
**"Bad Request"** => 400  
**"Unauthorized"** => 401  
**"Not Found"** => 404  
1. `/api/auth/login` 用户登录
>描述：
>>如果登录成功，则返回token，  
>>否则返回"Unauthorized"结果。

>用法： POST {username， password}  

2. `/api/auth/user` 获取当前用户信息
>描述：
>>如果当前token有效，返回该用户信息，  
>>否则返回"Unauthorized"结果。  

>用法：GET 携带'Authorization'请求头  
3. `/api/auth/refresh` 刷新用户token
>用法：
>>如果当前token有效且未过期，返回新的token，  
>>否则返回"Bad Request"结果。  

>用法： GET 携带'Authorization'请求头  

4. `/api/auth/verify` 验证用户名是否存在
>描述：
>>如果用户名存在，返回"OK"结果，  
>>否则返回"Not Found"结果。  

>用法：GET ?username=xxx  

5. `/api/auth/registerPassenger` 注册乘客
>描述：
>>如果注册乘客成功，返回用户信息，  
>>否则返回"Bad Request"结果。  

>用法：POST {username，password}  

6. `/api/user` 返回所有用户信息
>描述：  
>>返回所有用户信息
>>如果没有为page和row显式赋值，默认page（页码）为1，rows（每页记录数）为10。  

>用法：GET [?page,rows]  

7. `/api/user/{id}` 获取用户id为{id}的用户信息
>描述：
>>返回用户id为{id}的用户信息，
>>如果没有找到该用户，则返回"Not Found"结果。  

>用法：GET /api/user/{id}  

8. `/api/user/{id}` 修改用户信息
>描述：
>>修改用户id为{id}的用户信息，
>>如果存在图片但上传图片失败，返回"Bad Request"结果。  

>用法：POST 'enctype'='multipart/form-data'的表单
>>key|value
>>------ | ------ 
>>nickname|
>>gender|
>>age|
>>userImage|图片格式的文件

9. `/images/user/{id}.jpg` 显示用户头像的静态资源
>描述：
>>用户头像，直接指定即可。  

10. `/api/trip/search/findPublishedTripByCondition` 根据条件查询已发布的行程（用于设置听单内容后的筛选显示）
>描述：
>>根据设置条件返回未过期的（出发时间大于当前时间）已发布行程。  
>>如果没有为page和row显式赋值，默认page（页码）为1，rows（每页记录数）为10。  

>用法：GET [?page,row,tripType,startDate,endDate]
>>如tripType,startDate,endDate没有指定，则默认没有限制

>>参数|可选值
>>------ | ------
>>tripType|REAL_TIME（实时），RESERVED（预约）

11. `/api/trip/{id}` 返回行程id为{id}的行程信息
>描述：
>>返回行程id为{id}的行程信息，
>>如果没有找到，返回"Not Found"结果。    

>用法：GET /api/trip/{id}  

12. `/hailingService/car/uploadCarLocation` 车主上传车辆位置并广播给所有乘客
>描述：  
>>乘客通过订阅此通道来接收广播  
>>在车主开始听单后，定时将车辆位置推送到此通道。  

>用法：
>>1. 车主：`stompClient.send("/api/hailingService/car/uploadCarLocation"， {}， JSON.stringify({carId， lng， lat}))`
>>2. 乘客：`stompClient.subscribe("/topic/hailingService/car/uploadCarLocation"， function(carLocation))`

12. `/hailingService/car/uploadCarLocation/{passengerUsername}` 车主上传车辆位置并通知用户名为{passengerUsername}的乘客
>描述：
>>在车主受理订单后，定时将车辆位置推送到此通道，乘客通过订阅此通道监控车辆行进轨迹。  

>用法：
>>1. 车主：`stompClient.send("/api/queue/hailingService/car/uploadCarLocation/{passengerUsername}"， {}， JSON.stringify({carId， lng， lat}))`
>>2. 乘客：`stompClient.subscribe('/user/queue/hailingService/car/uploadCarLocation'，function(carLocation))`  

13. `/api/hailingService/fare/predictFare` 预估车费
>描述：
>>给定预估里程数和预估时长数，返回预估车费结果，  
>>如果lengthOfMileage和lengthOfTime任意一个为空或任意一个不大于0，返回"Bad Request"结果。  

>用法：GET ?lengthOfMileage=m&lengthOfTime=t  

14. `api/hailingService/trip/publishTrip` 乘客发布行程
>描述：  
>>乘客发布他的行程，正在听单的车主将会接收到此行程信息。

>用法：
>>1. 乘客：POST {departure， destination， departureTime， tripType， passengerId}
>>2. 车主：`stompClient.subscribe("/topic/hailingService/trip/publishTrip"， function(trip))`

>>参数|可选值
>>------ | ------
>>tripType|REAL_TIME（实时），RESERVED（预约）

15. `/api/hailingService/tripOrder/acceptTripOrder` 车主受理订单，通知乘客
>描述：
>>车主受理订单，并通知对应的乘客，  
>>如果没有找到对应行程，返回"Not Found"结果，  
>>如果当前行程无法被受理（行程状态错误），返回"Bad Request"结果。 
>>返回行程订单  

>用法： POST {tripId，driverId}

16. `/hailingService/tripOrder/acceptance-notification` 车主受理订单后触发受理通知
>描述：  
>>乘客受理订单后，将会通过该通道通知乘客。  

>用法：
>>1. 车主：`stompClient.send("/queue/hailingService/tripOrder/acceptance-notification/{passenggerUusername}"， {}， JSON.stringify({...}))` **（暂时未提供！）**
>>2. 乘客：`stompClient.subscribe("/user/queue/hailingService/tripOrder/acceptance-notification"， function(tripOrder))`

17. `/api/hailingService/tripOrder/pickupPassenger` 车主确认乘客上车
>描述：
>>车主确认乘客上车后，更新行程订单状态和行程状态。    
>>如果行程订单没找到，返回"Not Found"结果，  
>>如果给定的行程是无效的，返回"Bad Request"结果，    
>>如果当前行程无法被处理（行程订单状态错误），返回"Bad Request"结果。  
>>返回行程订单

>用法： POST {tripOrderId，tripId，driverId}  

18. `/api/fareRule/search/findRecentFareRule` 查询当前计费规则
>描述：
>>返回当前计费规则  

>用法：GET  

19. `/api/hailingService/tripOrder/confirmArrival` 车主确认到达
>描述：
>>车主确认乘客到达后，结束计算行程费用，更新行程订单状态和行程状态，并插入车费明细记录。    
>>如果行程订单没找到，返回"Not Found"结果，  
>>如果给定的行程是无效的，返回"Bad Request"结果，    
>>如果当前行程无法被确认完成（行程订单状态错误），返回"Bad Request"结果。  
>>返回行程订单  

>用法： POST {tripOrderId,tripId,driverId,FareRuleId,lengthOfMileage,lengthOfTime}  
