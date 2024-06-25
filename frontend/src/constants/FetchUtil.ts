import {API, BASE_API} from "./Api.ts";

export const fetchFromApi = async <T>(endpoint: string, options: RequestInit = {}): Promise<T> => {
    const response = await fetch(endpoint, options);
    if (!response.ok) {
        throw new Error(`Error: ${response.statusText}`);
    }
    return response.json();
};

export const fetchEmployees = async (): Promise<EmployeeRes[]> => {
    const { EMPLOYEES } = API;
    return fetchFromApi<EmployeeRes[]>(`${BASE_API}${EMPLOYEES.BASE}`);
};

export const fetchEmployeeById = async (employeeId: number): Promise<EmployeeRes> => {
    const { EMPLOYEES } = API;
    return fetchFromApi<EmployeeRes>(`${BASE_API}${EMPLOYEES.BASE}${employeeId}`);
};

export const fetchSupervisorById = async (employeeId: number): Promise<SupervisorRes> => {
    const { EMPLOYEES } = API;
    return fetchFromApi<SupervisorRes>(`${BASE_API}${EMPLOYEES.BASE}${employeeId}`);
}

export const fetchPosition = async (): Promise<PositionRes[]> => {
    const { COMPANY: POSITIONS } = API;
    return fetchFromApi<PositionRes[]>(`${BASE_API}${POSITIONS.BASE}`);
};

export const fetchPositionById = async (positionCode: string): Promise<PositionRes> => {
    const { COMPANY: POSITIONS } = API;
    return fetchFromApi<PositionRes>(`${BASE_API}${POSITIONS.BASE}${positionCode}`);
};

export const fetchDepartments = async (): Promise<DepartmentRes[]> => {
    const { COMPANY: DEPARTMENTS } = API;
    return fetchFromApi<DepartmentRes[]>(`${BASE_API}${DEPARTMENTS.BASE}`);
};

export const fetchDepartmentById = async (departmentCode: string): Promise<DepartmentRes> => {
    const { COMPANY: DEPARTMENTS } = API;
    return fetchFromApi<DepartmentRes>(`${BASE_API}${DEPARTMENTS.BASE}${departmentCode}`);
};

export const fetchEmploymentStatuses = async (): Promise<EmploymentStatusRes[]> => {
    const { COMPANY: STATUSES } = API;
    return fetchFromApi<EmploymentStatusRes[]>(`${BASE_API}${STATUSES.BASE}`);
};

export const fetchEmploymentStatusById = async (statusId: number): Promise<EmploymentStatusRes> => {
    const { COMPANY: STATUSES } = API;
    return fetchFromApi<EmploymentStatusRes>(`${BASE_API}${STATUSES.BASE}${statusId}`);
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
