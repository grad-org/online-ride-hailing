var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#orderList").show();
    }
    else {
        $("#orderList").hide();
    }
    $("#orders").html("");
}

function connect() {
    var socket = new SockJS('/orh');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        stompClient.subscribe('/topic/orderBroadcast', function (order) {
            showOrder(JSON.parse(order.body));
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

function sendLocation() {
    // 获取位置
    var a = $("#location").val().split(',');
    var location = {lng: a[0], lat: a[1]};

    // LBS储存车主实时位置
    // ...

    // 将消息发送到后端，由后端处理数据后再推送到订阅/topic/carBroadcast的前端
    stompClient.send("/app/carBroadcast", {}, JSON.stringify(location));

    // 直接将消息发送给订阅/topic/carBroadcast的前端，不通过后端处理
    // stompClient.send("/topic/broadcast",{}, JSON.stringify(location));
}

function showOrder(order) {
    $("#orders").append("<tr><td>" + order + "</td></tr>");
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
        }
        else {
            alert('failed'+this.getStatus());
        }
    },{enableHighAccuracy: true})
});