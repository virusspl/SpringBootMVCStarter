var stompClient = null;

$(document).ready(function() {
	connect();
});

function wsUrl (suffix){
	var baseUrl;
	var finalUrl;
	var url = window.location.href;
	var arr = url.split("/");
	var result = arr[0] + "//" + arr[2];
	if(arr[3] == "adr"){
		result = result + "/adr";
	}
	result = result + "/" + suffix;
	return result;
}

function setConnected(connected) {
    if (connected) {
    	$("#log").html("<p>ONLINE</p>");
    }
    else {
    	$("#log").html("<p>OFFLINE</p>");
    }
}

function connect() {
	var socket = new SockJS(wsUrl('sbs-websocket'));
	stompClient = Stomp.over(socket);
    stompClient.connect("user","user", function (frame) {
        stompClient.subscribe('/topic/messages', function (msg) {
            showMessage(JSON.parse(msg.body).sender, JSON.parse(msg.body).content);
        });
        stompClient.subscribe('/topic/cancelMessage', function () {
        	cancelMessages();
        });
        stompClient.send("/app/connect", {}, "" );
        setConnected(true);
    });
}

function sendMessage() {
	if (document.getElementById("message").value.length==0){
		return;
	}
	stompClient.send("/app/postmessage", {}, JSON.stringify({
		'sender' : $("#name").val(),
		'content' : $("#message").val()
	}));
	document.getElementById("message").value = "";
	document.getElementById("message").focus();
}

function showMessage(sender, message) {
	alert(message);
	$("#liveContainer").html(
			"<div id='live' class='alert alert-info'> <span> " + message + "</span>"
						);
}

function requestCancelMessage() {
	stompClient.send("/app/removemessage", {}, "remove");
}

function cancelMessages() {
	$("#liveContainer").html("")
}

/*
$("#message").on('keyup', function (e) {
    if (e.keyCode == 13) {
        sendMessage();
    }
});
*/