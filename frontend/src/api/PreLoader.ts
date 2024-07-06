import {QueryClient} from "@tanstack/react-query";
import {fetchData} from "./query/FetchUtil.ts";

export const preloadData = async (employeeId: number, role: UserRole, queryClient: QueryClient ) => {
    try {
        // Preload employee data
        await queryClient.prefetchQuery({
            queryKey: ['employee', { id: employeeId }],
            queryFn: () => fetchData('employee', { id: employeeId })
        });

        // Preload payroll data
        await queryClient.prefetchQuery({
            queryKey: ['payrollsByEmployeeId', employeeId],
            queryFn: () => fetchData('payrollsByEmployeeId', { id: employeeId })
        });

        // Preload attendance data
        await queryClient.prefetchQuery({
            queryKey: ['attendancesByEmployeeId', employeeId],
            queryFn: () => fetchData('attendancesByEmployeeId', { id: employeeId })
        });

    } catch (error) {
        console.error('Error prefetching data:', error);
    }

    if (role === 'admin'){
        //Preload User data
        // await queryClient.prefetchQuery({
        //     queryKey: ['users'],
        //     queryFn: () => fetchData('users')
        // });
    }

    if (role === 'hr' || role === 'admin' || role === 'payroll') {
        // Preload employee data
        await queryClient.prefetchQuery({
            queryKey: ['employees'],
            queryFn: () => fetchData('employees')
        });

        // Preload supervisor data
        await queryClient.prefetchQuery({
            queryKey: ['supervisors'],
            queryFn: () => fetchData('supervisors')
        });

        // Preload positions data
        await queryClient.prefetchQuery({
            queryKey: ['positions'],
            queryFn: () => fetchData('positions')
        });

        // Preload departments data
        await queryClient.prefetchQuery({
            queryKey: ['departments'],
            queryFn: () => fetchData('departments')
        });

        // Preload employment statuses data
        await queryClient.prefetchQuery({
            queryKey: ['employmentStatuses'],
            queryFn: () => fetchData('employmentStatuses')
        });

        // Preload payrolls data
        await queryClient.prefetchQuery({
            queryKey: ['payrolls'],
            queryFn: () => fetchData('payrolls')
        });
    }
};