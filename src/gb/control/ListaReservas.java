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

import gb.model.Exemplar;
import gb.model.Reserva;
import gb.model.Situacao;
import gb.model.dao.ExemplarDAO;
import gb.model.dao.ReservaDAO;
import gb.model.data.ConnectionManager;
import gb.model.exceptions.ValidationException;
import gb.view.ListaView;
import swt.cw.util.Dialog;

public class ListaReservas extends ListaView {

	private String busca = null;
	
	public ListaReservas(Composite parent) {
		super(parent);
		initialize();
	}
	
	private void initialize(){
		setHeader("Reserva", SWT.COLOR_WHITE, SWT.COLOR_DARK_GREEN);
		table.addColumn("dataHora", "Data/Hora");
		table.addColumn("usuario.nome", "Usuário");
		table.setWidth(1, 160);
		table.addColumn("exemplar.numRegistro", "Nº");
		table.setWidth(2, 60);
		table.addColumn("exemplar.livro.titulo", "Livro");
		table.setWidth(3, 190);
		table.addColumn("exemplar.livro.nomeAutores", "Autor(es)");
		table.setWidth(4, 190);
		table.addColumn("dataLimite", "Data limite");
		table.setWidth(5, 110);
		table.addColumn("status", "Status");
		table.setWidth(6, 70);
		Connection connection =  ConnectionManager.getConnection();
		ReservaDAO reservaDao = new ReservaDAO(connection);
		LocalDate dataIni = LocalDate.of(dtIni.getYear(), dtIni.getMonth()+1, 1);
		LocalDate dataFim = LocalDate.of(dtFim.getYear(), dtFim.getMonth()+1, 1).plusMonths(1);
		List<Reserva> list = reservaDao.getList(dataIni, dataFim);
		ConnectionManager.closeConnection(connection);
		table.setData(list);
		btnDetalhes.setToolTipText("Detalhar reserva");
		btnCancelar.setToolTipText("Excluir reserva");
	}
	
	private void filtra(){
		ReservaDAO reservaDao = new ReservaDAO();
		LocalDate dataIni = LocalDate.of(dtIni.getYear(), dtIni.getMonth()+1, 1);
		LocalDate dataFim = LocalDate.of(dtFim.getYear(), dtFim.getMonth()+1, 1).plusMonths(1);
		List<Reserva> list;
		if (busca == null)
			list = reservaDao.getList(dataIni, dataFim);
		else
			list = reservaDao.findList(dataIni, dataFim, busca);
		reservaDao.closeConnection();
		if (list.size() == 0)
			lblStatus.setText("Nenhuma reserva com o filtro atual!");
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
	
	@Override
	protected void txtBuscaKeyPressed(KeyEvent arg0) {
		if (arg0.keyCode == SWT.CR || arg0.keyCode == SWT.KEYPAD_CR){
			if (txtBusca.getText().trim().length() > 0)
				busca = txtBusca.getText();
			filtra();
		}
	}
	
	private void mostraDetalhes(){
		Reserva reserva = (Reserva)table.getSelection();
		if (reserva != null){
			DialogDetalheReserva dialog = new DialogDetalheReserva(getShell(), reserva);
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
	protected void btnCancelarWidgetSelected(SelectionEvent arg0) {
		int index = table.getSelectionIndex();
		if (index >= 0 && Dialog.questionExclude()){
			Reserva reserva = (Reserva)table.getSelection();
			if (reserva.getDataHoraRetirada() != null){
				Dialog.warning(getShell(), "Reserva já foi concluida e não pode ser excluida!");
				return;
			}
			Exemplar exemplar = reserva.getExemplar();
			exemplar.setSituacao(Situacao.DISPONIVEL);
			Connection connection = ConnectionManager.getConnection();
			ConnectionManager.disableAutoCommit(connection);
			ExemplarDAO exemplarDAO = new ExemplarDAO(connection);
			ReservaDAO reservaDAO = new ReservaDAO(connection);
			try {
				exemplarDAO.update(exemplar);
				reservaDAO.delete(reserva);
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
