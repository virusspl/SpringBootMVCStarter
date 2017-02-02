var stompClient = null;

function setConnected(connected) {
	$("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
    	$("#messages").html("");
    }
    else {
    	$("#messages").html("<p>...</p>");
    }
    $("#users").html("");
}

function wsURL(path) {
    var protocol = (location.protocol === 'https:') ? 'wss://' : 'ws://';
    var url = protocol + location.host;
    if(location.hostname === 'localhost') {
        url += '/' + location.pathname.split('/')[1]; // add context path
    }
    return url + path;
}


function connect() {
	//var path = wsURL('/chat-websocket');
	var socket = new SockJS('/chat-websocket');
	stompClient = Stomp.over(socket);
    stompClient.connect(document.getElementById("name").value,"", function (frame) {
        setConnected(true);
        stompClient.subscribe('/topic/messages', function (msg) {
            showMessage(JSON.parse(msg.body).sender, JSON.parse(msg.body).content);
        });
        stompClient.subscribe('/user/topic/priv', function (msg) {
            showPrivMessage(JSON.parse(msg.body).sender, JSON.parse(msg.body).content);
        });
        stompClient.subscribe('/topic/users', function (users) {
            showUsers(JSON.parse(users.body).users);
        });
        stompClient.subscribe('/user/topic/ping', function (msg) {
        	pong();
        });
        stompClient.send("/app/login", {}, "" );
        pong();
        
    });
   
}

function pong() {
	stompClient.send("/app/pong", {}, "" );
}

function disconnect() {
	stompClient.send("/app/disconnect", {}, "" );
    if (stompClient != null) {
        stompClient.disconnect();
    }
    setConnected(false);
}


$("#message").on('keyup', function (e) {
    if (e.keyCode == 13) {
        sendMessage();
    }
});


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
	var date = new Date(Date.now()).toLocaleString();
		$("#messages").append(
				"<div class='row'  style='border-bottom: 1px solid #DDDDDD;'><div class='col s1'><img src='/useravatar/" + sender + "' style='width: 35px; height: 35px;'  class='circle'/></div><div class='col s11 orange-text text-darken-3'><b>" +
				sender + "</b></div><div class='col s11 grey-text text-darken-0'>" + date + "</div><div class='col s12'>" +
				message + "</div></div>"
				);
		$("#messages").animate({scrollTop : $('#messages').prop("scrollHeight")}, 500);
}

function showPrivMessage(sender, message) {
	var date = new Date(Date.now()).toLocaleString();
	$("#messages").append(
			"<div class='row'  style='border-bottom: 1px solid #DDDDDD;'><div class='col s1'><img src='/useravatar/" + sender + "' style='width: 35px; height: 35px;' class='circle'/></div><div class='col s11 orange-text text-darken-3'><b>" +
			sender + "</b></div><div class='col s11 grey-text text-darken-0'>" + date + "</div><div class='col s12 cyan lighten-4'><b><i>" +
			message + "</i></b></div></div>"
			);
	$("#messages").animate({scrollTop : $('#messages').prop("scrollHeight")}, 500);
}

function showUsers(users) {
	$("#users").html("");
	for(var i = 0; i < users.length; i++) {
	    var user = users[i];
	    $("#users").append("<div style='width: 100%; padding: 3px; display: block; border-bottom: 1px solid #9ADADA;'><a href='#!' class='black-text' style='display: block;' onclick='makePriv(&apos;" + user + "&apos;)'><img src='/useravatar/" + user + "' style='width:25px; height: 25px;' class='circle'/> &nbsp&nbsp" + user + "</a></div>");
	};
}

function makePriv(username){
	document.getElementById("message").value = document.getElementById("message").value + "@" + username + " "
	document.getElementById("message").focus();
}

$(function () {
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
});


$('#toggle_fullscreen').on('click', function(){

	  element = $('#fullscreen').get(0);
	  messages = $('#messages').get(0);
	  if (
	    document.fullscreenElement ||
	    document.webkitFullscreenElement ||
	    document.mozFullScreenElement ||
	    document.msFullscreenElement
	  ) {
	    if (document.exitFullscreen) {
	      document.exitFullscreen();
	    } else if (document.mozCancelFullScreen) {
	      document.mozCancelFullScreen();
	    } else if (document.webkitExitFullscreen) {
	      document.webkitExitFullscreen();
	    } else if (document.msExitFullscreen) {
	      document.msExitFullscreen();
	    }
	  } else {
	    if (element.requestFullscreen) {
	      element.requestFullscreen();
	    } else if (element.mozRequestFullScreen) {
	      element.mozRequestFullScreen();
	    } else if (element.webkitRequestFullscreen) {
	      element.webkitRequestFullscreen(Element.ALLOW_KEYBOARD_INPUT);
	    } else if (element.msRequestFullscreen) {
	      element.msRequestFullscreen();
	    }
	  }
	});


$(document).ready(function() {
  $(window).on('resize', function(){
	  if ((!document.mozFullScreen && !document.webkitIsFullScreen)) {
		  document.getElementById( "messages" ).style.height = "51vh";
		} else {
			document.getElementById( "messages" ).style.height = "92vh";
		}
	  
	  
  });
});

	