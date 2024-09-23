package com.example.movie.service;

import com.example.movie.config.PrincipalDetails;
import com.example.movie.constant.UserRole;
import com.example.movie.dto.UserDto;
import com.example.movie.entity.User;
import com.example.movie.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserService { //고객관리

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    @PersistenceContext
    EntityManager em;

    private final UserRepository userRepository;
    private final SeatService seatService;
    private final TicketService ticketService;
    private final BoardService boardService;

    public UserService(UserRepository userRepository, SeatService seatService, TicketService ticketService, BoardService boardService) {
        this.userRepository = userRepository;
        this.seatService = seatService;
        this.ticketService = ticketService;
        this.boardService = boardService;
    }

    public List<UserDto> findAll() {
        List<UserDto> userDtoList = new ArrayList<>();
        return userRepository.findAll()
                .stream()
                .map(x -> UserDto.fromEntity(x))
                .toList();
    }


    public UserDto getOneUserDto(Long userNo) {
        User user =  getOneUser(userNo);
        return UserDto.fromEntity(user);
    }

    public User getOneUser(Long userNo) {
        return userRepository.findById(userNo).orElse(null);
    }


    //로그인한 '유저'의 정보를 가져오는 로직
    public UserDto loginUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            // 또는 PrincipalDetails로 형변환 후 사용자 정보 가져오기
            PrincipalDetails userDetails = (PrincipalDetails) authentication.getPrincipal();
            User user = userDetails.getUser();
            Long userNo = user.getUserNo();
            return getOneUserDto(userNo);
        } else {
            return null;
        }
    }

    //예매를 할 때 유저의 나이를 체크하기 위함이지만 '유저'의 상태 정보와 관련된 로직이라 유저서비스에 배치

    //하나의 함수에 하나의 기능만을 생각해서 나이체크하는 기능과 나이를 계산하는 기능을 분리
    public int userAgeCheck() {
        UserDto userDto = loginUserInfo();
        LocalDate birthDate = LocalDate.parse(userDto.getBirth());
        return calculateAge(birthDate);
    }

    public int calculateAge(LocalDate birthDate) {
        LocalDate today = LocalDate.now();
        return Period.between(birthDate, today).getYears();
    }


    public void update(UserDto userDto) {

        String encodedInputPassword = passwordEncoder.encode(userDto.getPassword1());
        User user = User.builder()
                .userNo(userDto.getUserNo())
                .userId(userDto.getUserId())
                .userName(userDto.getUserName())
                .password(encodedInputPassword)
                .birth(userDto.getBirth())
                .phone(userDto.getPhone())
                .email(userDto.getEmail())
                .money(userDto.getMoney())
                .userRole(userDto.getUserRole())
                .build();
        userRepository.save(user);
    }

    @Transactional
    public void createUser(UserDto userDto) {
        User user = new User();
        user.setUserId(userDto.getUserId());
        user.setPassword(passwordEncoder.encode(userDto.getPassword1()));
        user.setUserName(userDto.getUserName());
        user.setBirth(userDto.getBirth());
        user.setPhone(userDto.getPhone());
        user.setEmail(userDto.getEmail());
        if (userDto.getEmail().toLowerCase().contains("@ezen")) {
            user.setUserRole(UserRole.ADMIN);
        } else {
            user.setUserRole(UserRole.USER);
        }
        em.persist(user);
    }


//    @Transactional
//    public void delete(Long userNo) {
//        User user = em.find(User.class, userNo);
//
//        String sql1 = "SELECT b FROM Board b WHERE b.user.userNo=:userNo";
//        TypedQuery<Board> query1 = em.createQuery(sql1, Board.class)
//                .setParameter("userNo", userNo);
//        List<Board> boardList1 = query1.getResultList();
//        for (Board board : boardList1) {
//            board.setUser(null);
//        }
//        String sql2 = "SELECT t FROM Ticket t WHERE t.user.userNo=:userNo";
//        TypedQuery<Ticket> query2 = em.createQuery(sql2, Ticket.class)
//                .setParameter("userNo", userNo);
//        List<Ticket> boardList2 = query2.getResultList();
//        for (Ticket ticket : boardList2) {
//            ticket.setUser(null);
//        }
//        em.remove(user);
//    }

    @Transactional
    public void deleteUser(Long userNo) {
        User user = em.find(User.class, userNo);

        // 1. 사용자가 예약한 좌석 정보 삭제
        seatService.deleteSeatByUsers(user);
        // 2. 사용자가 예약한 티켓 정보 삭제
        ticketService.deleteTicketByUser(user);
        // 3. 사용자가 작성한 게시물 삭제
        boardService.deleteBoardByUser(user);
        // 4. 사용자 정보 삭제
        em.remove(user);
    }



    @Transactional
    public boolean checkId(String userId) {
        Long count = chechIdCount(userId);

        log.info(String.valueOf(count));
        if (count > 0) {
            System.out.println("이미 존재하는 아이디입니다");
            return true;
        } else {
            System.out.println("사용가능한 아이디입니다.");
            return false;
        }
    }

    private Long chechIdCount(String userId) {
        String sql = "SELECT COUNT(u) FROM User u WHERE u.userId = :userId";
        return em.createQuery(sql, Long.class)
                .setParameter("userId", userId)
                .getSingleResult();
    }

    public void money(UserDto dto, int money) {
        // 기존 사용자 정보 조회
        User user = getOneUser(dto.getUserNo());
        // 기존 금액에 추가할 금액을 더함
        int newMoney = user.getMoney() + money;
        // 엔티티에 새로운 금액 설정
        user.setMoney(newMoney);
        // 엔티티 저장
        userRepository.save(user);
    }

    @Transactional
    public void leftMoney(Long userNo, int leftMoney) {
        User user = getOneUser(userNo);
        user.setMoney(leftMoney);
        em.persist(user);
    }
}
