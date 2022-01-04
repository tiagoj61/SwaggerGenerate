package swagger.automate.rest;

import java.util.List;

import javax.ws.rs.core.Response;

import swagger.automate.ErroMensage;
import swagger.automate.ReflectionHelper;
import swagger.automate.ReflectionHelper2;
import swagger.automate.ReflectionHelper4;
import swagger.automate.annotation.CodeAndReturn;
import swagger.automate.annotation.ReturnsCods;
import swagger.automate.annotation.Tag;

public interface RestTesteInterface2 {

	
	@Tag(value = "Aasd")
	@ReturnsCods(value = { 
			@CodeAndReturn(code = 400)
			//	@CodeAndReturn(code = 403, object = ErroMensage.class)
	})
	public Response listRecipientes();
	
	@Tag(value = "Aasd")
	@ReturnsCods(value = { 
			@CodeAndReturn(code = 400),
				@CodeAndReturn(code = 200, object = ReflectionHelper4.class)
	})
	public Response listDadosRecipiente(Long recipienteId);

}
