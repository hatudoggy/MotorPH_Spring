import { useQuery, UseQueryResult } from "@tanstack/react-query";
import { fetchData } from "./FetchUtil.ts";

const useFetch = <T>(key: string, params?: Record<string, any>): UseQueryResult<T, Error> => {
    return useQuery<T, Error>({
        queryKey: [key, params],
        queryFn: () => fetchData(key, params),
        enabled: !params || Object.values(params).every(param => param !== null && param !== undefined),
        refetchOnWindowFocus: true
    });
};

// EMPLOYEES ------------------------------------------------------------------------
export const useFetchEmployeeById = (employeeId: number | null) => {
    return useFetch<EmployeeBasicRes>('employee', employeeId ? { id: employeeId } : undefined);
};

export const useFetchEmployees = () => {
    return useFetch<EmployeeBasicRes[]>('employees');
};

export const useFetchEmployeeFullById = (employeeId: number | null) => {
    return useFetch<EmployeeFullRes>('employee', employeeId ? { id: employeeId } : undefined);
};

// SUPERVISORS -----------------------------------------------------------------------
export const useFetchSupervisorById = (employeeId: number | null) => {
    return useFetch<SupervisorRes>('employee', employeeId ? { id: employeeId } : undefined);
};

export const useFetchSupervisors = () => {
    return useFetch<SupervisorRes[]>('supervisors');
};

// POSITIONS -----------------------------------------------------------------------
export const useFetchPositions = () => {
    return useFetch<PositionRes[]>('positions');
};

export const useFetchPositionById = (positionCode: string | null) => {
    return useFetch<PositionRes>('position', positionCode ? { id: positionCode } : undefined);
};

// DEPARTMENTS ---------------------------------------------------------------------
export const useFetchDepartments = () => {
    return useFetch<DepartmentRes[]>('departments');
};

export const useFetchDepartmentById = (departmentCode: string | null) => {
    return useFetch<DepartmentRes>('department', departmentCode ? { id: departmentCode } : undefined);
};

// STATUSES -----------------------------------------------------------------------
export const useFetchEmploymentStatuses = () => {
    return useFetch<EmploymentStatusRes[]>('employmentStatuses');
};

export const useFetchEmploymentStatusById = (statusId: number | null) => {
    return useFetch<EmploymentStatusRes>('employmentStatus', statusId ? { id: statusId } : undefined);
};

// PAYROLL -----------------------------------------------------------------------
export const useFetchPayroll = () => {
    return useFetch<PayrollRes[]>('payrolls');
};

export const useFetchPayrollsByEmployeeId = (employeeId: number | null) => {
    return useFetch<PayrollRes[]>('payrollByEmployeeId', employeeId ? { id: employeeId } : undefined);
};

export const useFetchPayrollById = (payrollId: number | null) => {
    return useFetch<PayrollRes>('payrollId', payrollId ? { id: payrollId } : undefined);
};

// ATTENDANCE -----------------------------------------------------------------------
export const useFetchAttendancesByEmployeeId = (employeeId: number | null) => {
    return useFetch<AttendanceRes[]>('attendancesByEmployeeId', employeeId ? { id: employeeId } : undefined);
};

export const useFetchAttendancesByEmployeeIdAndDateRange = (employeeId: number | null, startDate: string, endDate: string) => {
    return useFetch<AttendanceRes[]>('attendancesByDateRange', employeeId ? { id: employeeId, startDate, endDate } : undefined);
};

export const useFetchAttendanceByEmployeeIdAndDate = (employeeId: number | null, date: string) => {
    console.log(`Fetching attendance for employee ${employeeId} on ${date}`);
    return useFetch<AttendanceRes>('attendanceByEmployeeIdAndDate', employeeId ? { id: employeeId, startDate: date } : undefined);
};

export const useFetchAttendancesByDate = (dateFilter: Date | null) => {
    return useFetch<AttendanceFull[]>('attendancesByDate', dateFilter ? { date: dateFilter } : undefined);
};
