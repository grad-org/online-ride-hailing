# online-ride-hailing
毕业设计-网约车系统  

## API手册
**"OK"， "Success"** => 200  
**"Bad Request"** => 400  
**"Unauthorized"** => 401  
**"Not Found"** => 404  
* `/api/auth/login` 用户登录
>描述：
>>如果登录成功，则返回token，  
>>否则返回"Unauthorized"结果。

>用法：POST {username,password}  

* `/api/auth/user` 获取当前用户信息
>描述：
>>如果当前token有效，返回该用户信息，  
>>否则返回"Unauthorized"结果。  

>用法：GET 携带'Authorization'请求头  
* `/api/auth/refresh` 刷新用户token
>用法：
>>如果当前token有效且未过期，返回新的token，  
>>否则返回"Bad Request"结果。  

>用法：GET 携带'Authorization'请求头  

* `/api/auth/verify` 验证用户名是否存在
>描述：
>>如果用户名存在，返回"OK"结果，  
>>否则返回"Not Found"结果。  

>用法：GET ?username=xxx  

* `/api/auth/register` 注册
>描述：
>>如果注册成功，返回用户信息，  
>>否则返回"Bad Request"结果。  

>用法：POST {username,password}  

* `/api/user` 返回所有用户信息
>描述：  
>>返回所有用户信息
>>如果没有为page和row显式赋值，默认page（页码）为1，rows（每页记录数）为10。  

>用法：GET [?page,rows]  

* `/api/user/{id}` 获取用户id为{id}的用户信息
>描述：
>>返回用户id为{id}的用户信息，
>>如果没有找到该用户，则返回"Not Found"结果。  

>用法：GET /api/user/{id}  

* `/api/user/{id}` 修改用户信息
>描述：
>>修改用户id为{id}的用户信息，
>>如果存在图片但上传图片失败，返回"Bad Request"结果。  

>用法：POST {nickname,gender,age,userImage}
>>属性|说明
>>------ | ------
>>nickname|昵称
>>gender|性别
>>age|年龄
>>userImage|用户头像

* `/images/user/{userId}.jpg` 显示用户头像的静态资源
>描述：
>>用户头像，直接指定即可。  

* `/api/trip/search/findPublishedTripByCondition` 根据条件查询已发布的行程（用于设置听单内容后的筛选显示）
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

* `/api/trip/{id}` 返回行程id为{id}的行程信息
>描述：
>>返回行程id为{id}的行程信息，
>>如果没有找到，返回"Not Found"结果。    

>用法：GET /api/trip/{id}  

* `/api/fare/predictFare` 预估车费
>描述：
>>给定预估里程数和预估时长数，返回预估车费结果，  
>>如果lengthOfMileage和lengthOfTime任意一个为空或任意一个不大于0，返回"Bad Request"结果。  

>用法：GET ?lengthOfMileage=m&lengthOfTime=t  

* `api/hailingService/trip/publishTrip` 乘客发布行程
>描述：
>>乘客发布他的行程，正在听单的车主将会接收到此行程信息。

>用法：
>>1. POST {departure,departureLocation:{lng,lat},destination,destinationLocation:{lng,lat},departureTime,tripType,passengerId}

>>实时行程不指定departureTime

>>参数|可选值
>>------ | ------
>>tripType|REAL_TIME（实时），RESERVED（预约）

* `api/hailingService/trip/cancelTrip` 乘客在车主接单之前取消行程
>描述：
>>乘客在车主接单之前取消行程  

>用法：POST {tripId}


* `/api/hailingService/tripOrder/acceptTripOrder` 车主受理订单，通知乘客
>描述：
>>车主受理订单，并通知对应的乘客，  
>>如果没有找到对应行程，返回"Not Found"结果，  
>>如果当前行程无法被受理（行程状态错误），返回"Bad Request"结果。 
>>返回行程订单  

* `/api/hailingService/tripOrder/acceptTripOrder` 车主受理订单，通知乘客
>描述：
>>车主受理订单，并通知对应的乘客，  
>>如果没有找到对应行程，返回"Not Found"结果，  
>>如果当前行程无法被受理（行程状态错误），返回"Bad Request"结果。 
>>返回行程订单  

* `/api/hailingService/tripOrder/cancelTripOrderByPassenger` 乘客在车主受理后，取消订单
>描述：
>>乘客在车主受理前，取消订单，  
>>如果没有找到对应行程，返回"Not Found"结果，  
>>如果当前行程订单无法被取消（行程状态错误），返回"Bad Request"结果。 
>>返回行程订单  

>用法：POST {tripOrderId}

* `/api/hailingService/tripOrder/cancelTripOrderByDriver` 车主在确认乘客上车前，取消订单
>描述：
>>车主在确认乘客上车前，取消订单，  
>>如果没有找到对应行程，返回"Not Found"结果，  
>>如果当前行程订单无法被取消（行程状态错误），返回"Bad Request"结果。 
>>返回行程订单  

>用法：POST {tripOrderId}

* `/api/hailingService/tripOrder/pickUpPassenger` 车主确认乘客上车
>描述：
>>车主确认乘客上车后，更新行程订单状态和行程状态。    
>>如果行程订单没找到，返回"Not Found"结果，   
>>如果当前行程无法被处理（行程订单状态错误），返回"Bad Request"结果。  
>>返回行程订单

>用法：POST {tripOrderId}  

* `/api/fareRule/{id}` 查询对应id的计费规则
>描述：
>>返回对应id计费规则  

>用法：GET  

* `/api/fareRule` 设定计费规则
>描述：
>>设定计费规则 

