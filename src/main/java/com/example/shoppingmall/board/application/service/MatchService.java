package com.example.shoppingmall.board.application.service;

import com.example.shoppingmall.auth.domain.member.Member;
import com.example.shoppingmall.auth.domain.member.MemberRepository;
import com.example.shoppingmall.board.application.dto.CreateMatchRequest;
import com.example.shoppingmall.board.application.dto.DeleteMatchRequest;
import com.example.shoppingmall.board.application.dto.FindMatchByMatchIdRequest;
import com.example.shoppingmall.board.application.dto.MatchResponse;
import com.example.shoppingmall.board.application.dto.UpdateMatchRequest;
import com.example.shoppingmall.board.domain.match.Match;
import com.example.shoppingmall.board.domain.match.MatchRepository;
import java.util.List;

import com.example.shoppingmall.core.exception.AuthorizationException;
import com.example.shoppingmall.core.exception.BoardException;
import com.example.shoppingmall.core.exception.CustomErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MatchService {

  private final MatchRepository matchRepository;
  private final MemberRepository memberRepository;

  public MatchService(final MatchRepository matchRepository,
      final MemberRepository memberRepository) {
    this.matchRepository = matchRepository;
    this.memberRepository = memberRepository;
  }

  @Transactional
  public Match createMatch(Long memberId, CreateMatchRequest createMatchRequest) {
    final Member member = memberRepository.findById(memberId)
        .orElseThrow(() -> new AuthorizationException(CustomErrorCode.USER_NOT_FOUND));

    final Match match = createMatchRequest.toMatchingPost(member);

    return matchRepository.save(match);
  }

  @Transactional
  public void deleteMatch(Long memberId, DeleteMatchRequest matchRequest) {
    final Match match = matchRepository.findById(matchRequest.matchId())
        .orElseThrow(() -> new BoardException(CustomErrorCode.POST_NOT_FOUND));

    validateMatchOwnership(match, memberId);

    matchRepository.delete(match);
  }

  @Transactional
  public void updateMatch(Long memberId, Long matchId, UpdateMatchRequest updateMatchRequest) {
    final Match match = matchRepository.findById(matchId)
        .orElseThrow(() -> new BoardException(CustomErrorCode.POST_NOT_FOUND));

    validateMatchOwnership(match, memberId);

    match.updateMatchingPost(updateMatchRequest);
  }

  @Transactional(readOnly = true)
  public List<MatchResponse> findAllMatches() {
    final List<Match> matches = matchRepository.findAllWithMember();
    return matches.stream().map(MatchResponse::from).toList();
  }

  @Transactional(readOnly = true)
  public MatchResponse findMatchById(FindMatchByMatchIdRequest findMatchByMatchId) {
    final Match match = matchRepository.findById(findMatchByMatchId.matchId())
        .orElseThrow(() -> new BoardException(CustomErrorCode.POST_NOT_FOUND));
    return MatchResponse.from(match);
  }

  private void validateMatchOwnership(final Match match, final Long memberId) {
    if (!match.isOwnedBy(memberId)) {
      throw new BoardException(CustomErrorCode.POST_DELETE_FORBIDDEN);
    }
  }
}
