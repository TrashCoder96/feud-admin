package ru.feud.admin.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.feud.admin.data.GameRepository;
import ru.feud.admin.data.dto.Game;
import ru.feud.admin.rest.FeudRestException;
import ru.feud.admin.rest.ro.GameRo;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Transactional
    public GameRo createGame(GameRo game) {
        try {
            Game gameDto = new Game()
                .setBody(objectMapper.writeValueAsString(game.setId(null).setKey(null)))
                .setKey(UUID.randomUUID().toString());
            gameDto = gameRepository.save(gameDto);
            return objectMapper.readValue(gameDto.getBody(), GameRo.class)
                .setId(gameDto.getId())
                .setKey(gameDto.getKey());
        } catch (Exception e) {
            throw new FeudRestException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal error");
        }
    }

    @Transactional
    public void updateGame(GameRo game) {
        try {
            Game gameDto = gameRepository.getOne(game.getId());
            if (gameDto == null) {
                throw new FeudRestException(HttpStatus.NOT_FOUND, "Game not found");
            }
            gameDto.setBody(objectMapper.writeValueAsString(game.setId(null).setKey(null)));
            gameRepository.save(gameDto);
        } catch (FeudRestException fre) {
            throw fre;
        } catch (Exception e) {
            throw new FeudRestException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal error");
        }
    }

    @Transactional
    public void deleteGame(Long id) {
        try {
            gameRepository.findById(id).orElseThrow(() -> new FeudRestException(HttpStatus.NOT_FOUND, "Game not found"));
            gameRepository.deleteById(id);
        } catch (FeudRestException fre) {
            throw fre;
        } catch (Exception e) {
            throw new FeudRestException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal error");
        }
    }

    @Transactional
    public List<GameRo> list() {
        return gameRepository.findAll().stream().map(game -> {
            try {
                return objectMapper.readValue(game.getBody(), GameRo.class)
                    .setId(game.getId())
                    .setKey(game.getKey());
            } catch (Exception e) {
                throw new FeudRestException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal error");
            }
        }).collect(Collectors.toList());
    }

    @Transactional
    public GameRo getGame(Long id) {
        try {
            Game gameDto = gameRepository.findById(id).orElseThrow(() -> new FeudRestException(HttpStatus.NOT_FOUND, "Game not found"));
            return objectMapper.readValue(gameDto.getBody(), GameRo.class)
                .setId(gameDto.getId())
                .setKey(gameDto.getKey());
        } catch (FeudRestException fre) {
            throw fre;
        } catch (Exception e) {
            throw new FeudRestException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal error");
        }
    }

    @Transactional
    public GameRo getGameByKey(String key) {
        try {
            List<Game> gameDtos = gameRepository.findByKey(key);
            if (gameDtos.size() == 1) {
                GameRo ro = objectMapper.readValue(gameDtos.get(0).getBody(), GameRo.class);
                return ro.setKey(gameDtos.get(0).getKey()).setId(gameDtos.get(0).getId());
            } else if (gameDtos.size() == 0) {
                throw new FeudRestException(HttpStatus.NOT_FOUND, "Game not found");
            } else {
                throw new FeudRestException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal error");
            }
        } catch (FeudRestException fre) {
            throw fre;
        } catch (Exception e) {
            throw new FeudRestException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal error");
        }
    }

}
