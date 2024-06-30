import {Control, UseFormRegister, UseFormSetValue, UseFormWatch} from "react-hook-form";
import {Box, InputAdornment, TextField, Typography} from "@mui/material";
import {Badge, Payments, Person, Phone, Work} from "@mui/icons-material";
import {API, BASE_API} from "../../../../../api/Api.ts";
import axios from "axios";
import {keepPreviousData, useQuery} from "@tanstack/react-query";
import Dropdown, {DateSelect, FormWidget, HeadIcon, TextComplete} from "./EmployeeFormUtils.tsx";
import {Inputs} from "../EmployeeForm.tsx";

interface FormArea {
    register: UseFormRegister<Inputs>
    control: Control<Inputs, any>
    watch?: UseFormWatch<Inputs>
    setValue?: UseFormSetValue<Inputs>
    selectedId?: number | null
}

/**
 * Component for displaying and editing basic information of an employee.
 *
 * @param {Object} props - The component props.
 * @param {UseFormRegister<Inputs>} props.register - The register function from react-hook-form.
 * @param {Control<Inputs, any>} props.control - The control object from react-hook-form.
 * @param {number | null} [props.selectedId] - The ID of the selected employee.
 * @returns {JSX.Element} The rendered component.
 */
export default function BasicInfoArea({
                                          register,
                                          control,
                                          selectedId,
                                      }: FormArea): JSX.Element {
    return (
        <Box>
            {/* Display the "Basic Info" icon */}

            <HeadIcon Icon={Person}>Basic Info</HeadIcon>

            {/* Form widget for basic information */}
            <FormWidget>

                {/* First name input field */}
                <TextField
                    {...register("firstName")}
                    label="First Name"
                    variant="standard"
                    fullWidth
                    InputLabelProps={{ shrink: true }}
                />

                {/* Last name input field */}
                <TextField
                    {...register("lastName")}
                    label="Last Name"
                    variant="standard"
                    fullWidth
                    InputLabelProps={{ shrink: true }}
                />

                {/* Birth date input field */}
                <DateSelect
                    label="Birth Date"
                    control={control}
                    name="dob"
                />

                {/* Address input field */}
                <TextField
                    {...register("address")}
                    label="Address"
                    variant="standard"
                    fullWidth
                    InputLabelProps={{ shrink: true }}
                />
            </FormWidget>
        </Box>
    );
}

/**
 * Component for displaying contact numbers.
 *
 * @param {FormArea} props - The component props.
 * @param {UseFormRegister<Inputs>} props.register - The register function from react-hook-form.
 * @returns {JSX.Element} The rendered component.
 */
export function ContactNumbersArea({ register }: FormArea) {
    // Render the component
    return (
        <Box>
            {/* Display the "Contact Numbers" icon */}
            <HeadIcon Icon={Phone}>Contact Numbers</HeadIcon>

            {/* Form widget for contact numbers */}
            <FormWidget>
                {/* Add contact number input fields here */}
            </FormWidget>
        </Box>
    );
}

export function EmploymentInfoArea({control, watch, setValue}: FormArea) {

    const departmentCode = watch && watch("departmentCode")


    const departments = useQuery<DepartmentRes[]>({
        queryKey: ['departments'],
        queryFn: fetchAllDepartments
    })

    const positions = useQuery<(PositionRes & {departmentCode: string})[]>({
        queryKey: ['positions', departmentCode],
        queryFn: fetchAllPositions,
        enabled: !!departmentCode
    })

    const statuses = useQuery<EmploymentStatusRes[]>({
        queryKey: ['statuses'],
        queryFn: fetchAllStatuses
    })

    const supervisors = useQuery<SupervisorRes[]>({
        queryKey: ['supervisors'],
        queryFn: fetchSupervisors,
        placeholderData: keepPreviousData
    })

    const departmentOptions = departments.data
        && departments.data.map(({departmentCode, departmentName})=>({value: departmentCode, label: departmentName}))

    const positionOptions = positions.data
        && positions.data.map(({positionCode, positionName})=>({value: positionCode, label: positionName}))

    const statusOptions = statuses.data
        && statuses.data.map(({statusId, statusName})=>({value: statusId, label: statusName}))

    const supervisorOptions = supervisors.data
        && supervisors.data.map(({supervisorId, firstName, lastName}) => ({value: supervisorId, label:`${firstName} ${lastName}`}))

    return(
        <Box>
            <HeadIcon Icon={Work}>Employment Info</HeadIcon>
            <FormWidget>
                <Dropdown
                    variant="standard"
                    label="Department"
                    options={departmentOptions || []}
                    changeTrigger={()=>{setValue && setValue('positionCode', '')}}
                    control={control}
                    name="departmentCode"
                />
                <Dropdown
                    variant="standard"
                    label="Position"
                    options={positionOptions || []}
                    disabled={!departmentCode}
                    control={control}
                    name="positionCode"
                />
                <TextComplete
                    name="supervisor"
                    control={control}
                    label="Supervisor"
                    options={supervisorOptions || []}
                />
                <DateSelect
                    label="Hire Date"
                    control={control}
                    name="hireDate"
                />
                <Dropdown
                    variant="standard"
                    label="Employment Status"
                    options={statusOptions || []}
                    control={control}
                    name="statusId"
                />
            </FormWidget>
        </Box>
    )
}

export function ContributionIdsArea({register, selectedId}: FormArea) {

    return(
        <Box>
            <HeadIcon Icon={Badge}>Contribution Ids</HeadIcon>
            <FormWidget>
                <TextField
                    {...register("tinNo")}
                    label="Tin Number"
                    variant="standard"
                    fullWidth
                    InputLabelProps={{ shrink: true }}
                />
                <TextField
                    {...register("sssNo")}
                    label="SSS Number"
                    variant="standard"
                    fullWidth
                    InputLabelProps={{ shrink: true }}
                />
                <TextField
                    {...register("philHealthNo")}
                    label="Philhealth Number"
                    variant="standard"
                    fullWidth
                    InputLabelProps={{ shrink: true }}
                />
                <TextField
                    {...register("pagIbigNo")}
                    label="Pagibig Number"
                    variant="standard"
                    fullWidth
                    InputLabelProps={{ shrink: true }}
                />
            </FormWidget>
        </Box>
    )
}

export function SalaryBenefitsArea({register}: FormArea) {

    return(
        <Box>
            <HeadIcon Icon={Payments}>Salary & Benefits</HeadIcon>
            <FormWidget>
                <TextField
                    {...register("basicSalary")}
                    label="Basic Salary"
                    variant="standard"
                    fullWidth
                    InputLabelProps={{ shrink: true }}
                    InputProps={{
                        startAdornment: (
                            <InputAdornment position="start">
                                <Typography>₱</Typography>
                            </InputAdornment>
                        )
                    }}
                />
            </FormWidget>
        </Box>
    )
}
