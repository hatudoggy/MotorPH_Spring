package com.motorph.pms.service.impl;

import com.motorph.pms.model.PagibigMatrix;
import com.motorph.pms.model.PhilhealthMatrix;
import com.motorph.pms.model.SSSMatrix;
import com.motorph.pms.model.WitholdingTaxMatrix;
import com.motorph.pms.repository.*;
import com.motorph.pms.service.MatrixService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MatrixServiceImpl implements MatrixService {

    private final SSSMatrixRepository sssMatrixRepository;
    private final PhilhealthMatrixRepository philhealthMatrixRepository;
    private final PagibigMatrixRepository pagibigMatrixRepository;
    private final WitholdingTaxMatrixRepository witholdingTaxMatrixRepository;

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
    }

    @Override
    public SSSMatrix getSSSMatrix(Double value) {
        return sssMatrixRepository.findByMinRangeLessThanEqualAndMaxRangeGreaterThanEqual(value, value);
    }

    @Override
    public PhilhealthMatrix getPhilhealthMatrix(Double value) {
        return philhealthMatrixRepository.findByMinRangeLessThanEqualAndMaxRangeGreaterThanEqual(value, value);
    }

    @Override
    public PagibigMatrix getPagibigMatrix(Double value) {
        return pagibigMatrixRepository.findByMinRangeLessThanEqualAndMaxRangeGreaterThanEqual(value, value);
    }

    @Override
    public WitholdingTaxMatrix getWitholdingTaxMatrix(Double value) {
        return witholdingTaxMatrixRepository.findByMinRangeLessThanEqualAndMaxRangeGreaterThanEqual(value, value);
    }
}
