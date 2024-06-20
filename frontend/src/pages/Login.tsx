import { Whatshot } from "@mui/icons-material";
import { Button, Container, Paper, Stack, TextField, Typography } from "@mui/material";


export default function Login() {

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
              />
              <TextField
                label="Password"
              />
            </Stack>
            <Button 
              variant="contained"
              sx={{
                py: 1
              }}
            >
              Login
            </Button>
          </Stack>
        </Paper>
      </Stack>
      </Container>
  )
}