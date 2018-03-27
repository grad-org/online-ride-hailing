var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#users").html("");
}

function connect() {
    var socket = new SockJS('/stomp');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        var driverId = 1;
        stompClient.subscribe('/user/'+ driverId +'/listen', function (user) {
            showUser(JSON.parse(user.body));
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
    var point = {lng: a[0], lat: a[1]};

    //

    stompClient.send("/app/send", {}, JSON.stringify(point));
}

function showUser(user) {
    $("#users").append("<tr><td>" + user + "</td></tr>");
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