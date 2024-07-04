package com.motorph.pms.service;

import com.motorph.pms.model.PagibigMatrix;
import com.motorph.pms.model.PhilhealthMatrix;
import com.motorph.pms.model.SSSMatrix;
import com.motorph.pms.model.WithholdingTaxMatrix;

public interface MatrixService {
    SSSMatrix getSSSMatrix(Double value);
    PhilhealthMatrix getPhilhealthMatrix();
    PagibigMatrix getPagibigMatrix(Double value);
    WithholdingTaxMatrix getWithholdingTaxMatrix(Double value);
}
