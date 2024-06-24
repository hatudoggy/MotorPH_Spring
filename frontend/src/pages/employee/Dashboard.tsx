import { Container, Typography } from "@mui/material";
import Headertext from "../../components/HeaderText";


export default function Dashboard() {

  return(
    <Container
      sx={{
        my: 5,
      }}
    >
      <Headertext>Dashboard</Headertext>
    </Container>
  )
}