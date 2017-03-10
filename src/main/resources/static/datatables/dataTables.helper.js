function getUrl (){
	
	var baseUrl;
	var finalUrl;

	if(window.location.host == "localhost:8080"){
		baseUrl = "http://localhost:8080/datatables/"
	}
	else if (window.location.host == "atw-admin_3:8080"){
		baseUrl = "http://atw-admin_3:8080/datatables/"
	}
	else{
		baseUrl = "http://atwsrv-java:8080/adr/datatables/"
	}
	
	return baseUrl;
	
}