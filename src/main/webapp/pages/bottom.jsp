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
});
</script>
</body>
</html>