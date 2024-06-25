
interface PayrollRes {
  payrollId: number
  payDate: string
  employeeId: number
  firstName: string
  lastName: string
  periodStart: string
  periodEnd: string
  workingDays:number
  daysWorked: number
  monthlyRate: number
  hoursWorked: number
  hourlyRate: number
  overtimeHours: number
  overtimeRate: number
  overtimePay: number
  grossIncome: number
  totalBenefits: number
  totalDeductions: number
  netPay: number
  benefits: BenefitRes[]
  deductions: Deductions[]
}

interface PayrollFull {
  payrollId: number
  periodStart: string
  periodEnd: string
  monthlyRate: number
  dailyRate: number
  overtimePay: number
  grossIncome: number
  netPay: number
  deductions: Deductions[]
  employee: EmployeeRes
}

interface Deductions {
  deductionId: number
  payrollId: number
  amount: number
  deductionType: DeductionTypeRes
}

interface DeductionTypeRes {
  deductionCode: string
  name: string
  description: string
}