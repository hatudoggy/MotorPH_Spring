import { useEffect } from 'react';
import { Avatar, Container, InputAdornment, Paper, PaperProps, Stack, TextField, Typography, styled, CircularProgress } from "@mui/material";
import Headertext from "../../components/HeaderText";
import Table from "../../components/Table";

import { DatePicker } from "@mui/x-date-pickers";
import { useState, useMemo } from "react";
import { Search } from "@mui/icons-material";
import {useFetchAttendancesByDate} from "../../api/query/UseFetch.ts";

export default function HRAttendances() {
    const [dateFilter, setDateFilter] = useState<Date | null>(new Date())
    const [search, setSearch] = useState('')

    return(
        <Container sx={{ my: 5 }}>
            <Stack height='100%'>
                <Headertext>HR Attendance</Headertext>
                <Stack direction='row' mb={2} gap={1}>
                    <DatePicker
                        slotProps={{
                            textField: {
                                size: 'small',
                                sx: {
                                    bgcolor: 'white',
                                    width: 180
                                },
                                InputProps: {
                                    sx: {
                                        borderRadius: 2
                                    }
                                }
                            }
                        }}
                        value={dateFilter}
                        onChange={(val)=>setDateFilter(val)}
                    />
                    <TextField
                        placeholder="Search"
                        size="small"
                        InputProps={{
                            startAdornment: (
                                <InputAdornment position="start">
                                    <Search />
                                </InputAdornment>
                            ),
                            sx: {
                                borderRadius: 3
                            }
                        }}
                        sx={{
                            bgcolor: 'white',
                            width: 300
                        }}
                        value={search}
                        onChange={(e)=>setSearch(e.target.value)}
                    />
                </Stack>
                <AttendanceTable dateFilter={dateFilter} searchFilter={search} />
            </Stack>
        </Container>
    )
}

interface AttendanceTable {
    dateFilter: Date | null
    searchFilter: string
}

function AttendanceTable({ dateFilter, searchFilter }: AttendanceTable) {

    const { isLoading, data, refetch } = useFetchAttendancesByDate(dateFilter)

    useEffect(() => {
        if (!localStorage.getItem('attendanceData' + dateFilter)) {
            refetch();
        }
    }, [refetch]);

    const filteredData = useMemo(() => {
        if (!data) return [];
        return data.filter(attendance => {
            const fullName = `${attendance.employee.firstName} ${attendance.employee.lastName}`.toLowerCase();
            const searchLower = searchFilter.toLowerCase();
            return fullName.includes(searchLower) || attendance.attendanceId.toString().includes(searchLower);
        });
    }, [data, searchFilter]);

    const tableData = filteredData.map(({ attendanceId, ...rest }) => rest);

    const picURL = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT3ZsJ_-wh-pIJV2hEL92vKyS07J3Hfp1USqA&s";

    if (isLoading) {
        return (
            <Widget variant="outlined" sx={{ height: '100%', display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
                <CircularProgress />
            </Widget>
        );
    }

    return (
        <Widget variant="outlined" sx={{ height: '100%' }}>
            <Table
                colSizes={[3.5, true, true, true, true, true]}
                colHeader={[
                    "Employee",
                    "Date",
                    "Time In",
                    "Time Out",
                    "Hours Worked",
                    "Overtime",
                ]}
                tableData={tableData}
                rowHeight={70}
                renderers={{
                    employee: (item: EmployeeAttendanceInfo) => (
                        <Stack direction='row' alignItems='center' gap={1.5}>
                            <Avatar
                                sx={{
                                    height: 46,
                                    width: 46,
                                }}
                                src={picURL}
                            />
                            <Stack>
                                <Typography variant="body2" fontSize={16} fontWeight={500}>
                                    {`${item.firstName} ${item.lastName}`}
                                </Typography>
                                <Typography variant="body2" color='GrayText'>
                                    {item.position.positionName}
                                </Typography>
                            </Stack>
                        </Stack>
                    )
                }}
            />
        </Widget>
    );
}

const Widget = styled(Paper)<PaperProps>(({}) => ({
    borderRadius: 12,
    padding: 16,
}));
