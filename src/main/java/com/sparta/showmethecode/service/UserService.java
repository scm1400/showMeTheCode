package com.sparta.showmethecode.service;

import com.sparta.showmethecode.config.security.JwtUtils;
import com.sparta.showmethecode.config.security.UserDetailsServiceImpl;
import com.sparta.showmethecode.domain.Language;
import com.sparta.showmethecode.domain.ReviewAnswer;
import com.sparta.showmethecode.domain.User;
import com.sparta.showmethecode.domain.UserRole;
import com.sparta.showmethecode.dto.request.EvaluateAnswerDto;
import com.sparta.showmethecode.dto.request.SigninRequestDto;
import com.sparta.showmethecode.dto.request.SignupRequestDto;
import com.sparta.showmethecode.dto.response.ReviewRequestResponseDto;
import com.sparta.showmethecode.dto.response.ReviewerInfoDto;
import com.sparta.showmethecode.dto.response.SigninResponseDto;
import com.sparta.showmethecode.repository.LanguageRepository;
import com.sparta.showmethecode.repository.ReviewAnswerRepository;
import com.sparta.showmethecode.repository.ReviewRequestRepository;
import com.sparta.showmethecode.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final ReviewRequestRepository reviewRequestRepository;
    private final ReviewAnswerRepository reviewAnswerRepository;

    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final LanguageRepository languageRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User saveUser(SignupRequestDto requestDto) {
        UserRole userRole = requestDto.isReviewer() ? UserRole.ROLE_REVIEWER : UserRole.ROLE_USER;

        log.info("saveUser role = {}", userRole);

        User user = User.builder()
                .username(requestDto.getUsername())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .role(userRole)
                .evalCount(0)
                .evalCount(0)
                .answerCount(0)
                .languages(new ArrayList<>())
                .build();

        User savedUser = userRepository.save(user);

        if (requestDto.getLanguages().size() > 0) {
            Set<String> languages = requestDto.getLanguages();
            for (String l : languages) {
                Language language = new Language(l);
                language.setUser(savedUser);
                languageRepository.save(language);
            }
        }


        return savedUser;
    }

    public SigninResponseDto signin(SigninRequestDto requestDto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestDto.getUsername(), requestDto.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("로그인에 실패했습니다.");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(requestDto.getUsername());
        String token = jwtUtils.createToken(userDetails.getUsername());

        String authority = userDetails.getAuthorities().stream().findFirst().get().toString();

        return new SigninResponseDto(token, authority, HttpStatus.CREATED, "로그인에 성공했습니다.");
    }

    public List<String> getMyLanguage(Long userId) {
        return languageRepository.findByUserId(userId);
    }


    public List<ReviewerInfoDto> findReviewerByLanguage(String languageName) {
        List<User> reviewers = userRepository.findReviewerByLanguage(languageName);

        return reviewers.stream().map(
                r -> new ReviewerInfoDto(
                        r.getUsername(),
                        r.getLanguages().stream().map(l -> new String(l.getName())).collect(Collectors.toList()),
                        r.getAnswerCount(),
                        r.getEvalTotal() / r.getEvalCount())
        ).collect(Collectors.toList());
    }

    public List<ReviewRequestResponseDto> getMyReviewRequestList(User user) {
        return reviewRequestRepository.findMyReviewRequestList(user.getId());
    }

    /**
     * 답변에 대한 평가 API
     *
     * 평가하고자 하는 답변이 내가 요청한 코드리뷰에 대한 답변인지 확인해야 함
     */
    @Transactional
    public void evaluateAnswer(User user, Long answerId, EvaluateAnswerDto evaluateAnswerDto) {
        if(reviewRequestRepository.isAnswerToMe(answerId, user)) {
            ReviewAnswer reviewAnswer = reviewAnswerRepository.findById(answerId).orElseThrow(
                    () -> new IllegalArgumentException("존재하지 않는 답변입니다.")
            );

            reviewAnswer.evaluate(evaluateAnswerDto.getPoint());
            reviewAnswer.getAnswerUser().evaluate(evaluateAnswerDto.getPoint());
        }
    }
}
