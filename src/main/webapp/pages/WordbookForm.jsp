<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="top.jsp" %>
<div class="container my-3">
    <h5 class="my-3 border-bottom pb-2">단어 등록</h5>
    <form method="post" class="post-form my-3">
        <div class="mb-2">
            <label for="subject">단어</label>
            <input type="text" class="form-control" name="word" id="word" value="">
        </div>
        <div class="mb-2">
            <label for="content">의미</label>
            <textarea class="form-control" name="meaning1" id="meaning1"
                      rows="5"></textarea>
        </div>
        <button type="submit" class="btn btn-primary">Save</button>
    </form>
</div>
<%@ include file="bottom.jsp" %>

