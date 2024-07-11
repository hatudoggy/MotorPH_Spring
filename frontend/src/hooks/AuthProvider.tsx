import { ReactNode, createContext, useContext, useEffect, useState } from "react";
import { API, BASE_API } from "../api/Api.ts";
import axios from "axios";



interface AuthPayload {
  authUser: Auth | null
  login: (username: string, password: string) => Promise<boolean>
  logout: () => void
  loading: boolean
}

const AuthContext = createContext<AuthPayload | null>(null)
export const useAuth = () => {
  return useContext(AuthContext) as AuthPayload;
}

export function AuthProvider({children}: {children: ReactNode}) {

  const [authUser, setAuthUser] = useState<Auth | null>(null)
  const [loading, setLoading] = useState(true)

  const validateCredentials = async (username: string, password: string) => {
    const {USERS} = API
    const {data} = await axios.post<Auth>(BASE_API + USERS.AUTH, {
      username: username,
      password: password
    })
    return data
  }

  const saveAuth = (auth: Auth) => {
    if(auth != null){
      localStorage.setItem("auth", JSON.stringify(auth))
    }
  }

  const getAuth = () => {
    const auth = localStorage.getItem("auth")
    if(auth){
      return JSON.parse(auth) as Auth
    } else {
      return null
    }
  }

  const clearAuth = () => {
    localStorage.removeItem("auth")
  }

  const login = async (username: string, password: string) => {
    try {
      const auth = await validateCredentials(username, password)
      if(auth) {
        setAuthUser(auth)
        saveAuth(auth)
        return true
      } else {
        return false
      }
    } catch (err) {
      console.error(err)
      return false
    }
  }

  const logout = () => {
    setAuthUser(null)
    clearAuth()
  }

  useEffect(() => {
    const auth = getAuth()
    if(auth){
      setAuthUser(auth)
    }
    setLoading(false)
  }, [])

  const value: AuthPayload = {
    authUser,
    login,
    logout,
    loading
  };

  return(
    <>
      <AuthContext.Provider value={value}>
        {children}
      </AuthContext.Provider>
    </>
  )
}