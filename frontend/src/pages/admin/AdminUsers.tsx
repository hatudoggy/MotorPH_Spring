import { Container } from "@mui/material";
import Headertext from "../../components/HeaderText";


export default function AdminUsers() {

  return(
    <Container
      sx={{
        my: 5,
      }}
    >
      <Headertext>Users</Headertext>
    </Container>
  )
}