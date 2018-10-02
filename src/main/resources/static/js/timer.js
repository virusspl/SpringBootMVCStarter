

// <span id="timer"></span>

// Set the date we're counting down to
//var countDownDate = new Date("Jan 5, 2022 15:37:25").getTime();
var countDownDate = document.getElementById("timeLong").innerHTML;


// Update the count down every 1 second
var x = setInterval(function() {

  // Get todays date and time
  var now = new Date().getTime();

  // Find the distance between now and the count down date
  var distance = countDownDate - now;

  // Time calculations for days, hours, minutes and seconds
  var days = Math.floor(distance / (1000 * 60 * 60 * 24));
  var hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
  var minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
  var seconds = Math.floor((distance % (1000 * 60)) / 1000);
  var millis = Math.floor((distance % (1000)) / 10);
  if (millis < 10) {
	  millis = "0" + millis;
  }
  if (seconds < 10) {
	  seconds = "0" + seconds;
  }
  if (minutes < 10) {
	  minutes = "0" + minutes;
  }

  // Display the result in the element with id="timer"
  document.getElementById("timer").innerHTML = days + "d " + hours + "h "
  + minutes + "m " + seconds + "s " + millis + "\"";

  // If the count down is finished, write some text
  if (distance < 0) {
    clearInterval(x);
    document.getElementById("timer").innerHTML = "YEAH!";
  }
}, 10);
