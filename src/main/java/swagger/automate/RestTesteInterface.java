package swagger.automate;

import java.util.List;

import javax.ws.rs.core.Response;

import swagger.automate.annotation.CodeAndReturn;
import swagger.automate.annotation.ReturnsCods;
import swagger.automate.annotation.Tag;

public interface RestTesteInterface {


	@Tag(value = "Teste")
	@ReturnsCods(value = { 
			@CodeAndReturn(code = 200,object = ReflectionHelper2.class),
			@CodeAndReturn(code = 403, object = ErroMensage.class)
			})
	public Response restTeste(ReflectionHelper obj);
	
	@Tag(value = "Teste")
	@ReturnsCods(value = { 
			@CodeAndReturn(code = 200)
		//	@CodeAndReturn(code = 403, object = ErroMensage.class)
	})
	public Response restTeste2(List<ReflectionHelper> objArr);
	
	@Tag(value = "Teste")
	@ReturnsCods(value = { 
			@CodeAndReturn(code = 400)
			//	@CodeAndReturn(code = 403, object = ErroMensage.class)
	})
	public Response listRecipientes();
	
	@Tag(value = "Teste")
	@ReturnsCods(value = { 
			@CodeAndReturn(code = 400),
				@CodeAndReturn(code = 200, object = ReflectionHelper4.class)
	})
	public Response listDadosRecipiente(Long recipienteId);

}