>用法：POST {initialPrice,initialMileage,unitPricePerKilometer,unitPricePerMinute}
>>属性|说明
>>------ | ------
>>initialPrice|起步价
>>initialMileage|起步里程
>>unitPricePerKilometer|每公里价
>>unitPricePerMinute|每分钟价

* `/api/hailingService/tripOrder/confirmArrival` 车主确认到达
>描述：
>>车主确认乘客到达后，结束计算行程费用，更新行程订单状态和行程状态，并插入车费明细记录。    
>>如果行程订单没找到，返回"Not Found"结果，  
>>如果当前行程无法被确认完成（行程订单状态错误），返回"Bad Request"结果。  
>>返回行程订单  

>用法：POST {tripOrderId,lengthOfMileage,lengthOfTime}  

* `/api/tripOrder/search/findAllByPassenger` 根据乘客id查询历史行程
>描述：
>>根据乘客id查询历史行程  

>用法：GET [?passengerId,page,row]  

* `/api/tripOrder/search/findAllByDriver` 根据车主id查询历史行程
>描述：
>>根据车主id查询历史行程  

>用法：GET [?driverId,page,row]  

* `/api/tripOrder/{id}` 根据行程订单id查询行程明细
>描述：
>>根据行程订单id查询行程明细  

>用法：GET  

* `/api/fare/{id}` 根据行程id查询行程车费明细
>描述：
>>根据行程id查询行程车费明细  

>用法：GET  

* `/api/driver/certifyDriver` 认证车主
>描述：
>>认证车主信息，包括驾驶证、行驶证、车辆信息
>>如果存在图片但上传图片失败，返回"Bad Request"结果。

>用法：POST {[userId/driverId], drivingLicense: {driverName,identification,issueDate,drivingLicenseImage}, vehicleLicense: {owner,registerDate,vehicleLicenseImage}, car: {plateNo,brand,series,color}}
>>属性|说明
>>------ | ------
>>userId|用户id，只有在车主第一次认证时必须携带，其他情况不携带（有userId就没有driverId）
>>driverId|车主id，只有在车主每次审核不通过后重新认证必须携带，其他情况不携带（有driverId就没有userId）
>>driverName|司机姓名
>>identification|身份证号
>>issueDate|初次领取驾驶证日期
>>drivingLicenseImage|驾驶证照片
>>owner|车辆所有人
>>registerDate|车辆注册日期
>>vehicleLicenseImage|行驶证照片
>>plateNo|车牌号
>>brand|品牌
>>series|系列
>>color|颜色

* `/api/driverBalance/bindAlipayAccount` 认证车主
>描述：
>>绑定支付宝账户

>用法：POST {driverBalanceId, alipayAccount}

* `/api/driver/search/findPendingReviewDriver` 查询待审核的车主资料
>描述：
>>查询待审核的车主资料  

>用法：GET  

* `/images/drivingLicense/{driverId}.jpg` 显示车主的驾驶证照片
>描述：
>>驾驶证照片，直接指定即可。  

* `/images/vehicleLicense/{driverId}.jpg` 显示车主的行驶证照片
>描述：
>>行驶证照片，直接指定即可。  

* `/api/driver/{id}` 根据车主id查询车主资料
>描述：
>>根据车主id查询车主资料  

>用法：GET  

* `/api/driver/reviewDriver` 审核车主资料
>描述：
>>查询待审核的车主资料  

>用法：POST {driverId, driverStatus}  
>>参数|可选值
>>------ | ------
>>driverStatus|APPROVED（审核通过）、UNAPPROVED（审核不通过）  

* `/api/driver/updateVehicleLicense/{driverId}` 修改行驶证资料，待管理员审核
>描述：
>>行驶证资料包括行驶证、车辆信息
>>如果存在图片但上传图片失败，返回"Bad Request"结果。

>用法：POST {vehicleLicense: {owner,registerDate,vehicleLicenseImage}, car: {plateNo,brand,series,color}}
>>key|value
>>------ | ------ 
>>owner|车辆所有人
>>registerDate|车辆注册日期
>>vehicleLicenseImage|行驶证照片
>>plateNo|车牌号
>>brand|品牌
>>series|系列
>>color|颜色

* `/api/serviceRating/rateDriver` 评价车主
>描述：
>>乘客评价车主  

>用法：POST {serviceRatingId,driverRatingScore,driverRatingContent}  

* `/api/serviceRating/ratePassenger` 评价乘客
>描述：
>>车主评价乘客  

>用法：POST {serviceRatingId,passengerRatingScore,passengerRatingContent}  

* `/api/payment/alipay/pay` 乘客使用支付宝订单
>描述：
>>乘客使用支付宝支付订单。    
>>如果tripOrderId为空，返回"Not Found"结果，  
>>如果当前行程订单无法被支付（行程订单状态错误），返回"Bad Request"结果。  
>>跳转到支付宝界面进行支付，支付完成后跳转（暂时跳转效果为返回行程订单JSON的页面）  

>用法：POST {tripOrderId,totalAmount}  

* `/api/payment/alipay/withdraw` 车主从账户余额中提现资金到支付宝账户
>描述：
>>车主从账户余额中提现资金，返回乘客资金JSON。  
>>如果提取金额大于账户余额，返回"Bad Request"结果，  

>用法：POST {driverBalanceId,amountOfWithdrawal}  

* `/api/driverBalance/{Id}` 根据车主账户id查询车主账户余额
>描述：
>>根据车主账户id查询车主账户余额  

>用法：GET  
