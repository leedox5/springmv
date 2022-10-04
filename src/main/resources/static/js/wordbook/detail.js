$.chkDetail = function(id) {
    console.log("Hello, this is detail.js! id==>" + id);
    $("#div10").hide();
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
            $("#search").show();
            $("#btn1").trigger("click");
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
        console.log(tag1);
        $("#div1").append(tag1);
    });
}

// 데이터를 가져온후
const afterGetDetail = (res) => {
    if(res.status == 200) {
        data = res.data;
        console.log(data);

        $("#username").text(data.username);

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

        $("#search").hide();
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
