function isIEorEDGE() {

	var ieWarn = false;

	/*
	if (navigator.appName == 'Microsoft Internet Explorer'){
	return true; // IE
	}
	else if(navigator.appName == "Netscape"){                       
	 return navigator.appVersion.indexOf('Edge') > -1; // EDGE
	}       

	return false;
	 */
	var ua = window.navigator.userAgent;
	var msie = ua.indexOf('MSIE ');
	if (msie > 0) { // IE 10 or older => return version number
		//return parseInt(ua.substring(msie + 5, ua.indexOf('.', msie)), 10);
		ieWarn = true;
	}
	var trident = ua.indexOf('Trident/');
	if (trident > 0) { // IE 11 => return version number
		var rv = ua.indexOf('rv:');
		//return parseInt(ua.substring(rv + 3, ua.indexOf('.', rv)), 10);
		ieWarn = true;
	}
	var edge = ua.indexOf('Edge/');
	if (edge > 0) { // Edge (IE 12+) => return version number
		//return parseInt(ua.substring(edge + 5, ua.indexOf('.', edge)), 10);
		ieWarn = true;
	}

	if(ieWarn){
		document.getElementById("microsoftWarning").style.display = "block";
	}

}

isIEorEDGE();