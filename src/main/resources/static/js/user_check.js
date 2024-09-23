function signup() {
    var userId = document.getElementById("userId").value.trim();
    var userName = document.getElementById("userName").value.trim();
    var password1 = document.getElementById("password1").value;
    var password2 = document.getElementById("password2").value;
    var birth = document.getElementById("birth").value.trim();
    var phone = document.getElementById("phone").value.trim();
    var email = document.getElementById("email").value.trim();

    if (userId.length == 0) {
        alert("아이디가 입력되지 않았습니다.");
        document.getElementById("userId").focus();
        return false;
    }
    if (userName.length == 0) {
        alert("이름이 입력되지 않았습니다.");
        document.getElementById("userName").focus();
        return false;
    }
    if (password1.length == 0) {
        alert("비밀번호가 입력되지 않았습니다.");
        document.getElementById("password1").focus();
        return false;
    }
    if (password2.length == 0) {
        alert("비밀번호 확인이 입력되지 않았습니다.");
        document.getElementById("password2").focus();
        return false;
    }
    if (password1 !== password2) {
        alert("비밀번호가 일치하지 않습니다.");
        document.getElementById("password2").focus();
        return false;
    }
    if (birth.length == 0) {
        alert("생일이 입력되지 않았습니다.");
        document.getElementById("birth").focus();
        return false;
    }
    if (phone.length == 0) {
        alert("전화번호가 입력되지 않았습니다.");
        document.getElementById("phone").focus();
        return false;
    }
    if (!/^\d+$/.test(phone)) {
        alert("전화번호는 숫자만 입력해주세요.");
        document.getElementById("phone").focus();
        return false;
    }
    if (email.length == 0) {
        alert("이메일이 입력되지 않았습니다.");
        document.getElementById("email").focus();
        return false;
    }
    if (!/^.+@.+\..+$/.test(email)) {
        alert("올바른 이메일 형식이 아닙니다.");
        document.getElementById("email").focus();
        return false;
    }
    console.log("버튼은 눌림")
    var userData = {
        userId: userId,
        userName: userName,
        password1: password1,
        password2: password2,
        birth: birth,
        phone: phone,
        email: email
    };

    axios.post('/api/user/signup', userData)
        .then(function(response) {
            if (response.status === 201) {
                alert("회원가입 성공");
                window.location.href = '/cinema';
            }
        })
        .catch(function(error) {
            if (error.response) {
                // 서버로부터 받은 오류 메시지 처리
                alert(JSON.stringify(error.response.data));
            } else {
                alert("알 수 없는 오류가 발생했습니다.");
            }
        });
}


function update() {
    var userId = document.getElementById("userId").value.trim();
    var userNo = document.getElementById("userNo").value.trim();
    var userName = document.getElementById("userName").value.trim();
    var password1 = document.getElementById("password1").value;
    var password2 = document.getElementById("password2").value;
    var birth = document.getElementById("birth").value.trim();
    var phone = document.getElementById("phone").value.trim();
    var email = document.getElementById("email").value.trim();
    var userRole = document.getElementById("userRole").value.trim();
    var money = document.getElementById("money").value.trim();

    if (userId.length == 0) {
        alert("아이디가 입력되지 않았습니다.");
        document.getElementById("userId").focus();
        return false;
    }
    if (userName.length == 0) {
        alert("이름이 입력되지 않았습니다.");
        document.getElementById("userName").focus();
        return false;
    }
    if (password1.length == 0) {
        alert("비밀번호가 입력되지 않았습니다.");
        document.getElementById("password1").focus();
        return false;
    }
    if (password2.length == 0) {
        alert("비밀번호 확인이 입력되지 않았습니다.");
        document.getElementById("password2").focus();
        return false;
    }
    if (password1 !== password2) {
        alert("비밀번호가 일치하지 않습니다.");
        document.getElementById("password2").focus();
        return false;
    }
    if (birth.length == 0) {
        alert("생일이 입력되지 않았습니다.");
        document.getElementById("birth").focus();
        return false;
    }
    if (phone.length == 0) {
        alert("전화번호가 입력되지 않았습니다.");
        document.getElementById("phone").focus();
        return false;
    }
    if (!/^\d+$/.test(phone)) {
        alert("전화번호는 숫자만 입력해주세요.");
        document.getElementById("phone").focus();
        return false;
    }
    if (email.length == 0) {
        alert("이메일이 입력되지 않았습니다.");
        document.getElementById("email").focus();
        return false;
    }
    if (!/^.+@.+\..+$/.test(email)) {
        alert("올바른 이메일 형식이 아닙니다.");
        document.getElementById("email").focus();
        return false;
    }
    var userData = {
        userNo: userNo,
        userId: userId,
        userName: userName,
        password1: password1,
        birth: birth,
        phone: phone,
        email: email,
        userRole: userRole,
        money: money
    };

    axios.post('/api/user/update', userData)
        .then(function(response) {
        alert(response.data);
        window.location.href = '/admin';
        })
        .catch(function(error) {
            alert("회원 정보 수정 중 오류가 발생했습니다.");
            console.error(error);
        });

}

function res(){
    alert("처음부터 다시 입력합니다.")
    document.getElementById("frm").reset();
    document.getElementById("userId").focus();
}


function captureUserId() {
    var checkUser = new URLSearchParams(window.location.search).get('checkUser');

    if (checkUser) {
        console.log("사용자 ID 값:", checkUser);
        document.getElementById("userIdForSignUp").value = checkUser;
    } else {
        alert("사용자 ID를 입력하세요.");
    }
}

function deleteUser(userNo) {

    var deleteData = {
        deleteId: userNo
    };
    if (confirm("정말로 이 사용자를 삭제하시겠습니까?")) {
        console.log(deleteData);
        axios.delete('/api/admin/delete', { data: deleteData })  // data 속성을 포함한 config 객체를 전달
            .then(function(response) {
                alert("회원 정보 삭제가 완료되었습니다.");
                window.location.reload(); // 페이지 새로고침
            })
            .catch(function(error) {
                alert("회원 정보 삭제 중 오류가 발생했습니다.");
                console.error(error);
            });
    }
}

function validatePassword() {
     var userNo = document.getElementById('userNo').value;
     var inputPassword = document.getElementById('inputPassword').value;

     var deleteData = {
         userNo: userNo,
         password: inputPassword
     };

    console.log(deleteData);
    axios.delete('/api/user/delete', { data: deleteData })
        .then(function(response) {
            console.log('Response:', response);
            alert(response.data);
            window.location.href = '/cinema';
        })
        .catch(function(error) {
            console.error('Error details:', error);
            if (error.response) {
                console.log('Response data:', error.response.data);
                if (error.response.status === 403) {
                    alert("비밀번호가 틀렸습니다.");
                } else {
                    alert("오류 발생: " + error.response.data);
                }
            } else {
                alert("알 수 없는 오류가 발생했습니다.");
            }
        });
 }

