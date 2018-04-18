var stompClient = null;

function connect() {
    var socket = new SockJS('/orh');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {

    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

function publishTrip() {
    var departure = $("#departure").val();
    var destination = $("#destination").val();
    var departureTime = $("#departureTime").val();
    var tripType = $("#tripType").val();

    var passengerId = 2;
    var trip = {
        "departure": departure,
        "destination": destination,
        "departureTime": departureTime,
        "tripType": tripType,
        "passengerId": passengerId
    };


    stompClient.send("/api/hailingService/passenger/publishTrip", {}, JSON.stringify(trip));
}

$(function () {
    connect();
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#send" ).click(function() { publishTrip(); });
});