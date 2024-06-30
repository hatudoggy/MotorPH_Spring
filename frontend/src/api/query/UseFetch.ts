import { useQuery, UseQueryResult } from "@tanstack/react-query";
import {fetchData} from "./FetchUtil.ts";

export const useFetch = <T>(key: string, params?: Record<string, any>): UseQueryResult<T, Error> => {
    return useQuery<T, Error>({
        queryKey: [key, params],
        queryFn: () => fetchData(key, params),
        refetchOnWindowFocus: false
    });
};

export const useFetchEmployeeById = (employeeId: number) => {
    return useFetch<EmployeeBasicRes>('employee', { id: employeeId });
};

export const useFetchEmployees = () => {
    return useFetch<EmployeeBasicRes[]>('employees');
};

export const useFetchEmployeeFullById = (employeeId: number) => {
    return useFetch<EmployeeFullRes>('employee', { id: employeeId });
};

export const useFetchSupervisorById = (employeeId: number) => {
    return useFetch<SupervisorRes>('employee', { id: employeeId });
};

export const useFetchPositions = () => {
    return useFetch<PositionRes[]>('positions');
};

export const useFetchPositionById = (positionCode: string) => {
    return useFetch<PositionRes>('position', { id: positionCode });
};

export const useFetchDepartments = () => {
    return useFetch<DepartmentRes[]>('departments');
};

export const useFetchDepartmentById = (departmentCode: string) => {
    return useFetch<DepartmentRes>('department', { id: departmentCode });
};

export const useFetchEmploymentStatuses = () => {
    return useFetch<EmploymentStatusRes[]>('employmentStatuses');
};

export const useFetchEmploymentStatusById = (statusId: number) => {
    return useFetch<EmploymentStatusRes>('employmentStatus', { id: statusId });
};

export const useFetchPayroll = () => {
    return useFetch<PayrollRes[]>('payrolls');
};

export const useFetchPayrollsByEmployeeId = (employeeId: number) => {
    return useFetch<PayrollRes[]>('employeePayrolls', { id: employeeId });
};

export const useFetchPayrollById = (payrollId: number) => {
    return useFetch<PayrollRes>('payroll', { id: payrollId });
};

export const useFetchAttendancesByEmployeeId = (employeeId: number) => {
    return useFetch<AttendanceRes[]>('attendancesByEmployeeId', { id: employeeId });
};

export const useFetchAttendancesByEmployeeIdAndDateRange = (employeeId: number, startDate: string, endDate: string) => {
    return useFetch<AttendanceRes[]>('attendancesByDateRange', { id: employeeId, startDate: startDate, endDate: endDate });
};

export const useFetchAttendanceByEmployeeIdAndDate = (employeeId: number, date: string) => {
    console.log(`Fetching attendance for employee ${employeeId} on ${date}`);

    return useFetch<AttendanceRes>('attendanceByEmployeeIdAndDate', { id: employeeId, startDate: date });
};

export const useFetchAttendancesByDate = (dateFilter: Date) => {
    return useFetch<AttendanceFull[]>('attendancesByDate', { date: dateFilter });
};
