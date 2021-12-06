/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author links
 */
@Service
public class UsuarioBo extends AbstractService<Usuario> implements IUsuarioBo {

	@Autowired
	private IUsuarioDao dao;

	@Override
	protected IOperations<Usuario> getDao() {
		return dao;
	}

	@Override
	public Usuario persistOrThrow(Usuario usuario) throws Exception {
		var keepOldPwd = false;
		if (usuario == null) {
			throw new EntityNotFoundException(Usuario.class.getSimpleName());
		}
		// boolean matched = BCrypt.checkpw(entity.getSenha(), usuario.getSenha());
		if (usuario.getId() != null) {
			var entity = dao.load(usuario.getId());
			if (StringUtils.isBlank(usuario.getSenha())) {
				usuario.setSenha(entity.getSenha());
				keepOldPwd = true;
			}
		} else {
			if (dao.byEmail(usuario.getEmail()) != null) {
				throw new DuplicateConstraintViolationException(
						"O login/e-mail informado já está associado a outro usuário.");
			}
		}
		if (!keepOldPwd) {
			usuario.setSenha(DigestUtils.sha512Hex(usuario.getSenha()));
		}
		return super.persist(usuario);
	}

	@Override
	public Usuario login(String email, String senha) throws LoginException, SenhaException {
		var usuario = dao.byEmail(email);
		if (usuario == null) {
			throw new LoginException();
		}
		if (!usuario.getSenha().equalsIgnoreCase(senha)) {
			throw new SenhaException();
		}
		usuario.setUltimoAcesso(Calendar.getInstance().getTime());
		usuario.setHash(DigestUtils.md5Hex(String.valueOf(Calendar.getInstance().getTimeInMillis())));
		return dao.persist(usuario);
	}

	@Override
	public Usuario byHash(String hash) {
		return dao.byHash(hash);
	}

	@Override
	public Usuario byEmail(String email) {
		return dao.byEmail(email);
	}

	public void readFile(String email) throws IOException {
		String lines = "";
		File file = new File("");
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = br.readLine()) != null) {
				lines += line;
			}
		}
		JSONObject swaggerConf = new JSONObject(lines);

		changeStringInFile("%nomeSistema%", swaggerConf.getString("nomeSistema"), file);
		changeStringInFile("%versao%", swaggerConf.getString("versao"), file);
		changeStringInFile("%url%", swaggerConf.getString("url"), file);

		JSONArray swaggerPathRests = swaggerConf.getJSONArray("caminhos");

		for (int i = 0; i < swaggerPathRests.length(); i++) {

			JSONObject obj = swaggerPathRests.getJSONObject(i);

			String toReplace = (i == swaggerPathRests.length() - 1
					? generetePathRestToChange(obj.getString("nomePathRest"), obj.getString("descricao"))
					: generetePathRestToChange(obj.getString("nomePathRest"), obj.getString("descricao"))
							+ "\n%pathRest%");

			changeStringInFile("%pathRest%", toReplace, file);
		}

		JSONArray swaggerPaths = swaggerConf.getJSONArray("paths");

		for (int i = 0; i < swaggerPaths.length(); i++) {

			JSONObject obj = swaggerPaths.getJSONObject(i);

			String toReplace = (i == swaggerPaths.length() - 1
					? generetePathToChange(obj.getString("caminho"), obj.getString("method"))
					: generetePathToChange(obj.getString("caminho"), obj.getString("method")) + "\n%rests%");

			changeStringInFile("%rests%", toReplace, file);
		}

	}

	public String generetePathRestToChange(String toAdd, String descToAdd) {
		toAdd = toAdd == null ? "" : toAdd;
		descToAdd = descToAdd == null ? "" : descToAdd;
		String result = "-name:\"" + toAdd + "\"\n  description: \"%" + descToAdd + "%\"";
		return result;
	}

	public String generetePathToChange(String toAdd, String descToAdd) {
		toAdd = toAdd == null ? "" : toAdd;
		descToAdd = descToAdd == null ? "" : descToAdd;
		String result = "-name:\"" + toAdd + "\"\n  description: \"%" + descToAdd + "%\"";
		return result;
	}

	public void changeStringInFile(String replacemente, String toReplace, File file) {
		try {
			String content = FileUtils.readFileToString(new File("InputFile"), "UTF-8");
			content = content.replaceAll(replacemente, toReplace);
			File tempFile = new File("OutputFile");
			FileUtils.writeStringToFile(file, content, "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException("Generating file failed", e);
		}
	}
}
