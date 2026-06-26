package com.example.lab3.services;

import com.example.lab3.domain.dto.request.CreateSpecimenRequest;
import com.example.lab3.domain.dto.request.UpdateSpecimenRequest;
import com.example.lab3.domain.dto.response.PageableResponse;
import com.example.lab3.domain.dto.response.SpecimenResponse;

import java.util.List;
import java.util.UUID;

public interface SpecimenService {
    SpecimenResponse createSpecimen(CreateSpecimenRequest request);
    PageableResponse<SpecimenResponse> getAllSpecimens(int page, int size, String sortBy, String sortOrder);
    SpecimenResponse getSpecimenById(UUID id);
    SpecimenResponse updateSpecimen(UUID id, UpdateSpecimenRequest request);
    public SpecimenResponse deleteSpecimen(UUID id);

}
