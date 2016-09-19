package gb.control;

import org.eclipse.swt.widgets.Shell;

import gb.model.Reserva;
import gb.util.TemporalUtil;
import gb.view.DialogDetalheView;

public class DialogDetalheReserva extends DialogDetalheView {

	private Reserva reserva;
	
	public DialogDetalheReserva(Shell parent, Reserva reserva) {
		super(parent);
		this.reserva = reserva;
		initialize();
	}
	
	private void initialize(){
		shell.setText("Detalhes reserva");
		addItem("Data/Hora: ", TemporalUtil.formatDateTime(reserva.getDataHora()));
		addItem("Usuário:", reserva.getUsuario().getNome());
		addItem("Tipo usuario:", reserva.getUsuario().getTipo().getDescricao());
		addDivisor();
		addItem("Registro Exemplar:", reserva.getExemplar().getNumRegistro().toString());
		addItem("Título Livro:", reserva.getExemplar().getLivro().getTitulo());
		addItem("Autores:", reserva.getExemplar().getLivro().getNomeAutores());
		addDivisor();
		if (reserva.getDataHoraRetirada() == null)
			addItem("Limite retirada:", TemporalUtil.formatDate(reserva.getDataLimite()));
		else
			addItem("Retirado em:", TemporalUtil.formatDateTime(reserva.getDataHoraRetirada()));
		if (reserva.isExpirada())
			addItem("Obs:", "Cancelada");
	}

}
