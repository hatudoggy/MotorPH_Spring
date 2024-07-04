package com.motorph.pms.service.impl;

import com.motorph.pms.model.PagibigMatrix;
import com.motorph.pms.model.PhilhealthMatrix;
import com.motorph.pms.model.SSSMatrix;
import com.motorph.pms.model.WithholdingTaxMatrix;
import com.motorph.pms.repository.*;
import com.motorph.pms.service.MatrixService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatrixServiceImpl implements MatrixService {

    private final SSSMatrixRepository sssMatrixRepository;
    private final PhilhealthMatrixRepository philhealthMatrixRepository;
    private final PagibigMatrixRepository pagibigMatrixRepository;
    private final WitholdingTaxMatrixRepository witholdingTaxMatrixRepository;

    private List<SSSMatrix> sssMatrices;
    private List<PagibigMatrix> pagibigMatrices;
    private List<WithholdingTaxMatrix> withholdingTaxMatrices;
    private PhilhealthMatrix philhealthMatrix;

    @Autowired
    public MatrixServiceImpl(
            SSSMatrixRepository sssMatrixRepository,
            PhilhealthMatrixRepository philhealthMatrixRepository,
            PagibigMatrixRepository pagibigMatrixRepository,
            WitholdingTaxMatrixRepository witholdingTaxMatrixRepository
    ) {
        this.sssMatrixRepository = sssMatrixRepository;
        this.philhealthMatrixRepository = philhealthMatrixRepository;
        this.pagibigMatrixRepository = pagibigMatrixRepository;
        this.witholdingTaxMatrixRepository = witholdingTaxMatrixRepository;
        loadMatrices();
    }

    /**
     * Loads all the matrices from the repositories.
     */
    @PostConstruct
    private void loadMatrices() {
        sssMatrices = sssMatrixRepository.findAll();
        philhealthMatrix = philhealthMatrixRepository.findById(1L).orElseThrow(
                () -> new RuntimeException("Philhealth Matrix not found")
        );
        pagibigMatrices = pagibigMatrixRepository.findAll();
        withholdingTaxMatrices = witholdingTaxMatrixRepository.findAll();
    }

    /**
     * Get the SSS Matrix based on the given value.
     *
     * @param value The value to search for in the SSS Matrix range
     * @return The SSS Matrix that matches the given value
     */
    @Override
    public SSSMatrix getSSSMatrix(Double value) {
        return sssMatrices.stream().filter(
                matrix -> matrix.getMinRange() <= value && matrix.getMaxRange() >= value).findFirst().orElseThrow(
                () -> new RuntimeException("No SSS Matrix found for value: " + value)
        );
    }

    /**
     * Get the Philhealth Matrix.
     *
     * @return The Philhealth Matrix
     */
    @Override
    public PhilhealthMatrix getPhilhealthMatrix() {
        return philhealthMatrix;
    }

    /**
     * Get the Pagibig Matrix based on the given value.
     *
     * @param value The value to search for in the Pagibig Matrix range
     * @return The Pagibig Matrix that matches the given value
     */
    @Override
    public PagibigMatrix getPagibigMatrix(Double value) {
        return pagibigMatrices.stream().filter(
                matrix -> matrix.getMinRange() <= value && matrix.getMaxRange() >= value).findFirst().orElseThrow(
                () -> new RuntimeException("No Pagibig Matrix found for value: " + value)
        );
    }

    /**
     * Get the Withholding Tax Matrix based on the given value.
     *
     * @param value The value to search for in the Withholding Tax Matrix range
     * @return The Withholding Tax Matrix that matches the given value
     */
    @Override
    public WithholdingTaxMatrix getWithholdingTaxMatrix(Double value) {
        return withholdingTaxMatrices.stream().filter(
                matrix -> matrix.getMinRange() <= value && matrix.getMaxRange() >= value).findFirst().orElseThrow(
                () -> new RuntimeException("No Withholding Tax Matrix found for value: " + value)
        );
    }
}
