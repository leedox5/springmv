<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="top.jsp" %>
<div class="container my-3">
    <h2 class="border-bottom py-2">${wordbook.word}</h2>
    <h6 class="small text-muted text-end">[${wordbook.seq}] ${wordbook.updDate} 조회수:${wordbook.access}</h6>

        <div id="collap${wordbook.id}" class="collapse show mulcol${wordbook.id}">
            <div class="card mt-1" >
                <div class="card-body">
                    <span style="white-space: pre-line;">${wordbook.meaning1}</span>
                </div>
                <div class="card-footer text-end comment">
                    <a href=".mulcol${wordbook.id}" role="button" data-bs-toggle="collapse" class="small">수정</a>
                </div>
            </div>
        </div>
        <div class="collapse mulcol${wordbook.id}" id="comment">
            <form class="my-2" action="/wordbook/save/${wordbook.id}/${path}" method="post">
                <div class="mb-1">
                    <input type="text" class="form-control" name="word" id="word" value="${wordbook.word}">
                </div>
                <div class="mb-1">
                    <input type="text" class="form-control" name="seq" id="seq" value="${wordbook.seq}">
                </div>
                <div class="mb-1">
                    <textarea  name="meaning1" id="meaning1" class="form-control" rows="1">${wordbook.meaning1}</textarea>
                </div>
                <div class="mb-1">
                    <textarea  name="meaning2" id="meaning2" class="form-control" rows="1">${wordbook.meaning2}</textarea>
                </div>
                <div class="mb-1">
                    <textarea  name="example1" id="example1" class="form-control" rows="2">${wordbook.example1}</textarea>
                </div>
                <div class="text-end">
                    <button type="submit" class="btn btn-sm btn-outline-primary">등록</button>
                    <button type="button" class="btn btn-sm btn-outline-primary" data-bs-toggle="collapse" data-bs-target=".mulcol${wordbook.id}">취소</button>
                </div>
            </form>
        </div>

    <c:forEach var="wordMeaning" items="${wordbook.getWordMeanings()}">
      <div id="collap${wordMeaning.id}" class="collapse show mulcol${wordMeaning.id}">
            <div class="card mt-1" >
                <div class="card-body">
                    <span style="white-space: pre-line;">${wordMeaning.meaning}</span>
                </div>
                <div class="card-footer text-end comment">
                    <a href=".mulcol${wordMeaning.id}" role="button" data-bs-toggle="collapse" class="small">수정</a>
                    <a href="#" class="small delete" data-uri="/meaning/delete/${wordMeaning.id}">삭제</a>
                </div>
            </div>
        </div>
        <div class="collapse mulcol${wordMeaning.id}" id="comment">
            <form class="my-2" action="/wordbook/savemeaning/${wordbook.id}/${path}" method="post">
                <input  type="hidden" name="id" id="id" value="${wordMeaning.id}">
                <div class="mb-2">
                    <textarea  name="meaning" id="meaning" class="form-control" rows="2">${wordMeaning.meaning}</textarea>
                </div>
                <div class="text-end">
                    <button type="submit" class="btn btn-sm btn-outline-primary">등록</button>
                    <button type="button" class="btn btn-sm btn-outline-primary" data-bs-toggle="collapse" data-bs-target=".mulcol${wordMeaning.id}">취소</button>
                </div>
            </form>
        </div>
    </c:forEach>

    <c:if test="${not empty wordbook.meaning2}">
    <div class="card my-2">
        <div class="card-body">
            <div class="card-text" style="white-space: pre-line;">${wordbook.meaning2}</div>
        </div>
    </div>
    </c:if>

    <c:if test="${not empty wordbook.meaning3}">
    <div class="card my-2">
        <div class="card-body">
            <div class="card-text" style="white-space: pre-line;">${wordbook.meaning3}</div>
        </div>
    </div>
    </c:if>

    <c:if test="${not empty wordbook.example1}">
    <div class="card my-2">
        <div class="card-body">
            <div class="card-text" style="white-space: pre-line;">${wordbook.example1}</div>
        </div>
    </div>
    </c:if>

	<div class="mb-2">
		<textarea  name="meaning11" id="meaning11" class="form-control" rows="2"></textarea>
	</div>
	<div class="text-end">
		<button id="btn2" class="btn btn-sm btn-outline-primary">의미등록</button>
	</div>

    <form action="/wordbook" method="post">
        <input  type="hidden" name="opt" id="opt" value="${opt}">
        <input  type="hidden" name="key" id="key" value="${key}">
        <button  type="submit" class="btn btn-outline-secondary">목록으로</button>
    </form>

</div>
<%@ include file="WordbookBottom.jsp" %>
