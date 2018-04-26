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

>用法：POST {username,password}  

2. `/api/auth/user` 获取当前用户信息
>描述：
>>如果当前token有效，返回该用户信息，  
>>否则返回"Unauthorized"结果。  

>用法：GET 携带'Authorization'请求头  
3. `/api/auth/refresh` 刷新用户token
>用法：
>>如果当前token有效且未过期，返回新的token，  
>>否则返回"Bad Request"结果。  

>用法：GET 携带'Authorization'请求头  

4. `/api/auth/verify` 验证用户名是否存在
>描述：
>>如果用户名存在，返回"OK"结果，  
>>否则返回"Not Found"结果。  

>用法：GET ?username=xxx  

5. `/api/auth/register` 注册
>描述：
>>如果注册成功，返回用户信息，  
>>否则返回"Bad Request"结果。  

>用法：POST {username,password}  

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

9. `/images/user/{userId}.jpg` 显示用户头像的静态资源
>描述：
>>用户头像，直接指定即可。  

10. `/api/trip/search/findPublishedTripByCondition` 根据条件查询已发布的行程（用于设置听单内容后的筛选显示）
>描述：
>>根据设置条件返回未过期的（出发时间大于当前时间）已发布行程。  
>>如果没有为page和row显式赋值，默认page（页码）为1，rows（每页记录数）为10。  

>用法：GET [?page,row,tripType,startDate,endDate]  
>>如tripType,startDate,endDate没有指定，则默认没有限制  
>>查询已发布的实时行程：GET?tripType=REAL_TIME[,page,row]  
>>查询已发布的预约行程：GET?tripType=RESERVED[,startDate,endDate,page,row]  
>>查询已发布的所有行程：GET?[,startDate,endDate,page,row]  

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
>>1. 车主：`stompClient.send("/api/hailingService/car/uploadCarLocation", {}, JSON.stringify({carId, lng, lat}))`
>>2. 乘客：`stompClient.subscribe("/topic/hailingService/car/uploadCarLocation", function(carLocation))`

12. `/hailingService/car/uploadCarLocation/{passengerUsername}` 车主上传车辆位置并通知用户名为{passengerUsername}的乘客
>描述：
>>在车主受理订单后，定时将车辆位置推送到此通道，乘客通过订阅此通道监控车辆行进轨迹。  

>用法：
>>1. 车主：`stompClient.send("/api/queue/hailingService/car/uploadCarLocation/{passengerUsername}", {}, JSON.stringify({carId, lng, lat}))`
>>2. 乘客：`stompClient.subscribe('/user/queue/hailingService/car/uploadCarLocation', function(carLocation))`  

13. `/api/fare/predictFare` 预估车费
>描述：
>>给定预估里程数和预估时长数，返回预估车费结果，  
>>如果lengthOfMileage和lengthOfTime任意一个为空或任意一个不大于0，返回"Bad Request"结果。  

>用法：GET ?lengthOfMileage=m&lengthOfTime=t  

14. `api/hailingService/trip/publishTrip` 乘客发布行程
>描述：
>>乘客发布他的行程，正在听单的车主将会接收到此行程信息。

>用法：
>>1. 乘客：POST {departure,departureLocation:{lng,lat},destination,destinationLocation:{lng,lat},departureTime,tripType,passengerId}
>>2. 车主：`stompClient.subscribe("/topic/hailingService/trip/publishTrip", function(trip))`

>>实时行程不用指定departureTime，即departureTime为null

>>参数|可选值
>>------ | ------
>>tripType|REAL_TIME（实时），RESERVED（预约）

15. `/api/hailingService/tripOrder/acceptTripOrder` 车主受理订单，通知乘客
>描述：
>>车主受理订单，并通知对应的乘客，  
>>如果没有找到对应行程，返回"Not Found"结果，  
>>如果当前行程无法被受理（行程状态错误），返回"Bad Request"结果。 
>>返回行程订单  

>用法：POST {tripId,driverId}

16. `/hailingService/tripOrder/acceptance-notification` 车主受理订单后触发受理通知
>描述：  
>>乘客受理订单后，将会通过该通道通知乘客。  

>用法：
>>1. 车主：`stompClient.send("/queue/hailingService/tripOrder/acceptance-notification/{passenggerUusername}", {}, JSON.stringify({...}))` **（暂时未提供！）**
>>2. 乘客：`stompClient.subscribe("/user/queue/hailingService/tripOrder/acceptance-notification",function(tripOrder))`

17. `/api/hailingService/tripOrder/pickupPassenger` 车主确认乘客上车
>描述：
>>车主确认乘客上车后，更新行程订单状态和行程状态。    
>>如果行程订单没找到，返回"Not Found"结果，   
>>如果当前行程无法被处理（行程订单状态错误），返回"Bad Request"结果。  
>>返回行程订单

>用法：POST {tripOrderId}  

18. `/api/fareRule/{id}` 查询对应id的计费规则
>描述：
>>返回对应id计费规则  

>用法：GET  

