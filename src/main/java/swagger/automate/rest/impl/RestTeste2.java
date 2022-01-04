package swagger.automate.rest.impl;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;

import swagger.automate.ReflectionHelper4;
import swagger.automate.rest.RestTesteInterface2;

@Component
@Path("/usuario")
public class RestTeste2 implements RestTesteInterface2 {

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
