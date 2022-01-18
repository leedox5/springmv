<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<script src="/js/jquery-3.6.0.min.js"></script>
<script src="/js/bootstrap.min.js"></script>
<script type="text/javascript">
$(document).ready(function() {
    $(".delete").on("click", function() {
        if(confirm("정말로 삭제하시겠습니까?")) {
            location.href = $(this).data("uri");
        }
    });

    $("#btn1").click(function() {
        $("#collap1").collapse('toggle');
        $("#comment").collapse('toggle');
    });

    $("#form1").submit(function(event) {
        event.preventDefault();
        ajax_submit();
    });

    function ajax_submit() {
        var form_data = {};
        form_data.name = $("#name").val();

        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: "/player/create1/" + ${game.id},
            data: JSON.stringify(form_data),
            dataType: "json",
            success: function(data) {
                var json = JSON.stringify(data, null, 4);
                if(data.errorContent == "Y") {
                    location.reload();
                } else {
                    $("#err").html(data.messages[0]);
                }
            },
            error: function(e) {
                $("#err").html(e.responseText);
                console.log(e);
            }
        });
    }
});
</script>
</body>
</html>