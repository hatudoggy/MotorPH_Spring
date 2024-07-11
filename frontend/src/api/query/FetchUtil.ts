import { API, BASE_API } from "../Api.ts";
import { format } from "date-fns";

export const fetchFromApi = async <T>(endpoint: string, options: RequestInit = {}): Promise<T> => {
    console.log(`Starting fetch: ${options.method || 'GET'} "${endpoint}"`);
    const response = await fetch(endpoint, options);
    if (!response.ok) {
        throw new Error(`Error: ${response.statusText}`);
    }
    const data = await response.json();
    console.log(`Fetch finished loading: ${options.method || 'GET'} "${endpoint}"`);
    return data;
};

export const fetchData = async <T>(key: string,params?: Record<string, any>): Promise<T> => {
    let endpoint = BASE_API;

    switch (key) {
        case 'employees':
            endpoint += API.EMPLOYEES.ALL;
            break;
        case 'employee':
            endpoint += API.EMPLOYEES.BASE + params?.id;
            break;
        case 'activeEmployees':
            endpoint += API.EMPLOYEES.ACTIVE;
            break;
        case 'supervisors':
            endpoint += API.COMPANY.SUPERVISORS;
            break;
        case 'positions':
            endpoint += API.COMPANY.POSITIONS;
            break;
        case 'position':
            endpoint += API.COMPANY.POSITIONS + "/" + params?.id;
            break;
        case 'departments':
            endpoint += API.COMPANY.DEPARTMENTS;
            break;
        case 'department':
            endpoint += API.COMPANY.DEPARTMENTS + "/" + params?.id;
            break;
        case 'employmentStatuses':
            endpoint += API.COMPANY.STATUSES;
            break;
        case 'employmentStatus':
            endpoint += API.COMPANY.STATUSES + "/" + params?.id;
            break;
        case 'benefitTypes':
            endpoint += API.COMPANY.BENEFIT_TYPES
            break
        case 'leaveTypes':
            endpoint += API.COMPANY.LEAVE_TYPES
            break;
        case 'payrolls':
            endpoint += API.PAYROLLS.BASE;
            break;
        case 'payrollId':
            endpoint += API.PAYROLLS.BASE + params?.id;
            break;
        case 'payrollByEmployeeId':
            endpoint += API.EMPLOYEES.BASE + params?.id + API.PAYROLLS.ALL;
            break;
        case 'attendances':
            endpoint += API.ATTENDANCES.ALL;
            break;
        case 'attendancesByEmployeeId':
            endpoint += API.EMPLOYEES.BASE + params?.id + API.ATTENDANCES.ALL;
            break;
        case 'attendanceByEmployeeIdAndDate':
            endpoint += API.EMPLOYEES.BASE + params?.id + API.ATTENDANCES.DATE + `?date=${params?.startDate}`;
            break;
        case 'attendancesByDate':
            endpoint += API.ATTENDANCES.ALL + `?date=${format(params?.date, 'yyyy-MM-dd')}`;
            break;
        case 'attendancesByDateRange':
            endpoint += API.EMPLOYEES.BASE + params?.id + API.ATTENDANCES.RANGE + `?startDate=${params?.startDate}&endDate=${params?.endDate}`;
            break;
        case 'leaveRequests':
            endpoint += API.LEAVES.ALL;
            break;
        case 'leaveRequestsByEmployeeId':
            endpoint += API.EMPLOYEES.BASE + params?.id + API.LEAVES.REQUEST;
            break;
        case 'leaveRequestsByRequestDateRange':
            endpoint += API.LEAVES.RANGE + `?requestDateAfter=${params?.afterDate}&requestDateBefore=${params?.beforeDate}`;
            break;
        case 'leaveBalances':
            endpoint += API.LEAVES.BALANCES;
            break;
        case 'leaveBalancesByEmployeeId':
            endpoint += API.EMPLOYEES.BASE + params?.id + API.LEAVES.BALANCES;
            break;
        default:
            throw new Error('Invalid key');
    }

    console.log('Fetching from API endpoint:', endpoint);
    return fetchFromApi<T>(endpoint);
};

