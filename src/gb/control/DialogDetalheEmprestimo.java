package gb.control;

import org.eclipse.swt.widgets.Shell;

import gb.Main;
import gb.model.Emprestimo;
import gb.view.DialogDetalheView;

public class DialogDetalheEmprestimo extends DialogDetalheView {

	private Emprestimo emprestimo;
	
	public DialogDetalheEmprestimo(Shell parent, Emprestimo emprestimo) {
		super(parent);
		this.emprestimo = emprestimo;
		initialize();
	}
	
	private void initialize(){
		shell.setText("Detalhes empréstimo");
		addItem("Data/Hora: ", Main.FORMATADOR_D.format(emprestimo.getDataHora()));
		addItem("Usuário:", emprestimo.getUsuario().getNome());
		addItem("Tipo usuario:", emprestimo.getUsuario().getTipo().getDescricao());
		addDivisor();
		addItem("Registro Exemplar:", emprestimo.getExemplar().getNumRegistro().toString());
		addItem("Título Livro:", emprestimo.getExemplar().getLivro().getTitulo());
		addItem("Autores:", emprestimo.getExemplar().getLivro().getNomeAutores());
		addDivisor();
		addItem("Renovações:", emprestimo.getNumRenovacoes().toString());
		if (emprestimo.getDataHoraDevolucao() == null)
			addItem("Previsão Devolução", Main.FORMATADOR_D.format(emprestimo.getPrevisaoDevolucao()));
		else
			addItem("Devolvido em", Main.FORMATADOR_DH.format(emprestimo.getDataHoraDevolucao()));
	}

}
