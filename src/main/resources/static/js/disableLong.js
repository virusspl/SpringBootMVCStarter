function disableLong(el) {
	setTimeout(function() {
		disableButton(el);
	}, 0);
}

function disableButton(el) {
	//$("#btnSubmit").prop('disabled', true);
	el.disabled = true;
}
