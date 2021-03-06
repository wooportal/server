package de.codeschluss.wooportal.server.core.security;

import static org.springframework.http.ResponseEntity.ok;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.codeschluss.wooportal.server.core.security.services.JwtTokenService;
import de.codeschluss.wooportal.server.core.security.services.JwtUserDetailsService;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// TODO: Auto-generated Javadoc
/**
 * The Class SecurityController.
 * 
 * @author Valmir Etemi
 *
 */
@RestController
public class SecurityController {
  
  /** The token service. */
  private final JwtTokenService tokenService;
  
  /** The user detail service. */
  private final JwtUserDetailsService userDetailService;
  
  /**
   * Instantiates a new security controller.
   *
   * @param tokenService the token service
   * @param userDetailService the user detail service
   */
  public SecurityController(
      JwtTokenService tokenService,
      JwtUserDetailsService userDetailService) {
    this.tokenService = tokenService;
    this.userDetailService = userDetailService;
  }
  
  /**
   * Refresh token.
   *
   * @param req the req
   * @return the response entity
   * @throws Exception the exception
   */
  @GetMapping("/refresh")
  public ResponseEntity<String> refreshToken(HttpServletRequest req) throws Exception {
    String refreshToken = req.getHeader("Authorization");
    tokenService.verifyRefresh(refreshToken);

    String username = tokenService.extractUsername(refreshToken);
    String accessToken = tokenService
        .createAccessToken(userDetailService.loadUserByUsername(username));

    ObjectMapper objectMapper = new ObjectMapper();
    Map<String, String> tokenMap = new HashMap<String, String>();
    tokenMap.put("access", accessToken);
    
    return ok(objectMapper.writeValueAsString(tokenMap));
  }

}
