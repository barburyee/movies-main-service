package com.example.moviesmainservice.controllers;

import com.example.moviesmainservice.model.CatalogItem;
import com.example.moviesmainservice.model.MovieItem;
import com.example.moviesmainservice.model.Rating;
import com.example.moviesmainservice.model.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/api/catalog/")
public class MovieServiceResource {
    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("{userId}")
    public List<CatalogItem> getCatalogResourcesMovies(@PathVariable("userId") String userId) {
        //Get all rated Movie IDs
        UserRating userRating = restTemplate.getForObject("http://localhost:8082/api/rating/users/" + userId, UserRating.class);

        return userRating.getUserRating().stream().map(rating -> {
                    //foreach movie IDs, call movie info and get its details
                    MovieItem movieItem = restTemplate.getForObject("http://localhost:8081/api/info/" + rating.getMovieId(), MovieItem.class);
                    //Put them all together
                    return new CatalogItem(movieItem.getMovieId(), "Making Ways Imposible", rating.getRatings());
                })
                .collect(Collectors.toList());

    }

    /*List<Rating> ratings = Arrays.asList(
                new Rating("1234", 4),
                new Rating("5678", 3)
        );*/

    //        return ratings.stream().map(rating -> {
    /*MovieItem movieItem = webClientBuilder.build()
                            .get()
                            .uri("http://localhost:8081/api/info/1" )
                            .retrieve()
                            .bodyToMono(MovieItem.class)
                            .block();*/
    /*return Collections.singletonList(
                new CatalogItem("Impossible Ways","Making Ways Imposible", 6)
        );*/

}
