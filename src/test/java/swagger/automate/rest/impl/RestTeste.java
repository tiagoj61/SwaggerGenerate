package swagger.automate.rest.impl;

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

import swagger.automate.rest.ReflectionHelper;
import swagger.automate.rest.ReflectionHelper4;
import swagger.automate.rest.RestTesteInterface;

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
}
