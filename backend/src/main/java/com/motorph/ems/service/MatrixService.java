package com.motorph.ems.service;

import com.motorph.ems.model.PagibigMatrix;
import com.motorph.ems.model.PhilhealthMatrix;
import com.motorph.ems.model.SSSMatrix;
import com.motorph.ems.model.WitholdingTaxMatrix;

public interface MatrixService {

    SSSMatrix getSSSMatrix(Double value);
    PhilhealthMatrix getPhilhealthMatrix(Double value);
    PagibigMatrix getPagibigMatrix(Double value);
    WitholdingTaxMatrix getWitholdingTaxMatrix(Double value);
}
