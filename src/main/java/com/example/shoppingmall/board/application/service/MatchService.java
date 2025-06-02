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
        .orElseThrow(() -> new RuntimeException("유저가 존재하지 않습니다."));

    final Match match = createMatchRequest.toMatchingPost(member);

    return matchRepository.save(match);
  }

  @Transactional
  public void deleteMatch(Long memberId, DeleteMatchRequest matchRequest) {
    final Match match = matchRepository.findById(matchRequest.matchId())
        .orElseThrow(() -> new RuntimeException("매치를 찾을 수 없습니다."));

    validateMatchOwnership(match, memberId);

    matchRepository.delete(match);
  }

  @Transactional
  public void updateMatch(Long memberId, Long matchId, UpdateMatchRequest updateMatchRequest) {
    final Match match = matchRepository.findById(matchId)
        .orElseThrow(() -> new RuntimeException("매치를 찾을 수 없습니다."));

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
        .orElseThrow(() -> new RuntimeException("게시물이 존재하지 않습니다."));
    return MatchResponse.from(match);
  }

  private void validateMatchOwnership(final Match match, final Long memberId) {
    if (!match.isOwnedBy(memberId)) {
      throw new RuntimeException("작성자가 아니기 때문에 관리할 수 없습니다.");
    }
  }
}
