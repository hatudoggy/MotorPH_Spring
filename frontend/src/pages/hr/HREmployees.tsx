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

export default function HREmployees() {
    const [search, setSearch] = useState('');
    const debouncedSearch = useDebounce(search, 300);
    const {isPending, isRefetching, data} = useFetchEmployees();
    const [openSelectDialog, setOpenSelectDialog] = useState(false);
    const [openCUDialog, setOpenCUDialog] = useState<null | 'add' | 'edit'>(null);
    const [selectedEmployeeId, setSelectedEmployeeId] = useState<number | null>(null);
    const [refetchingEmployeeId, setRefetchingEmployeeId] = useState<number | null>(null);

    const handleSelectEmployee = (employeeId: number) => {
        setOpenSelectDialog(true);
        setSelectedEmployeeId(employeeId);
        setRefetchingEmployeeId(employeeId);
    };

    const handleCUEmployee = (type: 'add' | 'edit', employeeId?: number) => {
        setOpenCUDialog(type);
        if (employeeId) {
            setSelectedEmployeeId(employeeId);
        }
    };

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

    const handleCloseSelectDialog = () => {
        setOpenSelectDialog(false);
        setSelectedEmployeeId(null);
    };

    const handleCloseCUDialog = () => {
        setOpenCUDialog(null);
        setSelectedEmployeeId(null);
    };

    return (
            <>
            <Container sx={{ my: 5 }}>
                <Headertext>HR Employees</Headertext>
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
                                    sx: { borderRadius: 3 },
                                }}
                                sx={{ bgcolor: 'white', width: 300 }}
                                value={search}
                                onChange={(e) => setSearch(e.target.value)}
                            />
                        </Box>
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
                                    isLoading={isRefetching && refetchingEmployeeId === item.employeeId}
                                    isSelected={selectedEmployeeId === item.employeeId}
                                />
                            ))
                        ) : (
                            <Stack width="100%" height="100%" alignItems="center" justifyContent="center">
                                <CircularProgress/>
                            </Stack>
                        )}
                    </Stack>
                </Stack>
            </Container>
            <Dialog
                open={openSelectDialog}
                onClose={() => handleCloseSelectDialog()}
                TransitionProps={{
                    onExit: () => setSelectedEmployeeId(null),
                }}
                fullWidth
                maxWidth="sm"
                PaperProps={{ sx: { borderRadius: 3 } }}
            >
                <EmployeeDetailsDialog selectedEmployeeId={selectedEmployeeId} onClose={() => handleCloseSelectDialog()}/>
            </Dialog>
            <Dialog
                open={openCUDialog !== null}
                onClose={() => handleCloseCUDialog()}
                TransitionProps={{
                    onExit: () => setSelectedEmployeeId(null),
                }}
                fullWidth
                maxWidth="xs"
                PaperProps={{ sx: { borderRadius: 3 } }}
            >
                {openCUDialog && (
                    <EmployeeFormDialog type={openCUDialog} selectedId={selectedEmployeeId} onClose={() => handleCloseCUDialog()}/>
                )}
            </Dialog>
        </>
    );
}
