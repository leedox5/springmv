<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="top.jsp" %>
<div class="container my-3">
    <h5 class="my-3 border-bottom pb-2">게임 등록</h5>
    <form method="post" class="post-form my-3">
        <div class="mb-2">
            <label for="subject">제목</label>
            <input type="text" class="form-control" name="subject" id="subject" value="${game.getSubject()}">
        </div>
        <button type="submit" class="btn btn-primary">Save</button>
    </form>
</div>
<%@ include file="bottom.jsp" %>

