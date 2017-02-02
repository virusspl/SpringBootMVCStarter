$().ready(function() {
    var cookieName = "cookieok";
    $("#close-cookie-warn").click(function(){
        var expire = new Date();
        expire.setTime((new Date()).getTime() + 3600000*24*365);
        document.cookie = cookieName + "=1;expires=" + expire;
        $("#cookie-warn").hide("slow");
    });
 
    var str = '; '+ document.cookie +';';
    var index = str.indexOf('; '+ escape(cookieName) +'=');
    if (index === -1) {
        $("#cookie-warn").show();
    }
});