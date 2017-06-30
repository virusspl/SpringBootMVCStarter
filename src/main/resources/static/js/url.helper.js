function getAppUrl (){
	
	var baseUrl;
	var finalUrl;
	
	var url = window.location.href;
	var arr = url.split("/");
	var result = arr[0] + "//" + arr[2];
	if(arr[3] == "adr"){
		result = result + "/adr";
	}
	return result;
}