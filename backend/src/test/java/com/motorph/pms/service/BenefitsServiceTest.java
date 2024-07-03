package com.motorph.pms.service;

import com.motorph.pms.dto.BenefitDTO;
import com.motorph.pms.dto.BenefitTypeDTO;
import com.motorph.pms.dto.mapper.BenefitsMapper;
import com.motorph.pms.model.Benefits;
import com.motorph.pms.model.Benefits.BenefitType;
import com.motorph.pms.repository.BenefitTypeRepository;
import com.motorph.pms.service.impl.BenefitsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BenefitsServiceTest {

    @Mock
    private BenefitsMapper benefitsMapper;

    @Mock
    private BenefitTypeRepository typeRepository;

    @InjectMocks
    private BenefitsServiceImpl benefitsService;

    private Benefits benefit1;
    private Long benefitId;
    private BenefitDTO benefitDTO1;
    private BenefitType benefitType1;
    private BenefitTypeDTO benefitTypeDTO1;

    @BeforeEach
    void setUp() {
        benefitType1 = BenefitType.builder()
                .benefitTypeId(1)
                .benefit("Health Insurance")
                .build();

        benefitTypeDTO1 = BenefitTypeDTO.builder()
                .benefitTypeId(1)
                .benefit("Health Insurance")
                .build();

        benefitId = 1L;

        benefit1 = Benefits.builder()
                .benefitId(benefitId)
                .amount(500.0)
                .benefitType(benefitType1)
                .build();

        benefitDTO1 = BenefitDTO.builder()
                .benefitId(benefitId)
                .benefitType(benefitTypeDTO1)
                .amount(500.0)
                .build();
    }


    @Test
    @Transactional
    @DirtiesContext
    void BenefitsService_getBenefitTypeByBenefitId_ReturnsBenefitType() {
        when(typeRepository.findById(1)).thenReturn(Optional.of(benefitType1));

        when(benefitsMapper.toDTO(any(BenefitType.class))).thenReturn(benefitTypeDTO1);

        Optional<BenefitTypeDTO> foundBenefitType = benefitsService.getBenefitTypeByBenefitId(1);

        assertThat(foundBenefitType).isPresent();
        assertThat(foundBenefitType.get().benefit()).isEqualTo("Health Insurance");
    }

//    @Test
//    @Transactional
//    @DirtiesContext
//    void BenefitsService_getBenefitTypeByBenefit_ReturnsBenefitType() {
//        when(typeRepository.findBenefitTypeByBenefit("Health Insurance")).thenReturn(Optional.of(benefitType1));
//
//        Optional<BenefitTypeDTO> foundBenefitType = benefitsService.getBenefitTypeByBenefit("Health Insurance");
//
//        assertThat(foundBenefitType).isPresent();
//        assertThat(foundBenefitType.get().benefit()).isEqualTo("Health Insurance");
//    }

    @Test
    @Transactional
    @DirtiesContext
    void BenefitsService_getAllBenefitTypes_ReturnsListOfBenefitType() {
        when(typeRepository.findAll()).thenReturn(Collections.singletonList(benefitType1));

        List<BenefitTypeDTO> benefitTypes = benefitsService.getAllBenefitTypes();

        assertThat(benefitTypes).isNotEmpty();
    }
}
