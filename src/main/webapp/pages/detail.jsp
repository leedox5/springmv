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
                <td>
                    ${player.score01}:${player.score11}
                </td>
                <td>
                    ${player.score02}:${player.score12}
                </td>
                <td>
                    ${player.score03}:${player.score13}
                </td>
                <td>
                    ${player.score04}:${player.score14}
                </td>
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
                    <c:choose>
                    <c:when test="${match.score1 > 0 or match.score2 > 0}">
                        ${match.score1}:${match.score2}
                    </c:when>
                    <c:otherwise>
                        <button type="button" class="btn btn-sm btn-outline-secondary" data-bs-toggle="collapse" data-bs-target="#collap${match.id}">입력</button>
                    </c:otherwise>
                    </c:choose>
                </td>
            </tr>

            <tr>
                <td colspan="3">
                    <div id="collap${match.id}" class="collapse">
                        <form action="/match/save/${match.id}" method="post">
                            <div class="input-group input-group-sm justify-content-end">
                                <span class="input-group-text">Score</span>
                                <select class="form-select" name="score1" id="score1">
                                    <option value="6" selected>6</option>
                                    <option value="5">5</option>
                                    <option value="4">4</option>
                                    <option value="3">3</option>
                                    <option value="2">2</option>
                                    <option value="1">1</option>
                                    <option value="0">0</option>
                                </select>
                                <span class="input-group-text">:</span>
                                <select class="form-select" name="score2" id="score2">
                                    <option value="6" selected>6</option>
                                    <option value="5">5</option>
                                    <option value="4">4</option>
                                    <option value="3">3</option>
                                    <option value="2">2</option>
                                    <option value="1">1</option>
                                    <option value="0">0</option>
                                </select>
                                <button type="submit" class="btn btn-outline-secondary">결과입력</button>
                            </div>
                        </form>
                    </div>
                </td>
            </tr>

            </c:forEach>
        </tbody>
    </table>

    <!-- 집계-->
    <h5 class="border-bottom py-2">집계</h5>
    <table class="table">
        <thead class="text-center table-dark">
            <tr>
                <td>Name</td>
                <td>MW</td>
                <td>ML</td>
                <td>GW</td>
                <td>GL</td>
                <td>SM</td>
                <td>RA</td>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="player" items="${game.getPlayers()}">
            <tr class="text-center">
                <td>${player.name}     </td>
                <td>${player.matchWin} </td>
                <td>${player.matchLose}</td>
                <td>${player.gameWin}  </td>
                <td>${player.gameLose} </td>
                <td>${player.gameSum}  </td>
                <td>${player.matchRank}</td>
            </tr>
            </c:forEach>
        </tbody>
    </table>

</div>
<%@ include file="bottom.jsp" %>
