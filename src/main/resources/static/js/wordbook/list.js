$.getCheck = function() {
    console.log("Hello, this is list.js");
}

$.getData = function(loc, callback) {
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: loc,
        data: "",
        dataType: "json",
        success: callback,
        error: function(e) {
            alert("Request Error!");
		    console.log(e.responseText);
        }
    });
}

