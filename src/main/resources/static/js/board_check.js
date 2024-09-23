function insert() {
    // 작성자
    var username = document.getElementById("username").value.trim();
    if (username.length === 0) {
        alert("작성자를 입력하세요.");
        return false;
    }

    // 영화제목
    var movieNo = document.getElementById("movieNo").value.trim();
    if (movieNo.length === 0) {
        alert("영화를 선택하세요.");
        return false;
    }

    // 제목
    var title = document.getElementById("title").value.trim();
    if (title.length === 0) {
        alert("제목을 입력하세요.");
        return false;
    }

    // 내용
    var content = document.getElementById("content").value.trim();
    if (content.length === 0) {
        alert("내용을 입력하세요.");
        return false;
    }

    // 별점
    var goodPoint = document.getElementById("goodPoint").value.trim();
    if (goodPoint.length === 0) {
        alert("별점을 선택하세요.");
        return false;
    }

        var data = {
            movieNo : movieNo,
            title : title,
            content : content,
            goodPoint : goodPoint
        };
         axios.post('/api/board/insert', data)
             .then(function(response) {
             window.location.href = '/board/list';
             })
             .catch(function(error) {
                 alert("오류가 발생했습니다.");
                 console.error(error);
             });

}

function update() {
    var title = document.getElementById('title').value;
    var content = document.getElementById('content').value;
    var movieNo = document.getElementById('movieNo').value;
    var goodPoint = document.getElementById('goodPoint').value;
    var boardId = document.getElementById('boardId').value;

    if (title.trim() === '' || content.trim() === '') {
        alert('제목과 내용은 필수 입력 항목입니다.');
        return false;
    }
        var data = {
            movieNo : movieNo,
            title : title,
            content : content,
            goodPoint : goodPoint,
            boardId : boardId
        };
         axios.post('/api/board/update', data)
             .then(function(response) {
             window.location.href = '/board/list';
             })
             .catch(function(error) {
                 alert("오류가 발생했습니다.");
                 console.error(error);
             });
}

function deleteMovie(boardId) {

    var deleteData = {
        deleteId: boardId
    };
    if (confirm("정말로 이 게시글을 삭제하시겠습니까?")) {
        console.log(deleteData);
        axios.delete('/api/board/delete', { data: deleteData })  // data 속성을 포함한 config 객체를 전달
            .then(function(response) {
                alert("게시글 삭제가 완료되었습니다.");
                window.location.reload(); // 페이지 새로고침
            })
            .catch(function(error) {
                alert("영화 정보 삭제 중 오류가 발생했습니다.");
                console.error(error);
            });
    }
}



