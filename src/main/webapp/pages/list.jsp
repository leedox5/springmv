<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="top.jsp" %>
<div class="container my-1">
    <div class="btn-toolbar justify-content-end mb-1">
        <div class="btn-group btn-group-sm">
            <a href="/create" class="btn btn-outline-secondary">모임 추가</a>
        </div>
    </div>
    <table class="table">
        <thead class="text-center table-dark">
            <tr>
                <th>모임명</th>
                <th>만든이</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="game" items="${list}">
            <tr class="text-center">
                <td class="text-start">
                    <a href="/game/${game.id}/">${game.subject}</a>
                    <span class="text-danger small ml-2">${game.getPlayers().size()}</span>
                </td>
                <td>${game.creator}</td>
            </tr>
            </c:forEach>
        </tbody>
    </table>
</div>
<%@ include file="bottom.jsp" %>

