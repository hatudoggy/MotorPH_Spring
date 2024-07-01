import { API, BASE_API } from "../Api.ts";
import { format } from "date-fns";

export const fetchFromApi = async <T>(endpoint: string, options: RequestInit = {}): Promise<T> => {
    const response = await fetch(endpoint, options);
    if (!response.ok) {
        throw new Error(`Error: ${response.statusText}`);
    }
    return response.json();
};

export const fetchData = async <T>(key: string, params?: Record<string, any>): Promise<T> => {
    let endpoint = BASE_API;

    switch (key) {
        case 'employees':
            endpoint += API.EMPLOYEES.ALL;
            break;
        case 'employee':
            endpoint += API.EMPLOYEES.BASE + params?.id;
            break;
        case 'supervisors':
            endpoint += API.COMPANY.SUPERVISORS + "all";
            break;
        case 'positions':
            endpoint += API.COMPANY.POSITIONS + "all";
            break;
        case 'position':
            endpoint += API.COMPANY.POSITIONS + params?.id;
            break;
        case 'departments':
            endpoint += API.COMPANY.DEPARTMENTS + "all";
            break;
        case 'department':
            endpoint += API.COMPANY.DEPARTMENTS + params?.id;
            break;
        case 'employmentStatuses':
            endpoint += API.COMPANY.STATUSES + "all";
            break;
        case 'employmentStatus':
            endpoint += API.COMPANY.STATUSES + params?.id;
            break;
        case 'payrolls':
            endpoint += API.PAYROLLS.BASE;
            break;
        case 'payroll':
            endpoint += API.PAYROLLS.BASE + params?.id;
            break;
        case 'employeePayrolls':
            endpoint += API.EMPLOYEES.BASE + params?.id + API.PAYROLLS.ALL;
            break;
        case 'attendances':
            endpoint += API.ATTENDANCES.BASE;
            break;
        case 'attendancesByEmployeeId':
            endpoint += API.EMPLOYEES.BASE + params?.id + API.ATTENDANCES.BASE;
            break;
        case 'attendanceByEmployeeIdAndDate':
            endpoint += API.EMPLOYEES.BASE + params?.id + API.ATTENDANCES.DATE + `?date=${params?.startDate}`;
            break;
        case 'attendancesByDate':
            endpoint += API.ATTENDANCES.BASE + `?date=${format(params?.date, 'yyyy-MM-dd')}`;
            break;
        case 'attendancesByDateRange':
            endpoint += API.EMPLOYEES.BASE + params?.id + API.ATTENDANCES.RANGE + `?startDate=${params?.startDate}&endDate=${params?.endDate}`;
            break;
        default:
            throw new Error('Invalid key');
    }

    return fetchFromApi<T>(endpoint);
};
