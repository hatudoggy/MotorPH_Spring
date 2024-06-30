import {Box, Button, CircularProgress, Container, Dialog, InputAdornment, Stack, TextField} from "@mui/material";
import Headertext from "../../components/HeaderText";
import { Add, FileCopy, Search } from "@mui/icons-material";
import { useMemo, useState} from "react";
import { useDebounce } from "@uidotdev/usehooks";
import { format } from "date-fns";
import {useFetchEmployees} from "../../api/query/UseFetch.ts";
import EmployeeDetailsDialog from "./employeeManagement/EmployeeDetails.tsx";
import EmployeeCard from "./employeeManagement/EmployeeCard.tsx";
import EmployeeFormDialog from "./employeeManagement/form/EmployeeForm.tsx";

/**
 * Component for displaying and managing HR employees.
 *
 * @returns {JSX.Element} The HR employees component.
 */
export default function HREmployees() {
    // State for the search input
    const [search, setSearch] = useState('');

    // Debounced search value
    const debouncedSearch = useDebounce(search, 300);

    // Fetch employees data from server
    const {isPending, data} = useFetchEmployees();

    // State for the select employee dialog
    const [openSelectDialog, setOpenSelectDialog] = useState(false);

    // State for the create/update employee dialog
    const [openCUDialog, setOpenCUDialog] = useState<null | 'add' | 'edit'>(null);

    // State for the currently selected employee
    const [selectedEmployee, setSelectedEmployee] = useState<number | null>(null);

    /**
     * Handles selecting an employee.
     *
     * @param {number} employeeId - The ID of the employee to select.
     */
    const handleSelectEmployee = (employeeId: number) => {
        setOpenSelectDialog(true);
        setSelectedEmployee(employeeId);
    };

    /**
     * Handles creating or updating an employee.
     *
     * @param {'add' | 'edit'} type - The type of operation to perform.
     * @param {number} [employeeId] - The ID of the employee to update (optional).
     */
    const handleCUEmployee = (type: 'add' | 'edit', employeeId?: number) => {
        setOpenCUDialog(type);
        if (employeeId) {
            setSelectedEmployee(employeeId);
        }
    };


    /**
     *  Filters employee data based on search term
     *
     *  @param {Array} data - The original employee data
     *  @param {string} debouncedSearch - The debounced search term entered by the user
     *  @returns {Array} - The filtered employee data based on search term
     */
    const filteredData = useMemo(() => {
        if (!data) return [];

        return data.filter((item) => {
            const fullName = `${item.firstName} ${item.lastName}`.toLowerCase();
            const searchTerm = debouncedSearch.toLowerCase();
            return fullName.includes(searchTerm) ||
                item.position.positionName.toLowerCase().includes(searchTerm) ||
                item.department.departmentName.toLowerCase().includes(searchTerm);
        });
    }, [data, debouncedSearch]);

    // JSX code for the component
    return (
        <>
            <Container
                sx={{
                    my: 5,
                }}
            >
                <Headertext>HR Employees</Headertext>
                {/* Search bar */}
                <Stack gap={2}>
                    <Stack direction="row" justifyContent='space-between'>
                        <Box>
                            <TextField
                                placeholder="Search"
                                size="small"
                                InputProps={{
                                    startAdornment: (
                                        <InputAdornment position="start">
                                            <Search/>
                                        </InputAdornment>
                                    ),
                                    sx: {
                                        borderRadius: 3,
                                    },
                                }}
                                sx={{
                                    bgcolor: 'white',
                                    width: 300,
                                }}
                                value={search}
                                onChange={(e) => setSearch(e.target.value)}
                            />
                        </Box>
                        {/* Export and New Employee buttons */}
                        <Stack direction='row' gap={1}>
                            <Button
                                variant="outlined"
                                startIcon={<FileCopy/>}
                            >
                                Export Data
                            </Button>
                            <Button
                                variant="contained"
                                startIcon={<Add/>}
                                disableElevation
                                onClick={() => handleCUEmployee("add")}
                            >
                                New Employee
                            </Button>
                        </Stack>
                    </Stack>
                    {/* Employee list */}
                    <Stack direction="row" flexWrap='wrap' gap={2}>
                        {!isPending ? (
                            filteredData.map((item) => (
                                <EmployeeCard
                                    key={item.employeeId}
                                    name={`${item.firstName} ${item.lastName}`}
                                    position={item.position.positionName}
                                    department={item.department.departmentCode}
                                    status={item.status}
                                    hireDate={format(item.hireDate, "MMM dd, yyyy")}
                                    onClick={() => handleSelectEmployee(item.employeeId)}
                                    onEdit={() => handleCUEmployee("edit", item.employeeId)}
                                />
                            ))
                        ) : (
                            <Stack width='100%' alignItems='center'>
                                <CircularProgress/>
                            </Stack>
                        )}
                    </Stack>
                </Stack>
            </Container>
            {/* Select Employee Dialog */}
            <Dialog
                open={openSelectDialog}
                onClose={() => setOpenSelectDialog(false)}
                TransitionProps={{
                    onExit: () => setSelectedEmployee(null),
                }}
                fullWidth
                maxWidth="sm"
                PaperProps={{
                    sx: {
                        borderRadius: 3,
                    },
                }}
            >
                <EmployeeDetailsDialog selectedEmployee={selectedEmployee} onClose={() => setOpenSelectDialog(false)}/>
            </Dialog>
            {/* Create/Update Employee Dialog */}
            <Dialog
                open={openCUDialog !== null}
                onClose={() => setOpenCUDialog(null)}
                TransitionProps={{
                    onExit: () => setSelectedEmployee(null),
                }}
                fullWidth
                maxWidth="xs"
                PaperProps={{
                    sx: {
                        borderRadius: 3,
                    },
                }}
            >
                {openCUDialog && (
                    <EmployeeFormDialog type={openCUDialog} selectedId={selectedEmployee}
                                        onClose={() => setOpenCUDialog(null)}/>
                )}
            </Dialog>
        </>
    );
}