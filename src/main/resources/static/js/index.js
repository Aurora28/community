$(function(){
	$("#publishBtn").click(publish);
});

function publish() {
	$("#publishModal").modal("hide");

	// get the title and content
	var title = $("#recipient-name").val();
	var content = $("#message-text").val();
	// ajax
	$.post (
		CONTEXT_PATH + "/discuss/add",
		{"title": title, "content":content},
		function(data) {
			data = $.parseJSON(data);
			// return message in box
			$("#hintBody").text(data.message);
			// show the box and concale in 2s
			$("#hintModal").modal("show");
			setTimeout(function(){
				$("#hintModal").modal("hide");
				// refresh the page
				console.log("to be hide");
				if(data.code == 0) {
					console.log("to be reload");
					window.location.reload();
				}
			}, 2000);
			console.log("ok");
		}
	)

	
}