19. `/api/hailingService/tripOrder/confirmArrival` 车主确认到达
>描述：
>>车主确认乘客到达后，结束计算行程费用，更新行程订单状态和行程状态，并插入车费明细记录。    
>>如果行程订单没找到，返回"Not Found"结果，  
>>如果当前行程无法被确认完成（行程订单状态错误），返回"Bad Request"结果。  
>>返回行程订单  

>用法：POST {tripOrderId,lengthOfMileage,lengthOfTime}  

20. `/api/tripOrder/search/findAllByPassenger/{passengerId}` 根据乘客id查询历史行程
>描述：
>>根据乘客id查询历史行程  

>用法：GET [?page,row]  

21. `/api/tripOrder/search/findAllByDriverId/{driverId}` 根据车主id查询历史行程
>描述：
>>根据车主id查询历史行程  

>用法：GET [?page,row]  

22. `/api/tripOrder/{id}` 根据行程订单id查询行程明细
>描述：
>>根据行程订单id查询行程明细  

>用法：GET  

23. `/api/fare/{id}` 根据行程id查询行程车费明细
>描述：
>>根据行程id查询行程车费明细  

>用法：GET  

24. `/api/driver/certifyDriver` 认证车主
>描述：
>>认证车主信息，包括驾驶证、行驶证、车辆信息
>>如果存在图片但上传图片失败，返回"Bad Request"结果。  

>用法：POST 'enctype'='multipart/form-data'的表单
>>key|value
>>------ | ------ 
>>userId|
>>drivingLicenseDTO.driverName|司机姓名
>>drivingLicenseDTO.identification|身份证号
>>drivingLicenseDTO.issueDate|初次领取驾驶证日期
>>drivingLicenseDTO.drivingLicenseImage|驾驶证照片
>>vehicleLicenseDTO.owner|车辆所有人
>>vehicleLicenseDTO.registerDate|车辆注册日期
>>vehicleLicenseDTO.vehicleLicenseImage|行驶证照片
>>carDTO.PlateNo|车牌号
>>carDTO.brand|品牌
>>carDTO.series|系列
>>carDTO.color|颜色

22. `/api/driver/search/findPendingReviewDriver` 查询待审核的车主资料
>描述：
>>查询待审核的车主资料  

>用法：GET  

23. `/images/drivingLicense/{driverId}.jpg` 显示车主的驾驶证照片
>描述：
>>驾驶证照片，直接指定即可。  

24. `/images/vehicleLicense/{driverId}.jpg` 显示车主的行驶证照片
>描述：
>>行驶证照片，直接指定即可。  

25. `/api/driver/{id}` 根据车主id查询车主资料
>描述：
>>根据车主id查询车主资料  

>用法：GET  

26. `/api/driver/reviewDriver` 审核车主资料
>描述：
>>查询待审核的车主资料  

>用法：POST {driverId, driverStatus}  
>>参数|可选值
>>------ | ------
>>driverStatus|APPROVED（审核通过）、UNAPPROVED（审核不通过）  

27. `/api/driver/updateVehicleLicense/{driverId}` 修改行驶证资料，待管理员审核
>描述：
>>行驶证资料包括行驶证、车辆信息
>>如果存在图片但上传图片失败，返回"Bad Request"结果。  

>用法：POST 'enctype'='multipart/form-data'的表单
>>key|value
>>------ | ------ 
>>vehicleLicenseDTO.vehicleLicenseId|
>>carDTO.carId|
>>vehicleLicenseDTO.owner|车辆所有人
>>vehicleLicenseDTO.registerDate|车辆注册日期
>>vehicleLicenseDTO.vehicleLicenseImage|行驶证照片
>>carDTO.PlateNo|车牌号
>>carDTO.brand|品牌
>>carDTO.series|系列
>>carDTO.color|颜色

28. `/api/serviceRating/rateDriver` 评价车主
>描述：
>>乘客评价车主  

>用法：POST {ratingScore,driverId,driverUserId}  

29. `/api/serviceRating/ratePassenger` 评价乘客
>描述：
>>车主评价乘客  

>用法：POST {ratingScore,passengerId,passengerUserId}  

30. `/api/serviceRating/driverRating/{id}` 根据车主评价id返回车主评价
>描述：
>>根据车主评价id返回车主评价  

>用法：GET  

31. `/api/serviceRating/passengerRating/{id}` 根据乘客评价id返回乘客评价
>描述：
>>根据乘客评价id返回乘客评价  

>用法：GET  

32. `/api/serviceRating/complainDriver` 投诉车主
>描述：
>>乘客投诉车主  

>用法：POST {complaintContent,driverId,driverUserId}  

33. `/api/serviceRating/complainPassenger` 投诉乘客
>描述：
>>车主投诉乘客  

>用法：POST {complaintContent,passengerId,passengerUserId}  

34. `/api/serviceRating/driverComplaint/{id}` 根据车主投诉id返回车主投诉
>描述：
>>根据车主投诉id返回车主投诉  

>用法：GET  

35. `/api/serviceRating/passengerComplaint/{id}` 根据乘客投诉id返回乘客投诉
>描述：
>>根据乘客投诉id返回乘客投诉  

>用法：GET  
