
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
    fetchEmployeeFullById,
    fetchSupervisorById,
    fetchAttendancesByEmployeeId,
    fetchAttendancesByEmployeeIdAndDate,
    fetchAttendancesByEmployeeIdAndDateRange, fetchAttendancesByDate
} from "../constants/FetchUtil";

export const useFetchEmployeeById = (employeeId: number) => {
    return useQuery<EmployeeBasicRes>({
        queryKey: ['employee', employeeId],
        queryFn: () => fetchEmployeeById(employeeId),
        refetchOnWindowFocus: false
    });
};

export const useFetchEmployees = () => {
    return useQuery<EmployeeBasicRes[]>({
        queryKey: ['employees'],
        queryFn: fetchEmployees,
        refetchOnWindowFocus: false
    });
};

export const useFetchEmployeeFullById = (employeeId: number) => {
    return useQuery<EmployeeFullRes>({
        queryKey: ['employeeFull', employeeId],
        queryFn: () => fetchEmployeeFullById(employeeId),
        refetchOnWindowFocus: false
    });
};
export const useFetchSupervisorById = (employeeId: number) => {
    return useQuery<SupervisorRes>({
        queryKey: ['supervisor', employeeId],
        queryFn: () => fetchSupervisorById(employeeId),
        refetchOnWindowFocus: false
    });
}

export const useFetchPositions = () => {
    return useQuery<PositionRes[]>({
        queryKey: ['positions'],
        queryFn: fetchPosition,
        refetchOnWindowFocus: false
    });
};

export const useFetchPositionById = (positionCode: string) => {
    return useQuery<PositionRes>({
        queryKey: ['position', positionCode],
        queryFn: () => fetchPositionById(positionCode),
        refetchOnWindowFocus: false
    });
};

export const useFetchDepartments = () => {
    return useQuery<DepartmentRes[]>({
        queryKey: ['departments'],
        queryFn: fetchDepartments,
        refetchOnWindowFocus: false
    });
};

export const useFetchDepartmentById = (departmentCode: string) => {
    return useQuery<DepartmentRes>({
        queryKey: ['department', departmentCode],
        queryFn: () => fetchDepartmentById(departmentCode),
        refetchOnWindowFocus: false
    });
};

export const useFetchEmploymentStatuses = () => {
    return useQuery<EmploymentStatusRes[]>({
        queryKey: ['employmentStatuses'],
        queryFn: fetchEmploymentStatuses,
        refetchOnWindowFocus: false
    });
};

export const useFetchEmploymentStatusById = (statusId: number) => {
    return useQuery<EmploymentStatusRes>({
        queryKey: ['employmentStatus', statusId],
        queryFn: () => fetchEmploymentStatusById(statusId),
        refetchOnWindowFocus: false
    });
};

export const useFetchPayroll = () => {
    return useQuery<PayrollRes[]>({
        queryKey: ['payrolls'],
        queryFn: fetchPayrolls,
        refetchOnWindowFocus: false
    });
}

export const useFetchPayrollsByEmployeeId = (employeeId: number) => {
    return useQuery<PayrollRes[]>({
        queryKey: ['payrolls', employeeId],
        queryFn: () => fetchPayrollsByEmployeeId(employeeId),
        refetchOnWindowFocus: false
    });
}

export const useFetchPayrollById = (payrollId: number) => {
    return useQuery<PayrollRes>({
        queryKey: ['payroll', payrollId],
        queryFn: () => fetchPayrollById(payrollId),
        refetchOnWindowFocus: false
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

export function useFetchAttendancesByEmployeeId(employeeId: number) {
    // noinspection SpellCheckingInspection
    return useQuery<AttendanceRes[]>({
        queryKey: ['attendances', employeeId],
        queryFn: () => fetchAttendancesByEmployeeId(employeeId),
        refetchOnWindowFocus: false
    });
}

export function useFetchAttendancesByEmployeeIdAndDateRange(employeeId: number, startDate: string, endDate: string) {
    return useQuery<AttendanceRes[]>({
        queryKey: ['attendances', employeeId, startDate, endDate],
        queryFn: () => fetchAttendancesByEmployeeIdAndDateRange(employeeId, startDate, endDate),
        refetchOnWindowFocus: false
    });
}

export function useFetchAttendanceByEmployeeIdAndDate(employeeId: number, date: string) {
    return useQuery<AttendanceRes[]>({
        queryKey: ['attendances', employeeId, date],
        queryFn: () => fetchAttendancesByEmployeeIdAndDate(employeeId, date),
        refetchOnWindowFocus: false
    });
}

export function useFetchAttendancesByDate(dateFilter: Date){
    return useQuery<AttendanceFull[]>({
        queryKey: ['attendanceAll', dateFilter],
        queryFn: () => fetchAttendancesByDate(dateFilter),
        refetchOnWindowFocus: false,
        initialData: () => {
            const storedData = localStorage.getItem('attendanceData' + dateFilter);
            return storedData ? JSON.parse(storedData) : undefined;
        }
    })
}
