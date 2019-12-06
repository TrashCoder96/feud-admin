package ru.feud.admin.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.feud.admin.rest.ro.*;
import ru.feud.admin.service.GameService;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/v1")
public class GameController {

    @Autowired
    private GameService gameService;

    @PostMapping(value = "/game")
    public OneGameResponse create(@RequestBody @Valid CreateRequest request) {
        return new OneGameResponse()
                .setGame(gameService.createGame(request.getGame()));
    }

    @PutMapping(value = "/game")
    public void update(@RequestBody @Valid UpdateRequest request) {
        gameService.updateGame(request.getGame());
    }

    @DeleteMapping(value = "/delete")
    public void delete(@RequestBody @Valid DeleteRequest request) {
        gameService.deleteGame(request.getId());
    }

    @GetMapping(value = "/game")
    public OneGameResponse get(@RequestBody @Valid GetRequest request) {
        return new OneGameResponse().setGame(gameService.getGame(request.getId()));
    }

    @GetMapping(value = "/games")
    public ListResponse list() {
        return new ListResponse().setGames(gameService.list());
    }

    @GetMapping(value = "/game/{key}")
    public OneGameResponse getBykey(@PathVariable(required = true) String key) {
        return new OneGameResponse().setGame(gameService.getGameByKey(key));
    }

}
