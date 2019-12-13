$(document).ready(function() {
	$(window).keydown(function(event) {

		if (event.keyCode == 13) {
			if (
					window.location.href.indexOf("login") > -1
					||
					window.location.href.indexOf("bhptickets/edit") > -1
					||
					window.location.href.indexOf("bhptickets/create") > -1
					||
					window.location.href.indexOf("bhptickets/show") > -1
					||
					window.location.href.indexOf("tools/createproject") > -1
					||
					window.location.href.indexOf("tools/editproject") > -1
					||
					window.location.href.indexOf("inventory/editinventory") > -1
					||
					window.location.href.indexOf("inventory/createinventory") > -1
					||
					window.location.href.indexOf("qsurveys/templates/create") > -1
					||
					window.location.href.indexOf("qsurveys/templates/edit") > -1
					||
					window.location.href.indexOf("qcheck/create") > -1
					||
					window.location.href.indexOf("qcheck/edit") > -1
					||
					window.location.href.indexOf("qcheck/show") > -1
					||
					window.location.href.indexOf("mailer/create") > -1
					||
					window.location.href.indexOf("mailer/send") > -1
					||
					window.location.href.indexOf("readcard") > -1
					) {
				return;
			}
			else{
				event.preventDefault();
				return false;
			}

		}
	});
});