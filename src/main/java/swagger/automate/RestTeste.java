package swagger.automate;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;

@Component
@Path("/usuario")
public class RestTeste implements RestTesteInterface {

	@Path("login")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response restTeste(ReflectionHelper obj) {
		try {
			List<String> a = new ArrayList<>();
			return Response.ok().entity(new String()).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}

	@Path("receberDadosRecipientes")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response restTeste2(List<ReflectionHelper> id) {
		try {

			return Response.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
	@Path("listRecipientes")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response listRecipientes() {
		try {
			
			return Response.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
	@Path("listDadosRecipiente/{id}")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response listDadosRecipiente(@PathParam("id") Long recipienteId) {
		try {
			return Response.status(Response.Status.OK).entity(new ReflectionHelper4()).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().entity(new Error("Erro no servidor")).build();
		}

	}
}
