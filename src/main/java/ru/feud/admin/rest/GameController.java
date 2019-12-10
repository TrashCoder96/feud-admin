package ru.feud.admin.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.feud.admin.rest.ro.CreateRequest;
import ru.feud.admin.rest.ro.ListResponse;
import ru.feud.admin.rest.ro.OneGameResponse;
import ru.feud.admin.rest.ro.UpdateRequest;
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

    @DeleteMapping(value = "/game/{id}")
    public void delete(@PathVariable(required = true) Long id) {
        gameService.deleteGame(id);
    }

    @GetMapping(value = "/game/{id}")
    public OneGameResponse get(@PathVariable(required = true) Long id) {
        return new OneGameResponse().setGame(gameService.getGame(id));
    }

    @GetMapping(value = "/games")
    public ListResponse list() {
        return new ListResponse().setGames(gameService.list());
    }

    @GetMapping(value = "/game/key/{key}")
    public OneGameResponse getBykey(@PathVariable(required = true) String key) {
        return new OneGameResponse().setGame(gameService.getGameByKey(key));
    }

}
