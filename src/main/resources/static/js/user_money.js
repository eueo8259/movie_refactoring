function validateAndCharge() {
    // 충전 금액 확인
    var money = document.getElementById('money').value;
    var password = document.getElementById('inputPassword').value;

    if (money === "0") {
        alert('충전할 금액을 선택해주세요.');
        return false;
    }

    var userData = {
        money: money,
        password : password
    };

    // Axios 요청
    axios.post('/api/user/money', userData)
        .then(function(response) {
            alert(response.data);
            window.location.href = '/user/money';
        })
        .catch(function(error) {
            if (error.response) {
                alert("오류 발생: " + error.response.data);
            } else {
                alert("충전 중 오류가 발생했습니다.");
            }
            console.error(error);
        });
}
