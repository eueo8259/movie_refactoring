function insert() {
    var movieTitle = document.getElementById("movieTitle").value.trim();
    var movieDate = document.getElementById("movieDate").value.trim();
    var movieRate = document.getElementById("movieRate").value;
    var movieImg = document.getElementById("img").value.trim();
    var price = document.getElementById("moviePrice").value.trim();


    if (movieTitle.length == 0) {
        alert("영화 제목이 입력되지 않았습니다.");
        document.getElementById("movieTitle").focus();
        return false;
    }
    if (movieDate.length == 0) {
        alert("영화 개봉일이 입력되지 않았습니다.");
        document.getElementById("movieDate").focus();
        return false;
    }
    if (movieRate.length == 0) {
            alert("영화 등급이 입력되지 않았습니다.");
            document.getElementById("movieRate").focus();
            return false;
    }

        var movieDate = {
            movieTitle: movieTitle,
            movieDate: movieDate,
            movieRate: movieRate,
            price: price,
            movieImg: movieImg
        };

        axios.post('/api/movie/insert', movieDate)
            .then(function(response) {
            alert(response.data);
            window.location.href = '/admin';
            })
            .catch(function(error) {
                alert("오류가 발생했습니다.");
                console.error(error);
            });

}

function update() {
    var movieTitle = document.getElementById("movieTitle").value.trim();
    var movieDate = document.getElementById("movieDate").value.trim();
    var movieRate = document.getElementById("movieRate").value;
    var movieNo = document.getElementById("movieNo").value.trim();
    var movieImg = document.getElementById("movieImg").value.trim();
    var goodPointAvg = document.getElementById("goodPointAvg").value.trim();
    var price = document.getElementById("moviePrice").value.trim();


    if (movieTitle.length == 0) {
        alert("영화 제목이 입력되지 않았습니다.");
        document.getElementById("movieTitle").focus();
        return false;
    }
    if (movieDate.length == 0) {
        alert("영화 개봉일이 입력되지 않았습니다.");
        document.getElementById("movieDate").focus();
        return false;
    }
    if (movieRate.length == 0) {
            alert("영화 등급이 입력되지 않았습니다.");
            document.getElementById("movieRate").focus();
            return false;
    }

        var movieDate = {
            movieTitle: movieTitle,
            movieDate: movieDate,
            movieRate: movieRate,
            movieNo: movieNo,
            movieImg: movieImg,
            goodPointAvg: goodPointAvg,
            price : price
        };

        axios.post('/api/movie/update', movieDate)
            .then(function(response) {
            alert(response.data);
            window.location.href = '/admin';
            })
            .catch(function(error) {
                alert("오류가 발생했습니다.");
                console.error(error);
            });

}

function deleteMovie(movieNo) {

    var deleteData = {
        deleteId: movieNo
    };
    if (confirm("정말로 이 영화를 삭제하시겠습니까?")) {
        console.log(deleteData);
        axios.delete('/api/movie/delete', { data: deleteData })  // data 속성을 포함한 config 객체를 전달
            .then(function(response) {
                alert("영화 정보 삭제가 완료되었습니다.");
                window.location.reload(); // 페이지 새로고침
            })
            .catch(function(error) {
                alert("영화 정보 삭제 중 오류가 발생했습니다.");
                console.error(error);
            });
    }
}


