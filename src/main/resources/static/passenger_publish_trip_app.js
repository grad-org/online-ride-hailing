var stompClient = null;

function connect() {
    var socket = new SockJS('/orh');
    stompClient = Stomp.over(socket);

    var token = window.localStorage.getItem('Auth-Token');

    stompClient.connect({'Auth-Token': token}, function (frame) {

    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

function showTripOrder(tripOrder) {
    console.log("司机名: " + tripOrder.driverNickname);
    console.log("头像: " + "/images/user/" + tripOrder.driverUserId + '.jpg');
    console.log("车牌号: " + tripOrder.plateNo);
    console.log("车型: " + tripOrder.brand + '#' + tripOrder.series + '#' + tripOrder.color);
    console.log("出发地: " + tripOrder.departure);
    console.log("目的地: " + tripOrder.destination);
}

function publishTrip() {
    var departure = $("#departure").val();
    var destination = $("#destination").val();
    var departureTime = $("#departureTime").val();
    var tripType = $("#tripType").val();

    var trip = {
        "departure": departure,
        "destination": destination,
        "departureTime": departureTime,
        "tripType": tripType,
        "passengerId": 1
    };

    // 发布行程
    // stompClient.send("/api/hailingService/trip/publishTrip", {}, JSON.stringify(trip)); -- 该方法已不提供 --
    // 改成用POST来发布行程

    // 接收接单通知
    stompClient.subscribe('/user/queue/hailingService/tripOrder/acceptance-notification',function(tripOrder){
        // 取消监控附近车辆行进轨迹, unsubscribe
        // ...

        // 展示行程订单信息
        showTripOrder(JSON.parse(tripOrder.body));

        // 监控接单车辆的行车轨迹
        stompClient.subscribe('/user/queue/hailingService/car/uploadCarLocation',function(carLocation){
            console.log(JSON.parse(carLocation.body));
        });
    });
}

$(function () {
    connect();
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#send" ).click(function() { publishTrip(); });

    // 登录认证
    $.ajaxSetup({
        contentType: "application/json; charset=utf-8"
    });

    $.post({
        url: '/api/auth/login',
        data: JSON.stringify({username: "p1", password: "p1"}),
        success: function(response) {
            window.localStorage.setItem('Auth-Token', response.data.token);
        }
    });
});