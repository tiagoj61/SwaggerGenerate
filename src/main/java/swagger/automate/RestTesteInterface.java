package swagger.automate;

import javax.ws.rs.core.Response;

import swagger.automate.annotation.CodeAndReturn;
import swagger.automate.annotation.ReturnsCods;
import swagger.automate.annotation.Tag;

public interface RestTesteInterface {

	@Tag("Teste")
	@ReturnsCods(value = { @CodeAndReturn(code = 200, object = ReflectionHelper.class),
			@CodeAndReturn(code = 300) })
	public Response restTeste2(Long id);

	@Tag(value = "Teste", description = "")
	@ReturnsCods(value = { 
			@CodeAndReturn(code = 200),
			@CodeAndReturn(code = 300, object = ReflectionHelper.class, array = true)
			})
	public Response restTeste(ReflectionHelper obj);

}
