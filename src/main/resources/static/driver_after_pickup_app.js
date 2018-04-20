var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    $("#disconnect").prop("disabled", !connected);
}

function connect() {
    var socket = new SockJS('/orh');
    stompClient = Stomp.over(socket);

    var token = window.localStorage.getItem('Auth-Token');

    stompClient.connect({'Auth-Token': token}, function (frame) {
        setConnected(true);

        // listenOrderSubscription = stompClient.subscribe('/topic/hailingService/passenger/publishTrip', function (trip) {
        //     showTrip(JSON.parse(trip.body));
        // });
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
// function unsubscribe() {
//     if (listenOrderSubscription !== null) {
//         listenOrderSubscription.unsubscribe();
//     }
// }

function sendLocation() {
    // 获取位置
    var a = $("#location").val().split(',');

    var carLocation = {carId: 1, lng: a[0], lat: a[1]};

    var passengerUsername = 'p1';

    // LBS储存车主实时位置
    // ...

    // 将位置发送给订单对应的乘客
    stompClient.send("/api/queue/hailingService/car/uploadCarLocation/"+passengerUsername, {}, JSON.stringify(carLocation));
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