<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="top.jsp" %>
<div class="container my-3">
   <form action="/wordbook" method="post">
        <div class="input-group input-group-sm mb-3">
            <span class="input-group-text">Key</span>
            <input  type="text" class="form-control" name="word" id="word" value="${key}">
            <button  type="submit" class="btn btn-outline-secondary">Search</button>
        </div>
    </form>
    <table class="table">
        <thead class="text-center table-dark">
            <tr>
                <th>NO</th>
                <th>단어</th>
                <th>의미</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="wordbook" items="${list}">
            <tr class="text-start">
                <td class="text-end">${wordbook.seq}</td>
                <td class="text-start">
                    <a href="/wordbook/${wordbook.id}/${key}">${wordbook.word}</a>
                    <c:if test="${wordbook.getWordMeanings().size() > 0}">
                    <span class="text-danger small ml-2">${wordbook.getWordMeanings().size()}</span>
                    </c:if>
                </td>
                <td>${wordbook.meaning1}</td>
            </tr>
            </c:forEach>
        </tbody>
    </table>
    <a href="/wordbook/create" class="btn btn-primary">추가</a>
</div>
<%@ include file="bottom.jsp" %>

