var stompClient = null;
var listenOrderSubscription = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#tripList").show();
    }
    else {
        $("#tripList").hide();
    }
    $("#trips").html("");
}

function connect() {
    var socket = new SockJS('/orh');
    stompClient = Stomp.over(socket);

    var token = window.localStorage.getItem('Auth-Token');

    stompClient.connect({'Auth-Token': token}, function (frame) {
        setConnected(true);
        // 连接之后，先调用/api/trip/published获取已发布的行程并进行渲染
        // ...

        listenOrderSubscription = stompClient.subscribe('/topic/hailingService/passenger/publishTrip', function (trip) {
            showTrip(JSON.parse(trip.body));
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

// 终止听单，用于接单后的操作，订单完成后重新订阅
function unsubscribe() {
    if (listenOrderSubscription !== null) {
        listenOrderSubscription.unsubscribe();
    }
}

function sendLocation() {
    // 获取位置
    var a = $("#location").val().split(',');

    var carId = 1;
    var lng = a[0];
    var lat = a[1];
    var carLocation = {carId: carId, lng: a[0], lat: a[1]};

    // LBS储存车主实时位置
    // ...

    // 将消息发送到后端，由后端处理数据后再推送到订阅/topic/carBroadcast的前端
    stompClient.send("/api/hailingService/driver/uploadCarLocation", {}, JSON.stringify(carLocation));

    // 直接将消息发送给订阅/topic/hailingService/driver/uploadCarLocation的前端，不需要通过后端处理
    // stompClient.send("/topic/hailingService/driver/uploadCarLocation",{}, JSON.stringify(location));
}

function showTrip(trip) {
    $("#trips").append([
        '<tr><td>',
        '头像: <img src="/images/user/' + trip.passenger.user.id+'.jpg">',
        '昵称: ' + trip.passenger.user.nickname,
        '出行时间: ' + trip.departureTime,
        '出发地: ' + trip.departure,
        '目的地: ' + trip.destination,
        '</td></tr>'
    ].join(''));
}

$(function () {
    // 百度地图API功能
    var map = new BMap.Map("allmap");
    var point = new BMap.Point(116.331398,39.897445);
    map.centerAndZoom(point,12);

    var geolocation = new BMap.Geolocation();
    geolocation.getCurrentPosition(function(r){
        if(this.getStatus() == BMAP_STATUS_SUCCESS){
            var mk = new BMap.Marker(r.point);
            map.addOverlay(mk);
            map.panTo(r.point);
            $('#location').val(r.point.lng+','+r.point.lat);

            $("form").on('submit', function (e) {
                e.preventDefault();
            });
            $( "#connect" ).click(function() { connect(); });
            $( "#disconnect" ).click(function() { disconnect(); });
            $( "#send" ).click(function() { sendLocation(); });

            // 登录认证
            $.ajaxSetup({
                contentType: "application/json; charset=utf-8"
            });

            $.post({
                url: '/api/auth/login',
                data: JSON.stringify({username: "d1", password: "d1"}),
                success: function(response) {
                    window.localStorage.setItem('Auth-Token', response.data.token);
                }
            });
        }
        else {
            alert('failed'+this.getStatus());
        }
    },{enableHighAccuracy: true})
});