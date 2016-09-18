package gb.control;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.SWTResourceManager;

import gb.model.Emprestimo;
import gb.model.Exemplar;
import gb.model.Situacao;
import gb.model.dao.EmprestimoDAO;
import gb.model.dao.ExemplarDAO;
import gb.model.data.ConnectionManager;
import gb.model.exceptions.ValidationException;
import gb.view.ListaView;
import swt.cw.util.Dialog;

public class ListaEmprestimos extends ListaView {

	private String busca = null;
	
	public ListaEmprestimos(Composite parent) {
		super(parent);
		initialize();
	}
	
	private void initialize(){
		lblHeader.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		cmpHeader.setBackground(SWTResourceManager.getColor(SWT.COLOR_DARK_MAGENTA));
		lblHeader.setBackground(cmpHeader.getBackground());
		setHeader("Empréstimios/Devoluções", SWT.COLOR_WHITE, SWT.COLOR_DARK_MAGENTA);
		table.addColumn("dataHora", "Data/Hora");
		table.addColumn("usuario.nome", "Usuário");
		table.setWidth(1, 180);
		table.addColumn("exemplar.numRegistro", "Nº");
		table.setWidth(2, 60);
		table.addColumn("exemplar.livro.titulo", "Livro");
		table.setWidth(3, 200);
		table.addColumn("exemplar.livro.nomeAutores", "Autor(es)");
		table.setWidth(4, 200);
		table.addColumn("dataHoraDevolucao", "Devolvido em");
		table.setWidth(5, 120);
		Connection connection =  ConnectionManager.getConnection();
		EmprestimoDAO emprestimoDao = new EmprestimoDAO(connection);
		LocalDate dataIni = LocalDate.of(dtIni.getYear(), dtIni.getMonth()+1, 1);
		LocalDate dataFim = LocalDate.of(dtFim.getYear(), dtFim.getMonth()+1, 1).plusMonths(1);
		List<Emprestimo> list = emprestimoDao.getList(dataIni, dataFim);
		ConnectionManager.closeConnection(connection);
		table.setData(list);
	}
	
	private void filtra(){
		EmprestimoDAO emprestimoDao = new EmprestimoDAO();
		LocalDate dataIni = LocalDate.of(dtIni.getYear(), dtIni.getMonth()+1, 1);
		LocalDate dataFim = LocalDate.of(dtFim.getYear(), dtFim.getMonth()+1, 1).plusMonths(1);
		List<Emprestimo> list;
		if (busca == null)
			list = emprestimoDao.getList(dataIni, dataFim);
		else
			list = emprestimoDao.findList(dataIni, dataFim, busca);
		emprestimoDao.closeConnection();
		if (list.size() == 0)
			lblStatus.setText("Nenhum empréstimo com o filtro atual!");
		else
			lblStatus.setText("");
		table.setData(list);
	}
	
	@Override
	public void refresh(){
		filtra();
	}
	
	@Override
	protected void btnBuscarWidgetSelected(SelectionEvent arg0) {
		if (txtBusca.getText().trim().length() > 0)
			busca = txtBusca.getText();
		filtra();
	}
	
	@Override
	protected void dtIniWidgetSelected(SelectionEvent arg0) {
		filtra();
	}
	
	@Override
	protected void dtFimWidgetSelected(SelectionEvent arg0) {
		filtra();
	}
	
	@Override
	protected void txtBuscaModifyText(ModifyEvent arg0) {
		busca = null;
		filtra();
	}
	
	private void mostraDetalhes(){
		Emprestimo emprestimo = (Emprestimo)table.getSelection();
		if (emprestimo != null){
			DialogDetalheEmprestimo dialog = new DialogDetalheEmprestimo(getShell(), emprestimo);
			dialog.open();
		}
	}
	
	@Override
	protected void tableMouseDoubleClick(MouseEvent arg0) {
		mostraDetalhes();
	}
	
	@Override
	protected void btnDetalhesWidgetSelected(SelectionEvent arg0) {
		mostraDetalhes();
	}
	
	@Override
	protected void txtBuscaKeyPressed(KeyEvent arg0) {
		if (arg0.keyCode == SWT.CR || arg0.keyCode == SWT.KEYPAD_CR){
			if (txtBusca.getText().trim().length() > 0)
				busca = txtBusca.getText();
			filtra();
		}
	}
	
	@Override
	protected void btnCancelarWidgetSelected(SelectionEvent arg0) {
		int index = table.getSelectionIndex();
		if (index >= 0 && Dialog.questionExclude()){
			Emprestimo emprestimo = (Emprestimo)table.getSelection();
			if (emprestimo.getDataHoraDevolucao() != null){
				Dialog.warning(getShell(), "Emprestimo já foi concluida e não pode ser excluida!");
				return;
			}
			Exemplar exemplar = emprestimo.getExemplar();
			exemplar.setSituacao(Situacao.DISPONIVEL);
			Connection connection = ConnectionManager.getConnection();
			ConnectionManager.disableAutoCommit(connection);
			ExemplarDAO exemplarDAO = new ExemplarDAO(connection);
			EmprestimoDAO emprestimoDAO = new EmprestimoDAO(connection);
			try {
				exemplarDAO.update(exemplar);
				emprestimoDAO.delete(emprestimo);
				ConnectionManager.commit(connection);
				table.remove(index);
			} catch (ValidationException e) {
				ConnectionManager.rollback(connection);
				ConnectionManager.closeConnection(connection);
				throw new RuntimeException(e.getMessage());
			}
			ConnectionManager.closeConnection(connection);
		}
	}
	
}
