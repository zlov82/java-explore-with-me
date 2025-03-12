package ru.practicum.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.compilations.CreateCompilationRq;
import ru.practicum.dto.compilations.CreateCompilationRs;
import ru.practicum.dto.compilations.UpdateCompilationRq;
import ru.practicum.mappers.CompilationMapper;
import ru.practicum.models.Compilation;
import ru.practicum.services.CompilationsService;

@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
public class AdminCompilationController {
    private final CompilationsService service;
    private final CompilationMapper mapper;

    @PostMapping
    public ResponseEntity<CreateCompilationRs> create(@Valid @RequestBody CreateCompilationRq createRq) {
        Compilation compilation = service.create(createRq);
        return new ResponseEntity<>(mapper.toDto(compilation), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CreateCompilationRs> update(@PathVariable Long id,
                                                      @Valid @RequestBody UpdateCompilationRq updateRequest) {

        Compilation compilation = service.update(id, updateRequest);
        return new ResponseEntity<>(mapper.toDto(compilation), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
