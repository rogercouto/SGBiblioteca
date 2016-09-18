package gb.control;

import java.sql.Connection;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Shell;

import gb.Main;
import gb.model.Cidade;
import gb.model.Endereco;
import gb.model.TipoUsuario;
import gb.model.Usuario;
import gb.model.dao.EnderecoDAO;
import gb.model.dao.TipoUsuarioDAO;
import gb.model.dao.UsuarioDAO;
import gb.model.data.ConnectionManager;
import gb.model.exceptions.ValidationException;
import gb.view.DialogUsuarioView;
import swt.cw.Customizer;
import swt.cw.dialog.FindDialog;
import swt.cw.model.FindSource;
import swt.cw.util.Dialog;
import swt.cw.util.InputMask;

public class DialogUsuario extends DialogUsuarioView {

	private Usuario usuario;
	private List<TipoUsuario> tipos;
	
	public DialogUsuario(Shell parent) {
		super(parent);
		usuario = new Usuario();
		shell.setText("Inserir usuário");
		initialize();
	}
	
	public DialogUsuario(Shell parent, Usuario usuario) {
		super(parent);
		this.usuario = usuario;
		shell.setText("Editar usuário");
		initialize();
	}
	
	private void initialize(){
		Customizer.setInputMask(txtCpf, "###.###.###-##");
		Customizer.setInputMask(txtTelefone, "(##)####-####");
		Customizer.setInputMask(txtCel, "(##)####-####");
		Customizer.setNumeric(txtNumero, 0);
		Customizer.setInputMask(txtCep, "#####-###");
		TipoUsuarioDAO tipoDAO = new TipoUsuarioDAO();
		tipos = tipoDAO.getList();
		tipoDAO.closeConnection();
		for (TipoUsuario tipoUsuario : tipos) {
			cmbTipo.add(tipoUsuario.getDescricao());
		}
		if (usuario.getNome() != null)
			txtNome.setText(usuario.getNome());
		if (usuario.getCpf() != null)
			txtCpf.setText(usuario.getCpf());
		if (usuario.getTipo() != null){
			for (TipoUsuario tipoUsuario : tipos) {
				if (usuario.getTipo().getId() == tipoUsuario.getId()){
					cmbTipo.select(tipos.indexOf(tipoUsuario));
					break;
				}
				
			}
		}
		if (usuario.getTelefone() != null)
			txtTelefone.setText(usuario.getTelefone());
		if (usuario.getCelular() != null)
			txtCel.setText(usuario.getCelular());
		if (usuario.geteMail() != null)
			txtEmail.setText(usuario.geteMail());
		if (usuario.getEndereco() != null){
			Endereco endereco = usuario.getEndereco();
			if (endereco.getLogradouro() != null)
				txtLogradouro.setText(endereco.getLogradouro());
			if (endereco.getNumero() != null)
				txtNumero.setText(endereco.getNumero().toString());
			if (endereco.getCidade() != null)
				txtCidade.setText(endereco.getCidade().getNome()+" - "+endereco.getCidade().getEstado().getSigla());
			if (endereco.getBairro() != null)
				txtBairro.setText(endereco.getBairro());
			if (endereco.getCep() != null)
				txtCep.setText(endereco.getCep());
			if (endereco.getComplemento() != null)
				txtComplemento.setText(endereco.getComplemento());
		}
	}
	
	private void buscaCidade(){
		FindDialog dialog = new FindDialog(shell);
		dialog.addColumn("nome", "Cidade", true);
		dialog.addColumn("siglaEstado", "UF", false);
		dialog.setWidth(0, 300);
		dialog.setIcons(Main.ICONS);
		dialog.setFindSource(new FindSource() {
			@Override
			public List<?> getList(int index, String text) {
				EnderecoDAO dao = new EnderecoDAO();
				List<Cidade> list = dao.findCidades(text);
				dao.closeConnection();
				return list;
			}
		});
		Cidade cidade = (Cidade) dialog.open();
		if (cidade != null){
			if (usuario.getEndereco() == null)
				usuario.setEndereco(new Endereco());
			usuario.getEndereco().setCidade(cidade);
			txtCidade.setText(cidade.getNome()+" - "+cidade.getSiglaEstado());
		}
	}
	
