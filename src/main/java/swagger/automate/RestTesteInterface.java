package swagger.automate;

import javax.ws.rs.core.Response;

import swagger.automate.annotation.Return;
import swagger.automate.annotation.Returns;
import swagger.automate.annotation.Tag;

public interface RestTesteInterface {
	
	@Tag("Teste")
	@Return(ReflectionHelper.class)
	@Returns()
	public Response restTeste2(Long id);
	
	
	@Tag(value="Teste",description = "")
	@Return(ReflectionHelper2.class)
	@Returns({200,300,400,500})
	public Response restTeste(ReflectionHelper obj);

}
