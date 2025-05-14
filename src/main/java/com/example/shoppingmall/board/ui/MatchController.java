package com.example.shoppingmall.board.ui;

import com.example.shoppingmall.auth.application.token.Auth;
import com.example.shoppingmall.board.application.dto.CreateMatchRequest;
import com.example.shoppingmall.board.application.dto.DeleteMatchRequest;
import com.example.shoppingmall.board.application.dto.FindMatchByMatchIdRequest;
import com.example.shoppingmall.board.application.dto.MatchResponse;
import com.example.shoppingmall.board.application.service.MatchService;
import com.example.shoppingmall.board.domain.match.Match;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/match")
public class MatchController {

  private final MatchService matchService;

  public MatchController(final MatchService matchService) {
    this.matchService = matchService;
  }

  @PostMapping
  public Match createMatch(@Auth Long userId, @RequestBody final CreateMatchRequest createMatchRequest) {
    return matchService.createMatch(userId, createMatchRequest);
  }

  @GetMapping
  public List<MatchResponse> findAllMatches() {
    return matchService.findAllMatches();
  }

  @GetMapping("/{id}")
  public MatchResponse findMatch(@PathVariable Long id) {
    FindMatchByMatchIdRequest request = new FindMatchByMatchIdRequest(id);
    return matchService.findMatchById(request);
  }

  @DeleteMapping("/{id}")
  public void deleteMatch(@Auth Long userId, @PathVariable Long id) {
    DeleteMatchRequest request = new DeleteMatchRequest(id);
    matchService.deleteMatch(userId, request);
  }
}