	@Override
	protected void btnCidadeWidgetSelected(SelectionEvent arg0) {
		buscaCidade();
	}
	
	@Override
	protected void btnSalvarWidgetSelected(SelectionEvent arg0) {
		if (txtNome.getText().trim().length() > 0)
			usuario.setNome(txtNome.getText().trim());
		if (txtCpf.getText().trim().length() > 0)
			usuario.setCpf(InputMask.unmaskString(txtCpf.getText()));
		if (cmbTipo.getSelectionIndex() >= 0)
			usuario.setTipo(tipos.get(cmbTipo.getSelectionIndex()));
		if (txtTelefone.getText().trim().length() > 0)
			usuario.setTelefone(InputMask.unmaskString(txtTelefone.getText()));
		if (txtCel.getText().trim().length() > 0)
			usuario.setCelular(InputMask.unmaskString(txtCel.getText()));
		if (txtEmail.getText().trim().length() > 0)
			usuario.seteMail(txtEmail.getText().trim());
		if ((txtLogradouro.getText().trim().length() > 0 ||
			txtNumero.getText().trim().length() > 0 ||
			txtBairro.getText().trim().length() > 0 ||
			txtCep.getText().trim().length() > 0 ||
			txtComplemento.getText().trim().length() > 0)
			&&
			usuario.getEndereco() == null)
		{
			usuario.setEndereco(new Endereco());
		}
		Endereco delEnd = null;
		if (txtLogradouro.getText().trim().length() == 0 &&
			txtNumero.getText().trim().length() == 0 &&
			txtBairro.getText().trim().length() == 0 &&
			txtCidade.getText().trim().length() == 0 &&
			txtCep.getText().trim().length() == 0 &&
			txtComplemento.getText().trim().length() == 0 &&
			usuario.getEndereco() != null)
		{
			delEnd = usuario.getEndereco();
			usuario.setEndereco(null);
		}
		if (usuario.getEndereco() != null){
			if (txtLogradouro.getText().trim().length() > 0)
				usuario.getEndereco().setLogradouro(txtLogradouro.getText());
			if (txtNumero.getText().trim().length() > 0)
				usuario.getEndereco().setNumero(Integer.parseInt(txtNumero.getText()));
			if (txtBairro.getText().trim().length() > 0)
				usuario.getEndereco().setBairro(txtBairro.getText());
			if (txtCep.getText().trim().length() > 0)
				usuario.getEndereco().setCep(InputMask.unmaskString(txtCep.getText()));
			if (txtComplemento.getText().trim().length() > 0)
				usuario.getEndereco().setComplemento(txtComplemento.getText());
		}
		Connection connection = ConnectionManager.getConnection();
		try {
			EnderecoDAO enderecoDAO = new EnderecoDAO(connection);
			if (usuario.getEndereco() != null)
				enderecoDAO.save(usuario.getEndereco());
			UsuarioDAO usuarioDao = new UsuarioDAO(connection);
			usuarioDao.save(usuario);
			if (delEnd != null)
				enderecoDAO.delete(delEnd);
			result = usuario;
		} catch (ValidationException e) {
			Dialog.error(shell, e.getMessage());
		} 
		ConnectionManager.closeConnection(connection);
		if (result != null)
			shell.close();
	}
	
	@Override
	protected void txtCidadeKeyPressed(KeyEvent arg0) {
		if (arg0.keyCode == SWT.DEL){
			txtCidade.setText("");
			if (usuario.getEndereco() != null)
				usuario.getEndereco().setCidade(null);
		}else{
			arg0.doit = false;
			buscaCidade();
		}
	}

}
