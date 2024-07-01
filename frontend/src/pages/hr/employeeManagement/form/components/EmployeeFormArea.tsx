import {Control, UseFormRegister, UseFormSetValue, UseFormWatch} from "react-hook-form";
import {Box, IconButton, InputAdornment, TextField, Typography} from "@mui/material";
import {AddCircle, Badge, Circle, Payments, Person, Phone, TripOrigin, Work} from "@mui/icons-material";
import {API, BASE_API} from "../../../../../api/Api.ts";
import axios from "axios";
import {keepPreviousData, useQuery} from "@tanstack/react-query";
import Dropdown, {DateSelect, FormWidget, HeadIcon, TextComplete} from "./EmployeeFormUtils.tsx";
import {Inputs} from "../EmployeeForm.tsx";
import {
    useFetchDepartments,
    useFetchEmployees,
    useFetchEmploymentStatuses,
    useFetchPositions, useFetchSupervisors
} from "../../../../../api/query/UseFetch.ts";
import {useState} from "react";

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
    const [numFields, setNumFields] = useState<number>(1); // Start with 1 field initially

    const handleAddField = () => {
        setNumFields(numFields + 1);
    };

    return (
        <Box>
            <HeadIcon Icon={Phone}>Contact Numbers</HeadIcon>
            <FormWidget>
                {/* Render initial field */}
                <TextField
                    {...register(`contacts.${0}.contactNumber`)}
                    label="Contact Number 1"
                    variant="standard"
                    fullWidth
                    InputLabelProps={{ shrink: true }}
                />

                {/* Render additional fields dynamically */}
                {[...Array(numFields - 1)].map((_, index) => (
                    <TextField
                        key={index + 1}
                        {...register(`contacts.${index + 1}`)}
                        label={`Contact Number ${index + 2}`}
                        variant="standard"
                        fullWidth
                        InputLabelProps={{ shrink: true }}
                    />
                ))}

                {/* Button to add more fields */}
                {numFields < 3 && (  // Limit to 3 fields, adjust as needed
                    <TextField
                        onClick={handleAddField}
                        label="Add Contact Number"
                        variant="standard"
                        fullWidth
                        InputLabelProps={{ shrink: true }}
                        InputProps={{
                            endAdornment: numFields < 3 && (  // Limit to 3 fields, adjust as needed
                                <IconButton onClick={handleAddField}>
                                    <AddCircle />
                                </IconButton>
                            ),
                        }}
                    />
                )}
            </FormWidget>
        </Box>
    );
}

export function EmploymentInfoArea({ control, watch, setValue }: FormArea) {
    const departmentCode = watch && watch("departmentCode");

    const { data: departmentsData } = useFetchDepartments();
    const { data: positionsData } = useFetchPositions();
    const { data: supervisorsData } = useFetchSupervisors();
    const { data: statusesData } = useFetchEmploymentStatuses();

    const departmentOptions = departmentsData?.map(({ departmentCode, departmentName }) => ({
        value: departmentCode,
        label: departmentName,
    }));

    const filteredPositionOptions = positionsData
        ?.filter(({ departmentCode: posDeptCode }) => posDeptCode === departmentCode)
        .map(({ positionCode, positionName }) => ({
            value: positionCode,
            label: positionName,
        }));

    const filteredSupervisorOptions = supervisorsData
        ?.filter(({position: { departmentCode: supDeptCode } }) => supDeptCode === departmentCode)
        .map(({ supervisorId, firstName, lastName }) => ({
            value: supervisorId,
            label: `${firstName} ${lastName}`,
        }));

    const statusOptions = statusesData?.map(({ statusId, statusName }) => ({
        value: statusId,
        label: statusName,
    }));

    return (
        <Box>
            <HeadIcon Icon={Work}>Employment Info</HeadIcon>
            <FormWidget>
                <Dropdown
                    variant="standard"
                    label="Department"
                    options={departmentOptions || []}
                    changeTrigger={() => {
                        setValue && setValue("positionCode", "");
                        setValue && setValue("supervisor", null);
                    }}
                    control={control}
                    name="departmentCode"
                />
                <Dropdown
                    variant="standard"
                    label="Position"
                    options={filteredPositionOptions || []}
                    disabled={!departmentCode}
                    control={control}
                    name="positionCode"
                />
                <TextComplete
                    name="supervisor"
                    control={control}
                    label="Supervisor"
                    options={filteredSupervisorOptions || []}
                    disabled={!departmentCode}
                />
                <DateSelect label="Hire Date" control={control} name="hireDate" />
                <Dropdown
                    variant="standard"
                    label="Employment Status"
                    options={statusOptions || []}
                    control={control}
                    name="statusId"
                />
            </FormWidget>
        </Box>
    );
}


export function ContributionIdsArea({register}: FormArea) {

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

export function SalaryBenefitsArea({ register }: FormArea) {
    const benefits = [
        { label: "Rice Subsidy", benefitTypeId: 1 },
        { label: "Phone Allowance", benefitTypeId: 2 },
        { label: "Clothing Allowance", benefitTypeId: 3 }
    ];

    return (
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
                {benefits.map((benefit, index) => (
                    <Box key={index}>
                        <TextField
                            {...register(`benefits.${index}.amount`)}
                            label={benefit.label}
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
                        <input
                            type="hidden"
                            {...register(`benefits.${index}.benefitTypeId`)}
                            value={benefit.benefitTypeId}
                        />
                    </Box>
                ))}
            </FormWidget>
        </Box>
    );
}