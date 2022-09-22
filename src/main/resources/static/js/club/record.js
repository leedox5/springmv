$(document).ready(function() {
    console.log("ready");
    $.getName();
    $("#btn1").click(function() {
        alert("Clicked!");
    });
});

$.getName = function() {
    console.log("in getName function");
    $("#sp1").text("선수3");
}
