package gb.control;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import gb.model.Assunto;
import gb.model.Autor;
import gb.model.Categoria;
import gb.model.Editora;
import gb.model.Exemplar;
import gb.model.Livro;
import gb.model.dao.AssuntoDAO;
import gb.model.dao.AutorDAO;
import gb.model.dao.CategoriaDAO;
import gb.model.dao.EditoraDAO;
import gb.model.dao.ExemplarDAO;
import gb.model.dao.LivroDAO;
import gb.model.data.ConnectionManager;
import gb.model.exceptions.ValidationException;
import gb.view.DialogLivroView;
import swt.cw.Customizer;
import swt.cw.dialog.FindDialog;
import swt.cw.model.FindSource;
import swt.cw.util.Dialog;

public class DialogLivro extends DialogLivroView {

	private static final DateTimeFormatter FORMATADOR = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
	private Livro livro = null;
	
	private List<Editora> editoras;
	private List<Assunto> assuntos;
	
	private List<Categoria> categorias;
	private List<Categoria> categoriasDisp;
	
	private Autor autor = null;
	
	private boolean modificado = false;
	
	public DialogLivro(Shell parent) {
		super(parent);
		livro = new Livro();
		shell.setText("Inserir livro");
		initialize();
	}
	
	public DialogLivro(Shell parent, Livro livro){
		super(parent);
		this.livro = livro;
		shell.setText("Editar livro");
		initialize();
	}
	
