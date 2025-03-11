package ru.practicum.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.compilations.CreateCompilationRs;
import ru.practicum.mappers.CompilationMapper;
import ru.practicum.models.Compilation;
import ru.practicum.services.CompilationsService;

import java.util.List;

@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
public class PublicCompilationsController {
    private final CompilationsService service;
    private final CompilationMapper mapper;

    @GetMapping
    public ResponseEntity<List<CreateCompilationRs>> getAll(@RequestParam(required = false) Boolean pinned,
                                                            @RequestParam(defaultValue = "0") Integer from,
                                                            @RequestParam(defaultValue = "10") Integer size) {

        List<Compilation> compilations = service.getByFilter(pinned, from, size);
        return new ResponseEntity<>(mapper.toDto(compilations), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CreateCompilationRs> getById(@PathVariable Long id) {
        Compilation compilation = service.getById(id);
        return new ResponseEntity<>(mapper.toDto(compilation), HttpStatus.OK);

    }

}
