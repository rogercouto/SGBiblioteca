package gb.control;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Shell;

import gb.Main;
import gb.model.Emprestimo;
import gb.model.Exemplar;
import gb.model.Reserva;
import gb.model.Situacao;
import gb.model.dao.EmprestimoDAO;
import gb.model.dao.ExemplarDAO;
import gb.model.dao.ReservaDAO;
import gb.util.TemporalUtil;
import gb.view.DialogConsultaSituacaoView;
import swt.cw.dialog.FindDialog;
import swt.cw.model.FindSource;

public class DialogConsultaSituacao extends DialogConsultaSituacaoView {

	private Exemplar exemplar = null;

	public DialogConsultaSituacao(Shell parent) {
		super(parent);
	}

	private void buscaExemplar(){
		FindDialog dialog = new FindDialog(shell);
		dialog.setText("Buscar exemplar");
		dialog.addColumn("numRegistro", "Nº", true);
		dialog.addColumn("livro.titulo", "Título", true);
		dialog.addColumn("livro.isbn", "ISBN", true); //ad
		dialog.setIcons(Main.ICONS);
		dialog.setWidth(0, 80);
		dialog.setWidth(1, 250);
		dialog.setWidth(2, 80);
		dialog.setFindSource(new FindSource() {
			@Override
			public List<?> getList(int index, String text) {
				ExemplarDAO dao = new ExemplarDAO();
				List<Exemplar> list = dao.findList(index, text);
				dao.closeConnection();
				return list;
			}
		});
		exemplar = (Exemplar)dialog.open();
		table.removeAll();
		if (exemplar != null){
			txtBusca.setText(exemplar.getNumRegistro()+" - "+exemplar.getLivro().getTitulo());
			addItem("Autor(es):", exemplar.getLivro().getNomeAutores());
			addItem("Situação:", exemplar.getSituacao().name());
			if (exemplar.isFixo()){
				addItem("Exemplar fixo:", "Não pode ser emprestado!");
			}else{
				if (exemplar.getSituacao().equals(Situacao.EMPRESTADO)){
					EmprestimoDAO dao = new EmprestimoDAO();
					Emprestimo emprestimo = dao.getLastEmprestimo(exemplar);
					if (emprestimo.getDataHoraDevolucao() == null)
						addItem("Previsão devolução:", TemporalUtil.formatDate(emprestimo.getPrevisaoDevolucao()));
				}
				if (exemplar.isReservado()){
					addDivisor();
					addItem("", "RESERVADO");
					ReservaDAO dao = new ReservaDAO();
					Reserva reserva = dao.getLastReserva(exemplar);
					if (reserva != null && !reserva.isExpirada())
						addItem("Limite retirada:",  TemporalUtil.formatDate(reserva.getDataLimite()));
				}
				
			}
		}
	}
	
	protected void btnBuscarWidgetSelected(SelectionEvent arg0) {
		buscaExemplar();
	}
	
	protected void textKeyPressed(KeyEvent arg0) {
		if (arg0.keyCode == SWT.DEL){
			txtBusca.setText("");
			exemplar = null;
			table.removeAll();
		}else{
			buscaExemplar();
		}
		arg0.doit = false;
	}
	
	protected void textModifyText(ModifyEvent arg0) {
		//do nothing yet
	}
	
}
