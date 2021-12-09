package swagger.automate;

import javax.ws.rs.core.Response;

import org.springframework.context.annotation.Description;

import swagger.automate.ReflectionHelper;
import swagger.automate.annotation.Return;
import swagger.automate.annotation.Returns;
import swagger.automate.annotation.Tag;

public interface RestTesteInterface {
	
	@Tag("Teste")
//	@Description("Description teste")
	@Return(ReflectionHelper.class)
	@Returns()
	public Response restTeste(ReflectionHelper obj);
}
