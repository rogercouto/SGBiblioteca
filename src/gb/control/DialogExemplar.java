package gb.control;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Shell;

import gb.model.Exemplar;
import gb.model.Livro;
import gb.model.Origem;
import gb.model.Secao;
import gb.model.Situacao;
import gb.model.dao.ExemplarDAO;
import gb.model.dao.OrigemDAO;
import gb.model.dao.SecaoDAO;
import gb.model.data.ConnectionManager;
import gb.model.exceptions.ValidationException;
import gb.view.DialogExemplarView;
import swt.cw.Customizer;
import swt.cw.util.Dialog;

public class DialogExemplar extends DialogExemplarView {

	private Exemplar exemplar;
	private boolean update;
	
	private List<Secao> secoes;
	private List<Origem> origens;
	
	public DialogExemplar(Shell parent, Livro livro) {
		super(parent);
		exemplar = new Exemplar();
		exemplar.setLivro(livro);
		exemplar.setSituacao(Situacao.DISPONIVEL);
		update = false;
		shell.setText("Inserir exemplar");
		initialize();
	}
	
	public DialogExemplar(Shell parent, Exemplar  exemplar) {
		super(parent);
		this.exemplar = exemplar;
		update = true;
		shell.setText("Editar exemplar");
		initialize();
	}
	
	private void initialize(){
		Connection connection = ConnectionManager.getConnection();
		SecaoDAO secaoDAO = new SecaoDAO(connection);
		secoes = secaoDAO.getList();
		OrigemDAO origemDAO = new OrigemDAO(connection);
		origens = origemDAO.getList();
		ConnectionManager.closeConnection(connection);
		//Preenche o formulário
		if (exemplar.getNumRegistro() != null)
			txtNum.setText(exemplar.getNumRegistro().toString());
		for (Secao secao : secoes) {
			cmbSecao.add(secao.getDescricao());
			if (exemplar.getSecao() != null && exemplar.getSecao().getId() == secao.getId())
				cmbSecao.select(secoes.indexOf(secao));
		}
		for (Origem origem : origens) {
			cmbOrigem.add(origem.getDescricao());
			if (exemplar.getOrigem() != null && exemplar.getOrigem().getId() == origem.getId())
				cmbOrigem.select(origens.indexOf(origem));
		}
		chkFixo.setSelection(exemplar.getFixo());
		if (!exemplar.getSituacao().equals(Situacao.DISPONIVEL))
			chkFixo.setEnabled(false);
		txtSituacao.setText(exemplar.getSituacao().name());		
		Customizer.setNumeric(txtNum, 0);
		if (update)
			txtNum.setEditable(false);
	}
	
	private void salvar() throws ValidationException{
		if (!update && txtNum.getText().trim().length() > 0)
			exemplar.setNumRegistro(Integer.parseInt(txtNum.getText()));
		int index = cmbSecao.getSelectionIndex();
		if (index >= 0){
			exemplar.setSecao(secoes.get(index));
		}else if (cmbSecao.getText().trim().length() > 0){
			boolean insert = true;
			for (Secao secao : secoes) {
				if (cmbSecao.getText().compareTo(secao.getDescricao())==0){
					exemplar.setSecao(secao);
					insert = false;
					break;
				}
			}
			if (insert){
				SecaoDAO dao = new SecaoDAO();
				Secao secao = new Secao();
				secao.setDescricao(cmbSecao.getText().trim());
				dao.insert(secao);
				exemplar.setSecao(secao);
			}
		}
		index = cmbOrigem.getSelectionIndex();
		if (index >= 0){
			exemplar.setOrigem(origens.get(index));
		}else if (cmbOrigem.getText().trim().length() > 0){
			boolean insert = true;
			for (Origem origem : origens) {
				if (cmbOrigem.getText().compareTo(origem.getDescricao())==0){
					exemplar.setOrigem(origem);
					insert = false;
					break;
				}
			}
			if (insert){
				OrigemDAO dao = new OrigemDAO();
				Origem origem = new Origem();
				origem.setDescricao(cmbOrigem.getText().trim());
				dao.insert(origem);
				exemplar.setOrigem(origem);
			}
		}
		exemplar.setDataAquisicao(LocalDate.of(dtDataAquis.getYear(), dtDataAquis.getMonth()-1, dtDataAquis.getDay()));
		exemplar.setFixo(chkFixo.getSelection());
		ExemplarDAO dao = new ExemplarDAO();
		if (update)
			dao.update(exemplar);
		else
			dao.insert(exemplar);
		result = exemplar;
		shell.close();
	}
	
	protected void btnSalvarWidgetSelected(SelectionEvent arg0) {
		try {
			salvar();
		} catch (ValidationException e) {
			Dialog.error(shell, e.getMessage());
		}
	}

}
