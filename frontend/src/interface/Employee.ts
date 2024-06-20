

interface EmployeeRes {
  employeeId: number
  firstName: string
  lastName: string
  dob: string
  address: string
  contacts: Contacts[]
  benefits: Benefits[]
  employment: Employment
  governmentId: GovernmentId
}


interface Contacts {
  contact_id: number
  contactNo: string
}

interface Benefits {
  benefitId: number
  amount: number
  benefitType: BenefitType
}

interface BenefitType {
  benefitTypeId: number
  benefit: string
}

interface Employment {
  employeeId: number
  department: Department
  position: Position
  status: Status
  supervisor: Supervisor
  hireDate: string
  basicSalary: number
  semiMonthlyRate: number
  hourlyRate: number
}

interface Department {
  departmentCode: string
  departmentName: string
}

interface Position {
  positionCode: string
  positionName: string
}

interface Status {
  statusId: number
  status: string
}

interface Supervisor {
  id: number
  firstName: string
  lastName: string
}

interface GovernmentId {
  employeeId: number
  sssNo: string
  philHealthNo: string
  pagIbigNo: string
  tinNo: string
}