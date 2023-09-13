$.chkDetail = function(id) {
    console.log("Hello, this is detail.js! id==>" + id);
    //$("#div1").hide();
    var loc = `/data/words/${id}`
    $.getData(loc, afterGetDetail);
}

// [목록으로]
/*
const btn_d1 = document.getElementById("btn_d1");

if (btn_d1) {
    btn_d1.addEventListener('click', () => {
        if(path) {
            window.location.href = "/wordbook/words/" + path;
        } else {
            window.location.href = "/wordbook/words.html";
        }
    });
}
*/
// 동적으로 생성된 element 에 이벤트 걸기
$(document).on("click", "#btn_d1", function() {
    //window.location.href = "/words.html";
    var loc = `/data/search`;
    $("#main").load("/cont1.html", function(responseTxt, statusTxt, xhr) {
        if(statusTxt == "success") {
            //$.getData(loc, afterGetData);
            $("#top").show();
            $("#btn1").trigger("click");
        }
    });
});

$(document).on("click", ".delete", function(e) {
    if(confirm("정말로 삭제하시겠습니까?")) {
        var loc = e.target.dataset.uri;
        var wordbook_id = $("#word_id").val();

        if(loc == "/data/word/delete/") {
            loc += wordbook_id;
        }
        console.log(loc);

        $.ajax({
            type: "GET",
            contentType: "application/json",
            url: loc,
            data: null,
            dataType: "json",
            success: function(data) {
                console.log(data);
                if(loc.indexOf("meaning") > -1) {
                    detail(wordbook_id);
                } else {
                    $("#btn_d1").trigger("click");
                }
            },
            error: function(e) {
                console.log(e.responseText);
            }
        });
    }
});

// 메모수정
$(document).on("submit", ".meaning", function(e) {
    e.preventDefault();
    console.log($(this).serializeArray());

    const url = e.target.action;
    console.log(url);

    const data = Object.fromEntries(new FormData(e.target).entries());
    console.log(data);

    var wordbook_id = $("#word_id").val();

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: url,
        data: JSON.stringify(data),
        dataType: "json",
        beforeSend: function(jqXHR, settings) {
            var header = $("meta[name='_csrf_header']").attr("content");
            var token = $("meta[name='_csrf']").attr("content");
            jqXHR.setRequestHeader(header, token);
        },
        success: function(res) {
            console.log(res);
            detail(wordbook_id);
        },
        error: function(e) {
            console.log(e.responseText)
        }
    });
});

// 메모등록
$(document).on("click", "#btn2", function() {
    var form_data = {};
    form_data.meaning = $("#memo").val();

    var id = $("#word_id").val();

    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/data/savemeaning/" + id,
        data: JSON.stringify(form_data),
        dataType: "json",
        beforeSend: function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function(data) {
            var json = JSON.stringify(data, null, 4);
            if(data.errorContent == "Y") {
                //location.reload();
                detail(id);
            } else {
                //$("#err").html(data.messages[0]);
				alert(data.messages[0]);
            }
        },
        error: function(e) {
            //$("#err").html(e.responseText);
			alert(e.responseText);
            console.log(e);
        }
    });

});

// 의미 출력
const dispData = (rows) => {
    var tag = $("#div1").html();
    $("#div1").empty();
    $.each(rows, function(index, item) {
        tag1 = tag.replaceAll("HEADING_ID", "header" + item.id);
        tag1 = tag1.replaceAll("COLLAPSE_ID", "collapse" + item.id);
        tag1 = tag1.replaceAll("TOGGLE_ID", item.id);
        tag1 = tag1.replaceAll("UPD_DATE" , item.updDate);
        tag1 = tag1.replaceAll("MEANING"  , item.meaning);
        if(index == 0) {
            tag1 = tag1.replaceAll("CLASS_SHOW", "show");
            tag1 = tag1.replaceAll("CLASS_COLLAPSED", "");
        } else {
            tag1 = tag1.replaceAll("CLASS_SHOW", "");
            tag1 = tag1.replaceAll("CLASS_COLLAPSED", "collapsed");
        }
        console.log(tag1);
        $("#div1").append(tag1);
    });
    $("#main").show();
}

// 데이터를 가져온후
const afterGetDetail = (res) => {
    if(res.status == 200) {
        data = res.data;
        console.log(data);

        //$("#username").text(data.username);

        $("#word_id").val(data.words[0].id);

        $("#word"    ).text(data.words[0].word    );
        $("#word1"   ).val (data.words[0].word    );

        $("#access"  ).text(data.words[0].access  );

        $("#meaning1").text(data.words[0].meaning1);
        $("#meaning11").val(data.words[0].meaning1);

        const holder = document.getElementById("div10");

        if(search_type == "MY") {
            holder.innerHTML = "";
            const wrapper = document.createElement("div");
            wrapper.innerHTML = [
                `<div class="mb-2 form-group form-check">`,
                `    <label class="form-check-label" for="share">공개</label>`,
                `    <input type="checkbox" class="form-check-input" name="share" id="share" checked>`,
                `</div>`
            ].join('');
            holder.append(wrapper);
            if(data.words[0].seq == -1) {
                $("#share").prop("checked", true);
            } else {
                $("#share").prop("checked", false);
            }
        } else {
            holder.innerHTML = "";
        }

        dispData(data.wordMeanings);

        //$("#search").hide();
    } else {
        alert(res.message);
    }
}

$(document).on("click", "#btn_save1", function() {
    /*
    if($("input").first().val() == "") {
        $("#msg").text("단어는 필수!!").show().fadeOut(3000);
    }
    */
    var form_data = {};
    form_data.word = $("#word1").val();
    form_data.meaning1 = $("#meaning11").val();
    form_data.seq = $("#share").is(":checked") ? -1 : 0;

    console.log(form_data);
    var word_id = $("#word_id").val();
    var loc = `/data/save/${word_id}`;

    console.log(loc);

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: loc,
        data: JSON.stringify(form_data),
        dataType: "json",
        beforeSend: function(jqXHR, settings) {
            var header = $("meta[name='_csrf_header']").attr("content");
            var token = $("meta[name='_csrf']").attr("content");
            jqXHR.setRequestHeader(header, token);
        },
        success: function(res) {
            if(res.status == 200) {
                console.log(data);
                data = res.data;
                /*
                $("#main").load("/cont1.html", function(responseTxt, statusTxt, xhr) {
                    if(statusTxt == "success") {
                        //$.getData(loc, afterGetData);
                        $("#search").show();
                        $("#btn1").trigger("click");
                    }
                });
                */
                detail(word_id);
            } else {
                console.log(res);
                alert(res.message);
            }
        },
        error: function(e) {
            console.log(e.responseText);
            alert("Request Error!");
        }
    });
});
