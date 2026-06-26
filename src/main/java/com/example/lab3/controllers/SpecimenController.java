package com.example.lab3.controllers;

import com.example.lab3.domain.dto.request.CreateSpecimenRequest;
import com.example.lab3.domain.dto.request.UpdateSpecimenRequest;
import com.example.lab3.domain.dto.response.GeneralResponse;
import com.example.lab3.services.impl.SpecimenServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("api/specimen")
@RequiredArgsConstructor
public class SpecimenController {
    private final SpecimenServiceImpl specimenService;

    @PostMapping("/create")
    public ResponseEntity<GeneralResponse> createSpecimen(@RequestBody CreateSpecimenRequest specimen){
        return buildResponse(
                "Specimen created successfully",
                HttpStatus.CREATED,
                specimenService.createSpecimen(specimen)
        );
    }


    @GetMapping("/getAll")
    public ResponseEntity<GeneralResponse> getAllSpecimens(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder
    ){
        return buildResponse(
                "Specimens found",
                HttpStatus.OK,
                specimenService.getAllSpecimens(page, size, sortBy, sortOrder)
        );
    }

    @GetMapping("getBy/{id}")
    public ResponseEntity<GeneralResponse> getById(@PathVariable UUID id){
        return buildResponse(
                "Specimen found",
                HttpStatus.OK,
                specimenService.getSpecimenById(id)
        );
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<GeneralResponse> updateSpecimen(@RequestBody UpdateSpecimenRequest specimen, @PathVariable UUID id){
        specimenService.updateSpecimen(id, specimen);
        return buildResponse(
                "Specimen updated successfully",
                HttpStatus.OK,
                null
        );
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GeneralResponse> deleteSpecimen(@PathVariable UUID id){
        return buildResponse(
                "Specimen deleted successfully",
                HttpStatus.OK,
                specimenService.deleteSpecimen(id)
        );
    }

    public ResponseEntity<GeneralResponse> buildResponse(String message, HttpStatus status, Object data){
            String uri = ServletUriComponentsBuilder.fromCurrentRequestUri().build().getPath(); //devolver path que consulte
            return ResponseEntity.status(status)
                    .body(GeneralResponse.builder()
                            .uri(uri)
                            .message(message)
                            .status(status.value())
                            .time(LocalDateTime.now())
                            .data(data)
                            .build());
        }

}
