package gb.control;

import java.sql.Connection;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Shell;

import gb.Main;
import gb.model.Emprestimo;
import gb.model.Exemplar;
import gb.model.Situacao;
import gb.model.dao.EmprestimoDAO;
import gb.model.dao.ExemplarDAO;
import gb.model.data.ConnectionManager;
import gb.model.exceptions.ValidationException;
import gb.view.DialogDevolucaoView;
import swt.cw.dialog.FindDialog;
import swt.cw.model.FindSource;
import swt.cw.util.Dialog;

public class DialogDevolucao extends DialogDevolucaoView{

	private Emprestimo emprestimo = null;
	private double multa;
	
	public DialogDevolucao(Shell parent) {
		super(parent);
		initialize();
	}
	
	private void initialize(){
		shell.setText("Lançar devolução");
		btnConfirma.setEnabled(false);
	}
	
	private void buscaExemplar(){
		FindDialog dialog = new FindDialog(shell);
		dialog.setText("Buscar exemplar");
		dialog.addColumn("numRegistro", "Nº", true);
		dialog.addColumn("livro.titulo", "Título", true);
		dialog.setWidth(1, 300);
		dialog.setIcons(Main.ICONS);
		dialog.setFindSource(new FindSource() {
			@Override
			public List<?> getList(int index, String text) {
				ExemplarDAO dao = new ExemplarDAO();
				List<Exemplar> list = dao.findList(index, text, Situacao.EMPRESTADO);
				dao.closeConnection();
				return list;
			}
		});
		Exemplar exemplar = (Exemplar)dialog.open();
		if (exemplar != null){
			EmprestimoDAO dao = new EmprestimoDAO();
			emprestimo = dao.getLastEmprestimo(exemplar);
			txtExemplar.setText(exemplar.getNumRegistro()+" - "+exemplar.getLivro().getTitulo());
			long diasAtraso = ChronoUnit.DAYS.between(emprestimo.getPrevisaoDevolucao(), LocalDate.now());
			if (diasAtraso >= 0)
				multa = diasAtraso*Emprestimo.MULTA_DIA;
			txtMulta.setText(new DecimalFormat("0.00").format(multa));
			txtUsuario.setText(emprestimo.getUsuario().getNome());
			btnConfirma.setEnabled(true);
		}
	}

	@Override
	protected void txtExemplarKeyPressed(KeyEvent arg0) {
		if (arg0.keyCode == SWT.DEL){
			txtExemplar.setText("");
			emprestimo = null;
			btnConfirma.setEnabled(false);
		}else{
			buscaExemplar();
		}
		arg0.doit = false;
	}
	
	@Override
	protected void btnBuscarWidgetSelected(SelectionEvent arg0) {
		buscaExemplar();
	}
	
	@Override
	protected void btnConfirmaWidgetSelected(SelectionEvent arg0) {
		if (emprestimo == null)
			return;
		if (multa > 0 && !Dialog.question(shell, "Confirma pagamento da taxa de atraso?"))
			return;
		if (multa > 0)
			emprestimo.setMultaPaga(multa);
		emprestimo.setDataHoraDevolucao(LocalDateTime.now());
		Exemplar exemplar = emprestimo.getExemplar();
		Connection connection = ConnectionManager.getConnection();
		try {
			exemplar.setSituacao(Situacao.DISPONIVEL);
			EmprestimoDAO dao = new EmprestimoDAO(connection);
			dao.update(emprestimo);
			ExemplarDAO exemplarDao = new ExemplarDAO(connection);
			exemplarDao.update(exemplar);
			ConnectionManager.closeConnection(connection);
			result = emprestimo;
			Dialog.confirmation(shell, "Devolução efetuada!");
			shell.close();
		} catch (ValidationException e) {
			ConnectionManager.closeConnection(connection);
			throw new RuntimeException(e.getMessage());
		}
	}
	
}
