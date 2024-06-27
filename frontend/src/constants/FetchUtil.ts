import {API, BASE_API} from "./Api.ts";
import {format} from "date-fns";
import axios from "axios";

export const fetchFromApi = async <T>(endpoint: string, options: RequestInit = {}): Promise<T> => {
    const response = await fetch(endpoint, options);
    if (!response.ok) {
        throw new Error(`Error: ${response.statusText}`);
    }
    return response.json();
};

export const fetchEmployees = async (): Promise<EmployeeBasicRes[]> => {
    const { EMPLOYEES } = API;
    return fetchFromApi<EmployeeBasicRes[]>(`${BASE_API}${EMPLOYEES.ALL}`);
};

export const fetchEmployeeById = async (employeeId: number): Promise<EmployeeBasicRes> => {
    const { EMPLOYEES } = API;
    return fetchFromApi<EmployeeBasicRes>(`${BASE_API}${EMPLOYEES.BASE}${employeeId}`);
};

export const fetchEmployeeFullById = async (employeeId: number): Promise<EmployeeFullRes> => {
    const { EMPLOYEES } = API;
    return fetchFromApi<EmployeeFullRes>(`${BASE_API}${EMPLOYEES.BASE}${employeeId}`);
};

export const fetchSupervisorById = async (employeeId: number): Promise<SupervisorRes> => {
    const { EMPLOYEES } = API;
    return fetchFromApi<SupervisorRes>(`${BASE_API}${EMPLOYEES.BASE}${employeeId}`);
}

export const fetchPosition = async (): Promise<PositionRes[]> => {
    const { COMPANY } = API;
    return fetchFromApi<PositionRes[]>(`${BASE_API}${COMPANY.POSITIONS}`);
};

export const fetchPositionById = async (positionCode: string): Promise<PositionRes> => {
    const { COMPANY } = API;
    return fetchFromApi<PositionRes>(`${BASE_API}${COMPANY.POSITIONS}${positionCode}`);
};

export const fetchDepartments = async (): Promise<DepartmentRes[]> => {
    const { COMPANY } = API;
    return fetchFromApi<DepartmentRes[]>(`${BASE_API}${COMPANY.DEPARTMENTS}`);
};

export const fetchDepartmentById = async (departmentCode: string): Promise<DepartmentRes> => {
    const { COMPANY } = API;
    return fetchFromApi<DepartmentRes>(`${BASE_API}${COMPANY.DEPARTMENTS}${departmentCode}`);
};

export const fetchEmploymentStatuses = async (): Promise<EmploymentStatusRes[]> => {
    const { COMPANY } = API;
    return fetchFromApi<EmploymentStatusRes[]>(`${BASE_API}${COMPANY.STATUSES}`);
};

export const fetchEmploymentStatusById = async (statusId: number): Promise<EmploymentStatusRes> => {
    const { COMPANY } = API;
    return fetchFromApi<EmploymentStatusRes>(`${BASE_API}${COMPANY.STATUSES}${statusId}`);
};

export const fetchPayrolls = async (): Promise<PayrollRes[]> => {
    const { PAYROLLS } = API;
    return fetchFromApi<PayrollRes[]>(`${BASE_API}${PAYROLLS.BASE}`);
};

export const fetchPayrollsByEmployeeId = async (employeeId: number): Promise<PayrollRes[]> => {
    console.log("Fetching payrolls for employee:" + employeeId);
    const { EMPLOYEES, PAYROLLS } = API;

    console.log(`${BASE_API}${EMPLOYEES.BASE}${employeeId}${PAYROLLS.ALL}`);

    return fetchFromApi<PayrollRes[]>(`${BASE_API}${EMPLOYEES.BASE}${employeeId}${PAYROLLS.ALL}`);
};

export const fetchPayrollById = async (payrollId: number): Promise<PayrollRes> => {
    const { PAYROLLS } = API;
    return fetchFromApi<PayrollRes>(`${BASE_API}${PAYROLLS.BASE}${payrollId}`);
};

export const fetchAttendancesByEmployeeId = async (employeeId: number): Promise<AttendanceRes[]> => {
    const { EMPLOYEES, ATTENDANCES } = API;
    return fetchFromApi<AttendanceRes[]>(`${BASE_API}${EMPLOYEES.BASE}${employeeId}${ATTENDANCES.BASE}`);
};

export const fetchAttendancesByEmployeeIdAndDate = async (employeeId: number, date: string): Promise<AttendanceRes[]> => {
    const {EMPLOYEES, ATTENDANCES} = API;
    console.log("Fetching attendance for date:", date);

    return fetchFromApi(`${BASE_API}${EMPLOYEES.BASE}${employeeId}${ATTENDANCES.BASE}?startDate=${date}`);
}

export const fetchAttendancesByEmployeeIdAndDateRange = async (employeeId: number, startDate: string, endDate: string): Promise<AttendanceRes[]> => {
    const {EMPLOYEES, ATTENDANCES} = API

    console.log("Fetching attendance for date range:", startDate, endDate);

    return fetchFromApi<AttendanceRes[]>(`${BASE_API}${EMPLOYEES.BASE}${employeeId}${ATTENDANCES.BASE}?startDate=${startDate}&endDate=${endDate}`);
}

export const fetchAttendances = async (): Promise<AttendanceRes[]> => {
    const { ATTENDANCES } = API;
    return fetchFromApi<AttendanceRes[]>(`${BASE_API}${ATTENDANCES.BASE}`);
};
