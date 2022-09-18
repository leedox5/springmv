<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<script src="/js/jquery-3.6.0.min.js"></script>
<script src="/js/bootstrap.min.js"></script>
<script type="text/javascript">
$(document).ready(function() {
    $(".delete").on("click", function() {
        if(confirm("정말로 삭제하시겠습니까?")) {
			var form_data = {};
			var loc = $(this).data("uri");
            $.ajax({
            type: "GET",
            contentType: "application/json",
            url: loc,
            data: null,
            dataType: "json",
            success: function(data) {
                var json = JSON.stringify(data, null, 4);
                if(data.errorContent == "Y") {
                    location.reload();
                } else {
                    //$("#err").html(data.messages[0]);
					alert(data.messages[0]);
                }
            },
            error: function(e) {
                //$("#err").html(e.responseText);
				alert(e.responseText);
                console.log(e);
            }
        });
        }
    });

    $("#btn1").click(function() {
        $("#collap1").collapse('toggle');
        $("#comment").collapse('toggle');
    });

	$("#btn2").click(function(event) {
		event.preventDefault();
        var form_data = {};
		form_data.meaning = $("#meaning11").val();
		console.log(form_data);
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: "/wordbook/savemeaning1/" + ${wordbook.id},
            data: JSON.stringify(form_data),
            dataType: "json",
            success: function(data) {
                var json = JSON.stringify(data, null, 4);
                if(data.errorContent == "Y") {
                    location.reload();
                } else {
                    //$("#err").html(data.messages[0]);
					alert(data.messages[0]);
                }
            },
            error: function(e) {
                //$("#err").html(e.responseText);
				alert(e.responseText);
                console.log(e);
            }
        });
	});
});
</script>
</body>
</html>