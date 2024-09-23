    function deleteTicket(boardId) {

        var deleteData = {
            deleteId: boardId
        };
        if (confirm("정말로 예매 취소 하시겠습니까??")) {
            console.log(deleteData);
            axios.delete('/api/ticket/delete', { data: deleteData })  // data 속성을 포함한 config 객체를 전달
                .then(function(response) {
                    alert("예매 취소 완료되었습니다.");
                    window.location.reload(); // 페이지 새로고침
                })
                .catch(function(error) {
                    alert("예매 취소 중 오류가 발생했습니다.");
                    console.error(error);
                });
        }
    }