	private void initialize(){
		//Preenche as listas de editoras e assuntos
		Connection connection = ConnectionManager.getConnection();
		EditoraDAO editoraDAO = new EditoraDAO(connection);
		editoras = editoraDAO.getList();
		AssuntoDAO assuntoDAO = new AssuntoDAO(connection);
		assuntos = assuntoDAO.getList();
		CategoriaDAO categoriaDAO = new CategoriaDAO(connection);
		categorias = categoriaDAO.getCategorias();
		
		//Preenche os combos
		for (Editora editora : editoras) {
			cmbEditora.add(editora.getNome());
		}
		for (Assunto assunto : assuntos) {
			cmbAssunto.add(assunto.getDescricao());
		}
		//Ajusta as mascaras de entrada
		Customizer.setNumeric(txtISBN, 0);
		Customizer.setNumeric(txtNumPag, 0);
		Customizer.setTemporal(txtDataPublicacao, "dd/MM/yyyy");
		//Preenche os campos do formulário
		if (livro.getId() != null){
			txtTitulo.setText(livro.getTitulo());
			if (livro.getResumo() != null)
				txtResumo.setText(livro.getResumo());
			if (livro.getIsbn() != null)
				txtISBN.setText(livro.getIsbn().toString());
			if (livro.getCutter() != null)
				txtCutter.setText(livro.getCutter().toString());
			if (livro.getEditora() != null){
				for (Editora editora : editoras) {
					if (livro.getEditora().getId() == editora.getId()){
						cmbEditora.select(editoras.indexOf(editora));
						break;
					}
				}
			}
			if (livro.getEdicao() != null)
				txtEdicao.setText(livro.getEdicao());
			if (livro.getVolume() != null)
				txtVolume.setText(livro.getVolume());
			if (livro.getNumPaginas() != null)
				txtNumPag.setText(livro.getNumPaginas().toString());
			if (livro.getAssunto() != null){
				for (Assunto assunto : assuntos) {
					if (livro.getAssunto().getId() == assunto.getId()){
						cmbAssunto.select(assuntos.indexOf(assunto));
						break;
					}
				}
			}
			if (livro.getDataPublicacao() != null){
				txtDataPublicacao.setText(FORMATADOR.format(livro.getDataPublicacao()));
			}
			if (livro.getLocalPublicacao() != null)
				txtLocalPublicacao.setText(livro.getLocalPublicacao());
			for (Autor autor : livro.getAutores()) {
				lstAutores.add(autor.getNomeCompleto());
			}
			for (Categoria categoria : livro.getCategorias()) {
				lstCategorias.add(categoria.getDescricao());
			}
			tableExemplares.addColumn("numRegistro", "Nº");
			tableExemplares.addColumn("dataAquisForm", "Data aquisição");
			tableExemplares.addColumn("descrSecao", "Seçao");
			tableExemplares.addColumn("descrSituacao", "Situacao");
			tableExemplares.setWidth(0, 60);
			tableExemplares.setWidth(1, 120);
			tableExemplares.setWidth(2, 120);
			ExemplarDAO dao = new ExemplarDAO(connection);
			tableExemplares.setData(dao.getList(livro));
			
		}
		resetComboCategorias();
		ConnectionManager.closeConnection(connection);
		Listener listener = new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				modificado = true;
				btnSalvar.setEnabled(true);
			}
		};
		Customizer.addModifyListener(tab1, listener);
		Customizer.addModifyListener(tab2, listener);
		modificado = false;
		btnSalvar.setEnabled(false);
	}
	
	private void resetComboCategorias(){
		categoriasDisp = new ArrayList<>();
		for (Categoria categoria : categorias) {
			boolean disp = true;
			for (Categoria lc : livro.getCategorias()) {
				if (categoria.getId() == lc.getId()){
					disp = false;
					break;
				}
				
			}
			if (disp)
				categoriasDisp.add(categoria);
		}
		cmbAddCategoria.removeAll();
		for (Categoria categoria : categoriasDisp) 
			cmbAddCategoria.add(categoria.getDescricao());
	}
	
	private void buscaAutor(){
		FindDialog dialogAutores = new FindDialog(shell);
		dialogAutores.setText("Buscar autor");
		dialogAutores.addColumn("nomeCompleto", "Nome", true);
		dialogAutores.setFindText(txtBuscaAutor.getText());
		dialogAutores.setIcons(new Image[]{
				SWTResourceManager.getImage(this.getClass(), "/img/ic_search_black_18dp.png"),
				SWTResourceManager.getImage(this.getClass(), "/img/ic_add_circle_black_24dp.png"),
				SWTResourceManager.getImage(this.getClass(), "/img/ic_done_black_24dp.png"),
				SWTResourceManager.getImage(this.getClass(), "/img/ic_clear_black_24dp.png")
		});
		dialogAutores.setFindSource(new FindSource() {
			@Override
			public List<?> getList(int index, String text) {
				AutorDAO dao = new AutorDAO();
				List<Autor> list = dao.findList(text, livro.getAutores());
				return list;
			}
		});
		autor = (Autor)dialogAutores.open();
		btnAddAutor.setEnabled(autor != null);
		if (autor != null)
			txtBuscaAutor.setText(autor.getNomeCompleto());
	}
	
	private void adicionaAutor(){
		if (autor != null){
			livro.addAutor(autor);
			lstAutores.add(autor.getNomeCompleto());
			txtBuscaAutor.setText("");
			autor = null;
			btnAddAutor.setEnabled(false);
		}
	}
	
	private void removeAutor(){
		int index = lstAutores.getSelectionIndex();
		if (index >= 0 && Dialog.questionExclude()){
			livro.removeAutor(index);
			lstAutores.remove(index);
			btnRemAutor.setEnabled(false);
			modificado = true;
		}
	}
	
	private void addCategoria(){
		int index = cmbAddCategoria.getSelectionIndex();
		if (index < 0){
			boolean insert = true;
			for (Categoria categoria : categoriasDisp) {
				if (cmbAddCategoria.getText().compareTo(categoria.getDescricao())==0){
					livro.addCategoria(categoria);
					index = categoriasDisp.indexOf(categoria);
					cmbAddCategoria.remove(index);
					categoriasDisp.remove(index);
					lstCategorias.add(categoria.getDescricao());
					insert = false;
					break;
				}
			}
			if (insert && !cmbAddCategoria.getText().isEmpty()){
				Categoria categoria = new Categoria();
				categoria.setDescricao(cmbAddCategoria.getText());
				CategoriaDAO categoriaDAO = new CategoriaDAO();
				try {
					categoriaDAO.insert(categoria);
				} catch (ValidationException e) {
					Dialog.error(shell, e.getMessage());
				}
				livro.addCategoria(categoria);
				lstCategorias.add(categoria.getDescricao());
				categorias.add(categoria);
				cmbAddCategoria.setText("");
			}
		}else{
			livro.addCategoria(categoriasDisp.get(index));
			lstCategorias.add(categoriasDisp.get(index).getDescricao());
			cmbAddCategoria.remove(index);
			categoriasDisp.remove(index);
		}
	}
	
	private void removeCategoria(){
		int index = lstCategorias.getSelectionIndex();
		if (index >= 0 && Dialog.questionExclude()){
			livro.removeCategoria(index);
			lstCategorias.remove(index);
			resetComboCategorias();
		}
	}
	
	private boolean salvar(){
		if (txtTitulo.getText().trim().length() > 0)
			livro.setTitulo(txtTitulo.getText());
		if (txtResumo.getText().trim().length() > 0)
			livro.setResumo(txtResumo.getText());
		if (txtISBN.getText().trim().length() > 0)
			livro.setIsbn(Integer.parseInt(txtISBN.getText()));
		if (txtCutter.getText().trim().length() > 0)
			livro.setCutter(txtCutter.getText());
		if (cmbEditora.getSelectionIndex() >= 0){
			livro.setEditora(editoras.get(cmbEditora.getSelectionIndex()));
		}else if (cmbEditora.getText().trim().length() > 0){
			boolean insert = true;
			for (Editora editora : editoras) {
				if (cmbEditora.getText().compareTo(editora.getNome()) == 0){
					insert = false;
					livro.setEditora(editora);
					break;
				}
			}
			if (insert){
				Editora editora = new Editora();
				editora.setNome(cmbEditora.getText());
				EditoraDAO dao = new EditoraDAO();
				try {
					dao.insert(editora);
				} catch (ValidationException e) {
					throw new RuntimeException(e.getMessage());
				}
				livro.setEditora(editora);
			}
		}
		if (txtEdicao.getText().trim().length() > 0)
			livro.setEdicao(txtEdicao.getText());
		if (txtVolume.getText().trim().length() > 0)
			livro.setVolume(txtVolume.getText());
		if (txtNumPag.getText().trim().length() > 0)
			livro.setNumPaginas(Integer.parseInt(txtNumPag.getText()));
		if (cmbAssunto.getSelectionIndex() >= 0)
			livro.setAssunto(assuntos.get(cmbAssunto.getSelectionIndex()));
		if (txtDataPublicacao.getText().trim().length() > 0){
			TemporalAccessor ta = FORMATADOR.parse(txtDataPublicacao.getText());
			livro.setDataPublicacao(LocalDate.from(ta));
		}
		if (txtLocalPublicacao.getText().trim().length() > 0)
			livro.setLocalPublicacao(txtLocalPublicacao.getText());
		LivroDAO livroDAO = new LivroDAO();
		try {
			livroDAO.save(livro);
			result = livro;
			modificado = false;
			btnSalvar.setEnabled(false);
			Dialog.confirmation(shell, "Livro salvo!");
			return true;
		} catch (ValidationException e) {
			Dialog.error(shell, e.getMessage());
			return false;
		}
	}
	
	protected void btnBuscaAutorWidgetSelected(SelectionEvent arg0) {
		buscaAutor();
	}
	
	protected void btnAddAutorWidgetSelected(SelectionEvent arg0) {
		adicionaAutor();
	}
	
	protected void btnRemAutorWidgetSelected(SelectionEvent arg0) {
		removeAutor();
	}
	
	protected void lstAutoresWidgetSelected(SelectionEvent arg0) {
		btnRemAutor.setEnabled(true);
	}
	
	protected void txtBuscaAutorKeyPressed(KeyEvent arg0) {
		if (arg0.keyCode == SWT.CR || arg0.keyCode == SWT.KEYPAD_CR){
			if (autor != null)
				adicionaAutor();
			else
				buscaAutor();
		}else{
			autor = null;
		}
	}
	
	protected void lstAutoresKeyPressed(KeyEvent arg0) {
		if (arg0.keyCode == SWT.DEL)
			removeAutor();
	}
	
	protected void cmbAddCategoriaKeyPressed(KeyEvent arg0) {
		if (arg0.keyCode == SWT.CR || arg0.keyCode == SWT.KEYPAD_CR)
			addCategoria();
	}
	
	protected void btnAddCategoriaWidgetSelected(SelectionEvent arg0) {
		addCategoria();
	}
	
	protected void btnRemCategoriaWidgetSelected(SelectionEvent arg0) {
		removeCategoria();
	}
	
	protected void lstCategoriaKeyPressed(KeyEvent arg0) {
		if (arg0.keyCode == SWT.DEL && lstCategorias.getSelectionIndex() >= 0)
			removeCategoria();
	}
	
	protected void cmbAddCategoriaWidgetSelected(SelectionEvent arg0) {
		btnAddCategoria.setEnabled(true);
	}
	
	protected void cmbAddCategoriaModifyText(ModifyEvent arg0) {
		btnAddCategoria.setEnabled(!cmbAddCategoria.getText().isEmpty());
	}
	
	protected void lstCategoriasWidgetSelected(SelectionEvent arg0) {
		btnRemCategoria.setEnabled(true);
	}
	
	protected void cmbAssuntoKeyPressed(KeyEvent arg0) {
		if (arg0.keyCode == SWT.DEL)
			cmbAssunto.deselectAll();
	}
	
	protected void btnSalvarWidgetSelected(SelectionEvent arg0) {
		salvar();
	}
	
	protected void shellKeyTraversed(TraverseEvent arg0) {
		if (arg0.keyCode == SWT.ESC && modificado)
			arg0.doit = false;
	}
	
	protected void shellShellClosed(ShellEvent arg0) {
		if (modificado && !Dialog.question(shell, "Os dados ainda não foram salvos;\n Sair mesmo assim?")){
			arg0.doit = false;
		}
	}
	
	protected void tableExemplaresSelectionEvent(Event arg0){
		tbtEdtExemplar.setEnabled(true);
		tbtRemExemplar.setEnabled(true);
	}
	
	protected void tableExemplaresMouseDownEvent(Event arg0){
		int index = tableExemplares.getSelectionIndex();
		tbtEdtExemplar.setEnabled(index >= 0);
		tbtRemExemplar.setEnabled(index >= 0);
	}
	
	private boolean verificaEstado(){
		if (modificado){
			if (Dialog.question(shell, "Você precisa salvar os dados antes;\nConfirma?"))
				return salvar();
			else
				return false;
		}else{
			if (livro.getId() == null)
				return false;
			return true;
		}
	}
	
	private void inserirExemplar(){
		if (verificaEstado()){
			DialogExemplar dialog = new DialogExemplar(shell, livro);
			Exemplar exemplar = (Exemplar)dialog.open();
			if (exemplar != null)
				tableExemplares.add(exemplar);
		}
	}
	
	private void editarExemplar(){
		int index = tableExemplares.getSelectionIndex();
		if (index >= 0 && verificaEstado()){
			Exemplar exemplar = (Exemplar)tableExemplares.getSelection();
			DialogExemplar dialog = new DialogExemplar(shell, exemplar);
			exemplar = (Exemplar)dialog.open();
			if (exemplar != null)
				tableExemplares.set(index, exemplar);
		}
	}
	
	private void excluirExemplar(){
		int index = tableExemplares.getSelectionIndex();
		if (index >= 0 && Dialog.questionExclude()){
			Exemplar exemplar = (Exemplar)tableExemplares.getSelection();
			ExemplarDAO dao = new ExemplarDAO();
			try {
				dao.delete(exemplar);
				tableExemplares.remove(index);
			} catch (ValidationException e) {
				Dialog.error(shell, e.getMessage());
			}
		}
	}
	
	protected void tbtAddExemplarWidgetSelected(SelectionEvent arg0) {
		inserirExemplar();
	}
	
	protected void tableExemplaresMouseDoubleClickEvent(Event arg0){
		editarExemplar();
	}
	
	protected void tbtEdtExemplarWidgetSelected(SelectionEvent arg0) {
		editarExemplar();
	}
	
	protected void tbtRemExemplarWidgetSelected(SelectionEvent arg0) {
		excluirExemplar();
	}
	
	protected void tableExemplaresKeyDownEvent(Event arg0){
		excluirExemplar();
	}
	
}
