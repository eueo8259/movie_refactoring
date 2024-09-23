$(function() {
      $("#btn_show").click(function() {
        startInfiniteLoop();
      });

      function startInfiniteLoop() {
        // 무한 반복 함수
        function loop() {
          var $originalCard = $("#original-card");
          var $clone = $originalCard.clone();
          $clone.removeAttr('id'); // clone에서 id 제거
          $("#show").append($clone);
          setTimeout(loop, 1000); // 1초마다 반복
        }

        loop(); // 반복 시작
      }
    });