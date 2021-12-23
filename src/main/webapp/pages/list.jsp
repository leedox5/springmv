<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="top.jsp" %>
<div class="container my-3">
    <table class="table">
        <thead class="text-center table-dark">
            <tr>
                <th>제목</th>
                <th>만든이</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="game" items="${list}">
            <tr class="text-center">
                <td class="text-start">
                    <a href="/game/${game.id}/">${game.subject}</a>
                    <span class="text-danger small ml-2">5</span>
                </td>
                <td>${game.creator}</td>
            </tr>
            </c:forEach>
        </tbody>
    </table>
    <a href="/create" class="btn btn-primary">매치 추가</a>
</div>
<%@ include file="bottom.jsp" %>

