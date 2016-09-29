package gb.control;

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
import gb.model.Pendencia;
import gb.model.Situacao;
import gb.model.dao.EmprestimoDAO;
import gb.model.dao.ExemplarDAO;
import gb.model.dao.PendenciaDAO;
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
		dialog.addColumn("livro.isbn", "ISBN", true);
		dialog.setIcons(Main.ICONS);
		dialog.setWidth(0, 80);
		dialog.setWidth(1, 250);
		dialog.setWidth(2, 80);
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
			if (diasAtraso > 0)
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
		if (multa > 0)
			emprestimo.setMulta(multa);
		emprestimo.setDataHoraDevolucao(LocalDateTime.now());
		EmprestimoDAO dao = new EmprestimoDAO();
		try {
			dao.update(emprestimo);
			boolean pendente = false;
			if (multa > 0){
				Pendencia pendencia = new Pendencia();
				pendencia.setUsuario(emprestimo.getUsuario());
				pendencia.setDataHoraLancamento(LocalDateTime.now());
				pendencia.setValor(multa);
				if (Dialog.question(shell, "Lançar pagamento da multa gerada?"))
					pendencia.setDataHoraPagamento(LocalDateTime.now());
				else
					pendente = true;
				PendenciaDAO pDao = new PendenciaDAO(dao.getConnection());
				pDao.insert(pendencia);
			}
			dao.closeConnection();
			result = emprestimo;
			String msg = "Devolução efetuada!";
			if (pendente)
				msg += "\nPagamento pendente.";
			Dialog.confirmation(shell, msg);
			shell.close();
		} catch (ValidationException e) {
			dao.closeConnection();
			throw new RuntimeException(e.getMessage());
		}
	}
	
}
