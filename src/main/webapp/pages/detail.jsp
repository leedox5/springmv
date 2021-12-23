<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="top.jsp" %>
<div class="container my-3">
    <h2 class="border-bottom py-2">${game.subject}</h2>

    <table class="table">
        <thead class="text-center table-dark">
            <tr>
                <td>NO</td>
                <td>Name</td>
                <td>G1</td>
                <td>G2</td>
                <td>G3</td>
                <td>G4</td>
                <td></td>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="player" items="${game.getPlayers()}">
            <tr class="text-center">
                <td>${player.seq}</td>
                <td>${player.name}</td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td>

                </td>
            </c:forEach>
        </tbody>
    </table>
    <form action="/player/create/${game.id}" method="post">
        <div class="input-group input-group-sm mb-3">
            <span class="input-group-text">Name</span>
            <input  type="text" class="form-control" name="name" id="name" value="">
            <button  type="submit" class="btn btn-outline-secondary">Add</button>
        </div>
    </form>
    <div class="btn-toolbar justify-content-end">
        <div class="btn-group btn-group-sm">
            <a href="/match/create/${game.id}" class="btn btn-outline-secondary">대진표 생성</a>
        </div>
    </div>
    <h5 class="border-bottom py-2">대진표</h5>
    <table class="table">
        <thead class="text-center table-dark">
            <tr>
                <td>Team A : Team B</td>
                <td>결과</td>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="match" items="${game.getMatches()}">
            <tr class="text-center">
                <td>[${match.seq}]${match.description}</td>
                <td>
                        ${match.score}
                </td>
            </tr>
            </c:forEach>
        </tbody>
    </table>
</div>
<%@ include file="bottom.jsp" %>
