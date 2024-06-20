
interface PayrollRes {
  payrollId: number
  employeeId: number
  periodStart: string
  periodEnd: string
  monthlyRate: number
  dailyRate: number
  overtimePay: number
  grossIncome: number
  netIncome: number
}

interface PayrollFull {
  payrollId: number
  periodStart: string
  periodEnd: string
  monthlyRate: number
  dailyRate: number
  overtimePay: number
  grossIncome: number
  netIncome: number
  deductions: Deductions[]
  employee: EmployeeRes
}

interface Deductions {
  deductionId: number
  amount: number
  deductionType: DeductionType
}

interface DeductionType {
  deductionCode: string
  name: string
  description: string
}