package com.motorph.ems.repository;

import com.motorph.ems.model.Benefits.BenefitType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class BenefitTypeRepositoryTest {

    @Autowired
    private BenefitTypeRepository benefitTypeRepository;

    private BenefitType benefitType1, benefitType2;

    @BeforeEach
    void setUp() {
        benefitType1 = new BenefitType("Health Insurance");
        benefitType2 = new BenefitType("Retirement Plan");
    }

    @Test
    @Transactional
    @DirtiesContext
    void BenefitTypeRepository_saveBenefitType_returnsSavedBenefitType() {

        BenefitType savedBenefitType1 = benefitTypeRepository.save(benefitType1);
        BenefitType savedBenefitType2 = benefitTypeRepository.save(benefitType2);

        assertNotNull(savedBenefitType1);
        assertNotNull(savedBenefitType2);
        assertThat(savedBenefitType1.getBenefit()).isEqualTo("Health Insurance");
        assertThat(savedBenefitType2.getBenefit()).isEqualTo("Retirement Plan");
    }

    @Test
    @Transactional
    @DirtiesContext
    void BenefitTypeRepository_findBenefitTypeByBenefit_Found() {

        benefitTypeRepository.save(benefitType1);
        benefitTypeRepository.save(benefitType2);

        Optional<BenefitType> foundBenefitType1 = benefitTypeRepository.findBenefitTypeByBenefit("Health Insurance");
        Optional<BenefitType> foundBenefitType2 = benefitTypeRepository.findBenefitTypeByBenefit("Retirement Plan");

        assertThat(foundBenefitType1).isPresent();
        assertThat(foundBenefitType2).isPresent();
    }

    @Test
    @Transactional
    @DirtiesContext
    void BenefitTypeRepository_findBenefitTypeByBenefit_NotFound() {

        Optional<BenefitType> foundBenefitType = benefitTypeRepository.findBenefitTypeByBenefit("Non-Existent Benefit");

        assertThat(foundBenefitType).isNotPresent();
    }

    @Test
    @Transactional
    @DirtiesContext
    void BenefitTypeRepository_getAllBenefitTypes_Found() {

        benefitTypeRepository.save(benefitType1);
        benefitTypeRepository.save(benefitType2);

        List<BenefitType> benefitTypes = benefitTypeRepository.findAll();
        assertThat(benefitTypes).hasSize(2);
    }

    @Test
    @Transactional
    @DirtiesContext
    void BenefitTypeRepository_getAllBenefitTypes_ReturnsEmptyList() {
        List<BenefitType> benefitTypes = benefitTypeRepository.findAll();
        assertThat(benefitTypes).isEmpty();
    }

    @Test
    @Transactional
    @DirtiesContext
    void BenefitTypeRepository_updateBenefitType_ReturnsUpdatedBenefitType() {

        benefitTypeRepository.save(benefitType1);

        String newBenefit = "Updated Health Insurance";
        benefitType1.setBenefit(newBenefit);

        BenefitType updatedBenefitType1 = benefitTypeRepository.save(benefitType1);

        assertThat(updatedBenefitType1.getBenefit()).isEqualTo(newBenefit);
    }

    @Test
    @Transactional
    @DirtiesContext
    void BenefitTypeRepository_deleteBenefitType() {
        benefitTypeRepository.save(benefitType1);
        benefitTypeRepository.delete(benefitType1);
        List<BenefitType> benefitTypes = benefitTypeRepository.findAll();
        assertThat(benefitTypes).isEmpty();
    }
}
