

//Local API - "http://localhost:8080/api/"
export const BASE_API = "http://localhost:8080/api"

export const API = {
  EMPLOYEES: {
    BASE: "/employee/",
    ALL: "/employee/all",
    UPDATE: "/employee/update/",
    REGISTER: "/employee/register",
  },
  ATTENDANCES: {
    BASE: "/attendances",
    DATE: "/attendances/date",
    RANGE: "/attendances/dateRange",
    TIME_IN: "/attendances/timeIn",
    TIME_OUT: "/attendances/timeOut",
  },
  LEAVES: {
    BASE: "/leaves/",
    ALL: "/leaves",
  },
  PAYROLLS: {
    BASE: "/payrolls/",
    ALL: "/payrolls",
    YEARS: "/years",
    GENERATE: "/payrolls/generate",
  },
  USERS: {
    BASE:  "/users/",
    ALL: "/users",
    AUTH: "/users/authenticateUser"
  },
  COMPANY: {
    BASE: "/company",
    SUPERVISORS: "/company/supervisors/",
    POSITIONS: "/company/positions/",
    DEPARTMENTS: "/company/departments/",
    STATUSES: "/company/statuses/",
    BENEFITS: "/company/benefits/",
    LEAVE_TYPES: "/company/leave/types",
    LEAVE_STATUS: "/company/leave/status",
  }
}