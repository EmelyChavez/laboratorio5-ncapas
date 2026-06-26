package com.example.lab3.services.impl;

import com.example.lab3.common.mappers.SpecimenMapper;
import com.example.lab3.domain.dto.request.CreateSpecimenRequest;
import com.example.lab3.domain.dto.request.UpdateSpecimenRequest;
import com.example.lab3.domain.dto.response.PageableResponse;
import com.example.lab3.domain.dto.response.SpecimenResponse;
import com.example.lab3.domain.entities.Specimen;
import com.example.lab3.repositories.SpecimenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpecimenServiceImplTest {

    @Mock
    private SpecimenRepository specimenRepository;

    @Mock
    private SpecimenMapper specimenMapper;

    @InjectMocks
    private SpecimenServiceImpl specimenService;

    private UUID specimenId;
    private CreateSpecimenRequest createRequest;
    private UpdateSpecimenRequest updateRequest;
    private Specimen specimen;
    private SpecimenResponse specimenResponse;

    @BeforeEach
    void setUp() {

        specimenId = UUID.randomUUID();

        createRequest = CreateSpecimenRequest.builder()
                .name("Lynel")
                .region("Hebra")
                .dangerLevel(10)
                .isFriendly(false)
                .build();

        updateRequest = UpdateSpecimenRequest.builder()
                .name("Golden Lynel")
                .region("Hebra")
                .dangerLevel(15)
                .isFriendly(false)
                .build();

        specimen = Specimen.builder()
                .id(specimenId)
                .name(createRequest.getName())
                .region(createRequest.getRegion())
                .dangerLevel(createRequest.getDangerLevel())
                .isFriendly(createRequest.getIsFriendly())
                .build();

        specimenResponse = SpecimenResponse.builder()
                .id(specimenId)
                .name(specimen.getName())
                .region(specimen.getRegion())
                .dangerLevel(specimen.getDangerLevel())
                .isFriendly(specimen.getIsFriendly())
                .build();
    }

    @Test
    void createSpecimen_shouldSaveSpecimen() {

        when(specimenMapper.toEntityCreate(createRequest)).thenReturn(specimen);
        when(specimenRepository.save(specimen)).thenReturn(specimen);
        when(specimenMapper.toDto(specimen)).thenReturn(specimenResponse);

        SpecimenResponse result = specimenService.createSpecimen(createRequest);

        assertThat(result).isEqualTo(specimenResponse);

        verify(specimenRepository).save(specimen);
    }

    @Test
    void getSpecimenById_shouldReturnSpecimen() {

        when(specimenRepository.findById(specimenId)).thenReturn(Optional.of(specimen));
        when(specimenMapper.toDto(specimen)).thenReturn(specimenResponse);

        SpecimenResponse result = specimenService.getSpecimenById(specimenId);

        assertThat(result).isEqualTo(specimenResponse);

        verify(specimenRepository).findById(specimenId);
    }

    @Test
    void getAllSpecimens_shouldReturnPage() {

        Pageable pageable = PageRequest.of(0, 10, Sort.by("name").ascending());

        Page<Specimen> page = new PageImpl<>(List.of(specimen), pageable, 1);

        Page<SpecimenResponse> responsePage =
                new PageImpl<>(List.of(specimenResponse), pageable, 1);

        when(specimenRepository.findAll(pageable)).thenReturn(page);
        when(specimenMapper.toDtoList(page)).thenReturn(responsePage);

        PageableResponse<SpecimenResponse> result =
                specimenService.getAllSpecimens(0,10,"name","asc");

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(1);

        verify(specimenRepository).findAll(pageable);
    }

    @Test
    void updateSpecimen_shouldUpdateSpecimen() {

        Specimen updatedEntity = Specimen.builder()
                .id(specimenId)
                .name(updateRequest.getName())
                .region(updateRequest.getRegion())
                .dangerLevel(updateRequest.getDangerLevel())
                .isFriendly(updateRequest.getIsFriendly())
                .build();

        SpecimenResponse updatedResponse = SpecimenResponse.builder()
                .id(specimenId)
                .name(updateRequest.getName())
                .region(updateRequest.getRegion())
                .dangerLevel(updateRequest.getDangerLevel())
                .isFriendly(updateRequest.getIsFriendly())
                .build();

        when(specimenRepository.findById(specimenId)).thenReturn(Optional.of(specimen));
        when(specimenMapper.toDto(specimen)).thenReturn(specimenResponse);

        when(specimenMapper.toEntityUpdate(updateRequest, specimenId))
                .thenReturn(updatedEntity);

        when(specimenRepository.save(updatedEntity))
                .thenReturn(updatedEntity);

        when(specimenMapper.toDto(updatedEntity))
                .thenReturn(updatedResponse);

        SpecimenResponse result =
                specimenService.updateSpecimen(specimenId, updateRequest);

        assertThat(result).isEqualTo(updatedResponse);

        verify(specimenRepository).save(updatedEntity);
    }

    @Test
    void deleteSpecimen_shouldDeleteSpecimen() {

        when(specimenRepository.findById(specimenId))
                .thenReturn(Optional.of(specimen));

        when(specimenMapper.toDto(specimen))
                .thenReturn(specimenResponse);

        SpecimenResponse result = specimenService.deleteSpecimen(specimenId);

        assertThat(result).isEqualTo(specimenResponse);

        verify(specimenRepository).deleteById(specimenId);
    }

}