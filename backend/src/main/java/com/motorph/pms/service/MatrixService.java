package com.motorph.pms.service;

import com.motorph.pms.model.PagibigMatrix;
import com.motorph.pms.model.PhilhealthMatrix;
import com.motorph.pms.model.SSSMatrix;
import com.motorph.pms.model.WitholdingTaxMatrix;

public interface MatrixService {
    SSSMatrix getSSSMatrix(Double value);
    PhilhealthMatrix getPhilhealthMatrix(Double value);
    PagibigMatrix getPagibigMatrix(Double value);
    WitholdingTaxMatrix getWitholdingTaxMatrix(Double value);
}
