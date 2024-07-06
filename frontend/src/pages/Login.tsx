import { Visibility, VisibilityOff, Whatshot } from "@mui/icons-material";
import { Button, Container, IconButton, InputAdornment, Paper, Stack, TextField, TextFieldProps, Typography, CircularProgress, Backdrop } from "@mui/material";
import { useState } from "react";
import { useAuth } from "../hooks/AuthProvider";
import { useNavigate } from "react-router-dom";

export default function Login() {
    const { login } = useAuth();
    const navigate = useNavigate();

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [isError, setIsError] = useState(false);
    const [isLoading, setIsLoading] = useState(false);

    const handleLogin = async () => {
        setIsLoading(true);
        setIsError(false);
        try {
            const auth = await login(username, password);
            if (auth) {
                navigate('../dashboard');
            } else {
                setIsError(true);
            }
        } catch (error) {
            setIsError(true);
            console.error("Login error:", error);
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <Container sx={{ height: '100vh' }}>
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
                                onChange={(e) => setUsername(e.target.value)}
                                error={isError}
                            />
                            <PasswordField
                                label="Password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                error={isError}
                            />
                        </Stack>
                        <Button
                            variant="contained"
                            sx={{ py: 1 }}
                            onClick={handleLogin}
                            disabled={isLoading}
                        >
                            {isLoading ? <CircularProgress size={24} color="inherit" /> : "Login"}
                        </Button>
                        {isError && (
                            <Typography color="error" textAlign="center">
                                Invalid username or password
                            </Typography>
                        )}
                    </Stack>
                </Paper>
            </Stack>
            <Backdrop
                sx={{ color: '#fff', zIndex: (theme) => theme.zIndex.drawer + 1 }}
                open={isLoading}
            >
                <Stack alignItems="center" spacing={2}>
                    <CircularProgress color="inherit" />
                    <Typography fontSize={"large"}>Logging in Please Wait...</Typography>
                </Stack>
            </Backdrop>
        </Container>
    );
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