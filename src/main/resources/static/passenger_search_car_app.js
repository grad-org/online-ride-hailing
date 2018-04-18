var stompClient = null;

function connect() {
    var socket = new SockJS('/orh');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/topic/driver/uploadCarLocation', function (carLocation) {
            showCarLocation(JSON.parse(carLocation.body));
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

function sendLocation() {
    // 获取位置
    var a = $("#location").val().split(',');
    var location = {lng: a[0], lat: a[1]};

    // 根据位置重新查找附近车辆
    // ...
}

function showCarLocation(carLocation) {
    // 判断车辆位置是否在指定范围内，是则显示
    // ...

    $("#carLocations").append("<tr><td>" + JSON.stringify(carLocation) + "</td></tr>"); // JSON.stringify(): json对象->字符串
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

            connect();
            $("form").on('submit', function (e) {
                e.preventDefault();
            });
            $( "#send" ).click(function() { sendLocation(); });
        }
        else {
            alert('failed'+this.getStatus());
        }
    },{enableHighAccuracy: true})
});