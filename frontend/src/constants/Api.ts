

//Local API - "http://localhost:8080/api/"
export const BASE_API = "http://localhost:8080/api"

export const API = {
  EMPLOYEES: {
    BASE: "/employee/",
    ALL: "/employees",
  },
  ATTENDANCES: {
    BASE: "/attendances",
    SUMMARY: "/attendances/summary",
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
    YEARS: "/years"
  },
  USERS: {
    BASE:  "/users/",
    ALL: "/users",
    AUTH: "/users/authenticateUser"
  },
  COMPANY: {
    BASE: "/company",
    POSITIONS: "/company/positions/",
    DEPARTMENTS: "/company/departments/",
    STATUSES: "/company/statuses/",
  }
}