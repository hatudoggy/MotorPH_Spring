
import { useQuery } from "@tanstack/react-query";
import {
    fetchEmployeeById,
    fetchEmployees,
    fetchPositionById,
    fetchDepartments,
    fetchDepartmentById,
    fetchPosition,
    fetchEmploymentStatuses,
    fetchEmploymentStatusById,
    fetchPayrolls,
    fetchPayrollById,
    fetchPayrollsByEmployeeId,
    fetchEmployeeFullById, fetchSupervisorById
} from "../constants/FetchUtil";

export const useFetchEmployeeById = (employeeId: number) => {
    return useQuery<EmployeeBasicRes>({
        queryKey: ['employee', employeeId],
        queryFn: () => fetchEmployeeById(employeeId),
    });
};

export const useFetchEmployees = () => {
    return useQuery<EmployeeBasicRes[]>({
        queryKey: ['employees'],
        queryFn: fetchEmployees
    });
};

export const useFetchEmployeeFullById = (employeeId: number) => {
    return useQuery<EmployeeFullRes>({
        queryKey: ['employeeFull', employeeId],
        queryFn: () => fetchEmployeeFullById(employeeId),
    });
};
export const useFetchSupervisorById = (employeeId: number) => {
    return useQuery<SupervisorRes>({
        queryKey: ['supervisor', employeeId],
        queryFn: () => fetchSupervisorById(employeeId),
    });
}

export const useFetchPositions = () => {
    return useQuery<PositionRes[]>({
        queryKey: ['positions'],
        queryFn: fetchPosition
    });
};

export const useFetchPositionById = (positionCode: string) => {
    return useQuery<PositionRes>({
        queryKey: ['position', positionCode],
        queryFn: () => fetchPositionById(positionCode),
    });
};

export const useFetchDepartments = () => {
    return useQuery<DepartmentRes[]>({
        queryKey: ['departments'],
        queryFn: fetchDepartments
    });
};

export const useFetchDepartmentById = (departmentCode: string) => {
    return useQuery<DepartmentRes>({
        queryKey: ['department', departmentCode],
        queryFn: () => fetchDepartmentById(departmentCode),
    });
};

export const useFetchEmploymentStatuses = () => {
    return useQuery<EmploymentStatusRes[]>({
        queryKey: ['employmentStatuses'],
        queryFn: fetchEmploymentStatuses
    });
};

export const useFetchEmploymentStatusById = (statusId: number) => {
    return useQuery<EmploymentStatusRes>({
        queryKey: ['employmentStatus', statusId],
        queryFn: () => fetchEmploymentStatusById(statusId),
    });
};

export const useFetchPayroll = () => {
    return useQuery<PayrollRes[]>({
        queryKey: ['payrolls'],
        queryFn: fetchPayrolls
    });
}

export const useFetchPayrollsByEmployeeId = (employeeId: number) => {
    return useQuery<PayrollRes[]>({
        queryKey: ['payrolls', employeeId],
        queryFn: () => fetchPayrollsByEmployeeId(employeeId),
    });
}

export const useFetchPayrollById = (payrollId: number) => {
    return useQuery<PayrollRes>({
        queryKey: ['payroll', payrollId],
        queryFn: () => fetchPayrollById(payrollId),
    });
}

export function useEmployeePayrollData(employeeId: number) {
    const { data: employee, isLoading: employeeLoading, error: employeeError } = useFetchEmployeeById(employeeId);
    const { data: payrolls, isLoading: payrollsLoading, error: payrollsError } = useFetchPayrollsByEmployeeId(employeeId);

    return {
        employee,
        payrolls,
        isLoading: employeeLoading || payrollsLoading,
        error: employeeError || payrollsError
    };
}
