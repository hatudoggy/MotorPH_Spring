import { Visibility, VisibilityOff, Whatshot } from "@mui/icons-material";
import { Button, Container, IconButton, InputAdornment, Paper, Stack, TextField, TextFieldProps, Typography } from "@mui/material";
import { useState } from "react";
import { useAuth } from "../hooks/AuthProvider";
import { useNavigate } from "react-router-dom";


export default function Login() {

  const {login} = useAuth()

  const navigate = useNavigate()

  const [username, setUsername] = useState("")
  const [password, setPassword] = useState("")
  const [isError, setIsError] = useState(false)


  const handleLogin = async() => {
    const auth = await login(username, password)
    if(auth) {
      navigate('../dashboard')
    } else {
      setIsError(true)
    }
  }



  return(
    <Container
      sx={{
        height: '100vh'
      }}
    >
      <Stack height='100%' justifyContent='center' alignItems='center'>
        <Paper
          elevation={8}
          sx={{
            px: 4,
            py: 5,
            width: 400,
            borderRadius: 3
          }}
        >
          <Stack gap={4}>
          <Typography variant="h4" fontWeight={500} textAlign='center'>MotorPH<Whatshot fontSize="inherit"/></Typography>
            <Stack gap={1.5}>
              <TextField
                label="Username"
                value={username}
                onChange={(e)=>setUsername(e.target.value)}
              />
              <PasswordField
                label="Password"
                value={password}
                type="password"
                onChange={(e)=>setPassword(e.target.value)}
              />
            </Stack>
            <Button 
              variant="contained"
              sx={{
                py: 1
              }}
              onClick={handleLogin}
            >
              Login
            </Button>
          </Stack>
        </Paper>
      </Stack>
      </Container>
  )
}


type PasswordField = {} & TextFieldProps

function PasswordField({ InputProps, ...props}: PasswordField) {

  const [show, setShow] = useState(false)

  return(
    <TextField
      {...props}
      InputProps={{
        endAdornment: (
          <InputAdornment position="end">
            <IconButton onClick={()=>setShow(!show)}>
              {show ? <Visibility /> : <VisibilityOff />}
            </IconButton>
          </InputAdornment>
        ),
        ...InputProps
      }}
      type={show ? "text" : "password"}
    />
  )
}