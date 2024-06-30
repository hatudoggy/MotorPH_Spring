import axios, { AxiosRequestConfig } from "axios";
import { API, BASE_API } from "../Api.ts";

export const fetchFromApi = async <T>(isPost: boolean, endpoint: string, data: any = {}, config: AxiosRequestConfig = {}): Promise<T> => {
    let response;

    if (isPost) {
        response = await axios.post(endpoint, data, config);
    } else {
        response = await axios.put(endpoint, data, config);
    }

    if (response.status < 200 || response.status >= 300) {
        throw new Error(`Error: ${response.statusText}`);
    }
    return response.data;
};

export const postTimeIn = async (employeeId: number): Promise<TimeInOut> => {
    console.log(`Clocking in for employee ${employeeId}`);

    const { EMPLOYEES, ATTENDANCES } = API;
    return fetchFromApi<TimeInOut>(true, `${BASE_API}${EMPLOYEES.BASE}${employeeId}${ATTENDANCES.TIME_IN}`);
}

export const putTimeOut = async (employeeId : number): Promise<TimeInOut> => {
    console.log(`Clocking out for employee ${employeeId}`);


    const { EMPLOYEES, ATTENDANCES } = API;
    return fetchFromApi<TimeInOut>(false, `${BASE_API}${EMPLOYEES.BASE}${employeeId}${ATTENDANCES.TIME_OUT}`);
}
