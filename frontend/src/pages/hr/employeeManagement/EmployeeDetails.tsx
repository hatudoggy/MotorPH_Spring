import {useState} from "react";
import {API, BASE_API} from "../../../api/Api.ts";
import axios from "axios";
import {keepPreviousData, useQuery} from "@tanstack/react-query";
import {Avatar, Box, CircularProgress, DialogContent, IconButton, Stack, Tab, Typography} from "@mui/material";
import {ArrowBack} from "@mui/icons-material";
import {TabContext, TabList, TabPanel} from "@mui/lab";
import ReadonlyTextField from "../../../components/ReadOnlyTextField.tsx";
import {calculateAge, formatterWhole, idformatter} from "../../../utils/utils.ts";
import {format} from "date-fns";

interface EmployeeDetailsDialog {
    selectedEmployee: number | null
    onClose: () => void
}

export default function EmployeeDetailsDialog({ selectedEmployee, onClose }: EmployeeDetailsDialog) {
    const [value, setValue] = useState("1");

    const fetchEmployee = async () => {
        const { EMPLOYEES } = API;
        const res = await axios.get(BASE_API + EMPLOYEES.BASE + selectedEmployee);
        return res.data;
    };

    const { isPending, data } = useQuery<EmployeeFullRes>({
        queryKey: ['employee', selectedEmployee],
        queryFn: fetchEmployee,
        placeholderData: keepPreviousData,
    });

    const picURL = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT3ZsJ_-wh-pIJV2hEL92vKyS07J3Hfp1USqA&s";

    return (
        <>
            <DialogContent>
                <Stack direction="row" position='absolute' left={10} top={10}>
                    <IconButton onClick={onClose}>
                        <ArrowBack />
                    </IconButton>
                </Stack>
                {!isPending ? (
                    data && (
                        <Stack alignItems='center' gap={1}>
                            <Avatar
                                sx={{
                                    width: 130,
                                    height: 130,
                                }}
                                src={picURL}
                            />
                            <Stack alignItems='center'>
                                <Typography variant="body2" fontSize={24} fontWeight={500} noWrap>{`${data.firstName} ${data.lastName}`}</Typography>
                                <Typography variant="body2" fontSize={17} color='GrayText' noWrap>{data.position.positionName}</Typography>
                            </Stack>

                            <TabContext value={value}>
                                <Box sx={{ width: '100%', borderBottom: 1, borderColor: 'divider' }}>
                                    <TabList
                                        onChange={(_e, val) => setValue(val)}
                                    >
                                        <Tab label="Info" value="1" />
                                        <Tab label="Employment" value="2" />
                                        <Tab label="Government IDs" value="3" />
                                        <Tab label="Salary" value="4" />
                                    </TabList>
                                </Box>
                                <Box width='100%' mb={3}>
                                    <TabPanel value="1">
                                        <Stack gap={3}>
                                            <Stack direction='row' gap={3}>
                                                <ReadonlyTextField
                                                    label="First Name"
                                                    defaultValue={data.firstName}
                                                    fullWidth
                                                />
                                                <ReadonlyTextField
                                                    label="Last Name"
                                                    defaultValue={data.lastName}
                                                    fullWidth
                                                />
                                            </Stack>
                                            <Stack direction='row' gap={3}>
                                                <ReadonlyTextField
                                                    label="Age"
                                                    defaultValue={calculateAge(data.dob)}
                                                    fullWidth
                                                />
                                                <ReadonlyTextField
                                                    label="Birthdate"
                                                    defaultValue={format(data.dob,'MMM dd, yyyy')}
                                                    fullWidth
                                                />
                                            </Stack>
                                            <ReadonlyTextField
                                                label="Contact Number"
                                                defaultValue={data.contacts[0].contactNo}
                                                fullWidth
                                            />
                                            <ReadonlyTextField
                                                label="Email Address"
                                                defaultValue={"N/A"}
                                                fullWidth
                                            />
                                            <ReadonlyTextField
                                                label="Address"
                                                defaultValue={data.address}
                                                fullWidth
                                            />
                                        </Stack>
                                    </TabPanel>

                                    <TabPanel value="2">
                                        <Stack gap={3}>
                                            <Stack direction='row' gap={3}>
                                                <ReadonlyTextField
                                                    label="Position"
                                                    defaultValue={data.position.positionName}
                                                    fullWidth
                                                />
                                                <ReadonlyTextField
                                                    label="Department"
                                                    defaultValue={data.department.departmentName}
                                                    fullWidth
                                                />
                                            </Stack>
                                            <ReadonlyTextField
                                                label="Supervisor"
                                                defaultValue={`${data.supervisor.firstName} ${data.supervisor.lastName}`}
                                                fullWidth
                                            />
                                            <Stack direction='row' gap={3}>
                                                <ReadonlyTextField
                                                    label="Hire Date"
                                                    defaultValue={format(data.hireDate, "MMM dd, yyyy")}
                                                    fullWidth
                                                />
                                                <ReadonlyTextField
                                                    label="Employment Status"
                                                    defaultValue={data.status.statusName}
                                                    fullWidth
                                                />
                                            </Stack>
                                        </Stack>
                                    </TabPanel>

                                    <TabPanel value="3">
                                        <Stack gap={3}>
                                            <ReadonlyTextField
                                                label="Tin Number"
                                                defaultValue={idformatter(data.governmentId.tinNo, 'tin')}
                                                fullWidth
                                            />
                                            <ReadonlyTextField
                                                label="SSS Number"
                                                defaultValue={idformatter(data.governmentId.sssNo, 'sss')}
                                                fullWidth
                                            />
                                            <ReadonlyTextField
                                                label="Philhealth Number"
                                                defaultValue={idformatter(data.governmentId.philHealthNo, 'philhealth')}
                                                fullWidth
                                            />
                                            <ReadonlyTextField
                                                label="Pagibig Number"
                                                defaultValue={idformatter(data.governmentId.pagIbigNo, 'pagibig')}
                                                fullWidth
                                            />
                                        </Stack>
                                    </TabPanel>

                                    <TabPanel value="4">
                                        <Stack gap={3}>
                                            <ReadonlyTextField
                                                label="Base Salary"
                                                defaultValue={formatterWhole.format(data.basicSalary)}
                                                fullWidth
                                            />
                                            {data.benefits.map((item) =>
                                                <ReadonlyTextField
                                                    key={item.benefitId}
                                                    label={item.benefitType.benefit}
                                                    defaultValue={formatterWhole.format(item.amount)}
                                                    fullWidth
                                                />
                                            )}
                                        </Stack>
                                    </TabPanel>
                                </Box>
                            </TabContext>
                        </Stack>
                    )
                ) : (
                    <Stack alignItems='center' justifyContent='center' minHeight={300}>
                        <CircularProgress />
                    </Stack>
                )}
            </DialogContent>
        </>
    );
}

