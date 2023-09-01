package com.appsdeveloperblog.ws.clents.photoappwebclient.controller;

import com.appsdeveloperblog.ws.clents.photoappwebclient.response.AlbumRest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AlbumsController {

    private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;
    private final RestTemplate restTemplate;
    private  final WebClient webClient;

    @GetMapping("/albums")
    public String getAlbums(Model model, @AuthenticationPrincipal OidcUser principal) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2AuthorizedClient oauth2Client = oAuth2AuthorizedClientService.loadAuthorizedClient(oauthToken.getAuthorizedClientRegistrationId(),
                oauthToken.getName());
        String jwtAccessToken = oauth2Client.getAccessToken().getTokenValue();
        System.out.println("jwtAccessToken + " + jwtAccessToken);


        System.out.println("Principal = " + principal);
        String tokenValue = principal.getIdToken().getTokenValue();
        System.out.println("idTokenValue = " + tokenValue);

//        AlbumRest album1 = new AlbumRest();
//        album1.setAlbumId("albumOne");
//        album1.setAlbumTitle("Album one Title");
//        album1.setAlbumUrl("http://localhost:8082/albums/1");
//
//        AlbumRest album2 = new AlbumRest();
//        album1.setAlbumId("albumTwo");
//        album1.setAlbumTitle("Album two Title");
//        album1.setAlbumUrl("http://localhost:8082/albums/2");
//        model.addAttribute("albums", Arrays.asList(album1,album2));

        String url = "http://localhost:8082/albums";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + jwtAccessToken);
        HttpEntity<List<AlbumRest>> entity = new HttpEntity<>(httpHeaders);
        ResponseEntity<List<AlbumRest>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, new ParameterizedTypeReference<List<AlbumRest>>() {}) ;
        List<AlbumRest> albums = responseEntity.getBody();
        model.addAttribute("albums", albums);
        return "albums";
    }

    @GetMapping("/albums-web-client")
    public String getAlbumsWebClientExample(Model model,
                            @AuthenticationPrincipal OidcUser principal) {


        String url = "http://localhost:8082/albums";

        List<AlbumRest> albums = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<AlbumRest>>(){})
                .block();

        model.addAttribute("albums", albums);
        return "albums";
    }

}
