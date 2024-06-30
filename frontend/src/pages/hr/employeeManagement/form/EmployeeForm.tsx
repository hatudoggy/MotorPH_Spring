import {useMutation, useQuery, useQueryClient} from "@tanstack/react-query";
import {API, BASE_API} from "../../../../api/Api.ts";
import axios from "axios";
import {SubmitHandler, useForm} from "react-hook-form";
import {useEffect} from "react";
import {format} from "date-fns";
import {Button, DialogActions, DialogContent, DialogTitle, Stack} from "@mui/material";
import BasicInfoArea, {
    ContributionIdsArea,
    EmploymentInfoArea,
    SalaryBenefitsArea
} from "./components/EmployeeFormArea.tsx";
import {TextCompleteOption} from "./components/EmployeeFormUtils.tsx";

interface BenifitInput {
    amount: number
    type: number
}

export interface Inputs {
    firstName: string
    lastName: string
    dob: Date | null
    address: string

    contacts: string[]
    benefits: BenifitInput[]

    departmentCode: string
    positionCode: string
    statusId: string
    supervisor: TextCompleteOption | null
    hireDate: Date | null

    sssNo: string
    philHealthNo: string
    pagIbigNo: string
    tinNo: string

    basicSalary: number
    semiMonthlyRate: number
    hourlyRate: number
}

interface EmployeeFormDialog {
    type: 'add' | 'edit'
    selectedId?: number | null
    onClose: () => void
}


export default function EmployeeFormDialog({type, selectedId, onClose}: EmployeeFormDialog) {

    const queryClient = useQueryClient()

    const fetchEmployee = async() => {
        const {EMPLOYEES} = API
        if(selectedId){
            const res = await axios.get(BASE_API + EMPLOYEES.BASE + selectedId)
            return res.data;
        } else {
            return null
        }
    }

    const {isPending, data} = useQuery<EmployeeFullRes>({
        queryKey: ['employeeEdit', selectedId],
        queryFn: fetchEmployee,
        enabled: !!selectedId
    })

    const supervisorData = data ? {
        value: data?.supervisor.supervisorId,
        label: `${data?.supervisor.firstName} ${data?.supervisor.lastName}`
    } : null

    const {
        register,
        control,
        watch,
        setValue,
        handleSubmit,
        reset
    } = useForm<Inputs>({
        defaultValues: {
            firstName: data?.firstName || "",
            lastName: data?.lastName || "",
            dob: data?.dob ? new Date(data.dob) : null,
            address: data?.address || "",

            contacts: [],
            benefits: data?.benefits || [],

            departmentCode: data?.department.departmentCode || "",
            positionCode: data?.position.positionCode || "",
            statusId: data?.status.statusId.toString() || "",
            supervisor: supervisorData || null,
            hireDate: data?.dob ? new Date(data.hireDate) : null,

            sssNo: data?.governmentId.sssNo || "",
            philHealthNo: data?.governmentId.philHealthNo || "",
            pagIbigNo: data?.governmentId.pagIbigNo || "",
            tinNo: data?.governmentId.tinNo || "",

            basicSalary: data?.basicSalary || 0,
            semiMonthlyRate: data?.semiMonthlyRate || 0,
            hourlyRate: data?.hourlyRate || 0,
        }
    })

    useEffect(()=>{
        if(selectedId && !isPending && data) {
            reset({
                firstName: data.firstName || "",
                lastName: data.lastName || "",
                dob: data.dob ? new Date(data.dob) : null,
                address: data.address || "",

                contacts: [],
                benefits: data.benefits || [],

                departmentCode: data.department.departmentCode || "",
                positionCode: data.position.positionCode || "",
                statusId: data.status.statusId.toString() || "",
                supervisor: supervisorData || null,
                hireDate: data.dob ? new Date(data.hireDate) : null,

                sssNo: data.governmentId.sssNo || "",
                philHealthNo: data.governmentId.philHealthNo || "",
                pagIbigNo: data.governmentId.pagIbigNo || "",
                tinNo: data.governmentId.tinNo || "",

                basicSalary: data.basicSalary || 0,
                semiMonthlyRate: data.semiMonthlyRate || 0,
                hourlyRate: data.hourlyRate || 0,
            })
        }
    }, [data, isPending])


    const addEmployee = async (employee: EmployeeReq) => {
        const {EMPLOYEES } = API
        const res = await axios.post(BASE_API + EMPLOYEES.ALL, employee);
        return res.data;
    }

    const useAddEmployee = useMutation({
        mutationFn: addEmployee,
        onSettled: async () => {
            return await queryClient.invalidateQueries({ queryKey: ['employeesAll']})
        }
    })

    const editEmployee = async (employee: EmployeeReq) => {
        const {EMPLOYEES } = API
        const res = await axios.patch(BASE_API + EMPLOYEES.BASE + selectedId, employee);
        return res.data;
    }

    const useEditEmployee = useMutation({
        mutationFn: editEmployee,
        onSettled: async () => {
            return await queryClient.invalidateQueries({ queryKey: ['employeesAll']})
        }
    })

    const onSubmit: SubmitHandler<Inputs> = (data) => {
        const formData: EmployeeReq = {
            lastName: data.lastName,
            firstName: data.firstName,
            dob: data.dob ? format(data.dob, 'yyyy-MM-dd') : "",
            address: data.address,
            contacts: [
                {
                    contactNumbers: "1234567890"
                },
                {
                    contactNumbers: ""
                }
            ],
            benefits: [
                {
                    amount: 1500,
                    benefitType: {
                        benefitTypeId: 1
                    }
                },
                {
                    amount: 2000,
                    benefitType: {
                        benefitTypeId: 2
                    }
                }
            ],
            employment: {
                department: {
                    departmentCode: data.departmentCode
                },
                position: {
                    positionCode: data.positionCode
                },
                status: {
                    statusId: Number(data.statusId)
                },
                hireDate: data.hireDate ? format(data.hireDate, 'yyyy-MM-dd') : "",
                basicSalary: data.basicSalary,
                semiMonthlyRate: data.semiMonthlyRate,
                hourlyRate: data.hourlyRate,
                supervisor: {
                    employeeId: Number(data.supervisor?.value)
                }
            },
            governmentId: {
                sssNo: data.sssNo,
                philHealthNo: data.philHealthNo,
                pagIbigNo: data.pagIbigNo,
                tinNo: data.tinNo
            }
        }

        if(selectedId){
            const formDataWId: EmployeeReq = {
                employeeId: selectedId,
                ...formData
            }
            useEditEmployee.mutate(formDataWId)
        } else {
            useAddEmployee.mutate(formData)
        }

        onClose()
    }

    return(
        <>
            <DialogTitle textTransform='capitalize'>
                {`${type} Employee`}
            </DialogTitle>
            <DialogContent>
                <Stack mb={4} gap={3}>
                    <BasicInfoArea register={register} control={control} selectedId={selectedId}/>
                    {/* <ContactNumbersArea register={register} control={control}/> */}
                    <EmploymentInfoArea register={register} control={control} watch={watch} setValue={setValue}/>
                    <ContributionIdsArea register={register} control={control} selectedId={selectedId}/>
                    <SalaryBenefitsArea register={register} control={control} />
                </Stack>
            </DialogContent>
            <DialogActions>
                <Button onClick={onClose}>Cancel</Button>
                <Button variant="contained" onClick={handleSubmit(onSubmit)}>Save</Button>
            </DialogActions>
        </>
    )
}