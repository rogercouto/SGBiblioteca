package gb.control;

import java.time.LocalDateTime;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;

import gb.model.Pendencia;
import gb.model.Usuario;
import gb.model.dao.PendenciaDAO;
import gb.model.dao.UsuarioDAO;
import gb.view.DialogPagamentoView;
import swt.cw.dialog.FindDialog;
import swt.cw.model.FindSource;
import swt.cw.util.Dialog;

public class DialogPagamento extends DialogPagamentoView {

	private Usuario usuario;

	public DialogPagamento(Shell parent) {
		super(parent);
		initialize();
	}
	
	private void initialize(){
		table.addColumn("dataHoraLancamento", "Lançamento");
		table.addColumn("valor", "Valor");
		table.setWidth(0, 150);
	}
	
	private void busca(){
		FindDialog dialog = new FindDialog(shell);
		dialog.addColumn("nome", "Nome", true);
		dialog.setWidth(0, 200);
		dialog.setFindSource(new FindSource() {
			@Override
			public List<?> getList(int index, String text) {
				UsuarioDAO dao = new UsuarioDAO();
				List<Usuario> list = dao.findList(text);
				dao.closeConnection();
				return list;
			}
		});
		usuario = (Usuario)dialog.open();
		if (usuario != null){
			txtBusca.setText(usuario.getNome());
			PendenciaDAO dao = new PendenciaDAO();
			List<Pendencia> list = dao.getList(usuario);
			table.setData(list);
		}
	}

	
	protected void txtBuscaKeyPressed(KeyEvent arg0) {
		if (arg0.keyCode == SWT.DEL){
			txtBusca.setText("");
			usuario = null;
		}else{
			busca();
		}
		arg0.doit = false;
	}
	
	protected void btnBuscaWidgetSelected(SelectionEvent arg0) {
		busca();
	}
	
	protected void txtBuscaModifyText(ModifyEvent arg0) {
	}
	
	protected void tableMouseDown(MouseEvent arg0) {
		int size = table.getCheckedCount();
		btnConfirma.setEnabled(size > 0);
	}
	
	protected void tableKeyReleased(KeyEvent arg0) {
		int size = table.getCheckedCount();
		btnConfirma.setEnabled(size > 0);
	}
	
	protected void tableSelection(Event arg0){
	}
	
	protected void btnConfirmaWidgetSelected(SelectionEvent arg0) {
		List<?> checkedItems = table.getCheckedItems();
		if (checkedItems.isEmpty())
			return;
		PendenciaDAO dao = new PendenciaDAO();
		for (Object object : checkedItems) {
			Pendencia pendencia = (Pendencia)object;
			pendencia.setDataHoraPagamento(LocalDateTime.now());
			dao.update(pendencia);
		}
		Dialog.confirmation(shell, "Pagamento confirmado!");
		List<Pendencia> list = dao.getList(usuario);
		table.setData(list);
		dao.closeConnection();
	}
}
