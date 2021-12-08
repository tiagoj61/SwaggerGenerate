package swagger.automate;

import javax.ws.rs.core.Response;

import org.springframework.context.annotation.Description;

import swagger.automate.annotation.Return;
import swagger.automate.annotation.Tag;

public interface RestTesteInterface {
	
	@Tag("Teste")
//	@Description("Description teste")
	@Return(ReflectionHelper.class)
	//@Returns([200,300,400])
	public Response restTeste(Long id);
}
