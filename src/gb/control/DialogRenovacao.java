package gb.control;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;

import gb.model.Emprestimo;
import gb.model.dao.EmprestimoDAO;
import gb.model.exceptions.ValidationException;
import gb.util.TemporalUtil;
import gb.view.DialogRenovacaoView;
import swt.cw.util.Dialog;

public class DialogRenovacao extends DialogRenovacaoView {

	private Emprestimo emprestimo = null;
	
	public DialogRenovacao(Shell parent) {
		super(parent);
		initialize();
	}
	
	private void initialize(){
		table.addColumn("dataHora", "Data/Hora");
		table.addColumn("exemplar.numRegistro", "Nº");
		table.setWidth(1, 60);
		table.addColumn("usuario.nome", "Usuário");
		table.setWidth(2, 150);
		table.addColumn("exemplar.livro.titulo", "Livro");
		table.setWidth(3, 150);
		table.addColumn("exemplar.livro.nomeAutores", "Autor(es)");
		table.setWidth(4, 150);
	}
	
	
	
	private void busca(){
		if (txtBusca.getText().trim().length() == 0){
			Dialog.warning(shell, "Texto da busca não pode ficar em branco!");
			return;
		}
		EmprestimoDAO dao = new EmprestimoDAO();
		List<Emprestimo> list = dao.findList(txtBusca.getText().trim());
		dao.closeConnection();
		if (list.size() == 0)
			lblDetalhes.setText("Nenhum empréstimo contendo "+txtBusca.getText());
		else
			lblDetalhes.setText("");
		table.setData(list);
		emprestimo = null;
	}
	
	@Override
	protected void txtBuscaKeyPressed(KeyEvent arg0) {
		if (arg0.keyCode == SWT.CR || arg0.keyCode == SWT.KEYPAD_CR)
			busca();
	}

	@Override
	protected void btnBuscaWidgetSelected(SelectionEvent arg0) {
		busca();
	}
	
	@Override
	protected void txtBuscaModifyText(ModifyEvent arg0) {
		table.setData(new ArrayList<>());
	}
	
	
	protected void tableMouseDown(MouseEvent arg0) {
		btnConfirma.setEnabled(table.getSelectionIndex() >= 0);
		if (table.getSelectionIndex() < 0 && table.getItemCount() > 0)
			lblDetalhes.setText("");
	}
	
	@Override
	protected void tableSelection(Event arg0){
		emprestimo = null;
		btnConfirma.setEnabled(false);
		Emprestimo emprestimo = (Emprestimo)table.getSelection();
		if (emprestimo != null){
			if (emprestimo.getExemplar().isReservado()){
				lblDetalhes.setText("Livro está reservado e não pode ser renovado!");
			}else{
				if (emprestimo.getNumRenovacoes() >= emprestimo.getUsuario().getTipo().getNumRenov()){
					lblDetalhes.setText("Livro já não pode mais ser renovado!");
					return;
				}
				lblDetalhes.setText("Nova data de entrega: "+TemporalUtil.formatDate(emprestimo.getPrevisaoDevolucao(1)));
				this.emprestimo = emprestimo;
				btnConfirma.setEnabled(true);
			}
		}
	}
	
	@Override
	protected void btnConfirmaWidgetSelected(SelectionEvent arg0) {
		if (emprestimo != null){
			emprestimo.incNumRenovacoes();
			EmprestimoDAO dao = new EmprestimoDAO();
			try {
				dao.update(emprestimo);
			} catch (ValidationException e) {
				dao.closeConnection();
				new RuntimeException(e.getMessage());
			}
			dao.closeConnection();
			Dialog.confirmation(shell, "Empréstimo renovado até: "+TemporalUtil.formatDate(emprestimo.getPrevisaoDevolucao()));
			shell.close();
		}
	}

}
