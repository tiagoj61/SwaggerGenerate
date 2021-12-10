package swagger.automate;

import javax.ws.rs.core.Response;

import org.springframework.context.annotation.Description;

import swagger.automate.ReflectionHelper;
import swagger.automate.annotation.Return;
import swagger.automate.annotation.Returns;
import swagger.automate.annotation.Tag;

public interface RestTesteInterface {
	
	@Tag(value="Teste",description = "")
	@Return(ReflectionHelper.class)
	@Returns({200,300,400,500})
	public Response restTeste(ReflectionHelper obj);
	@Tag("Teste")
	@Return(ReflectionHelper.class)
	@Returns()
	public Response restTeste2(ReflectionHelper obj);
}
