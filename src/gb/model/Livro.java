package gb.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Livro {

	private Integer id;
	private String titulo;
	private Integer isbn;
	private String cutter;
	private String resumo;
	private Editora editora;
	private String edicao;
	private String volume;
	private Integer numPaginas;
	private Assunto assunto;
	private LocalDate dataPublicacao;
	private String localPublicacao;
	
	private List<Autor> autores = new ArrayList<>();
	private List<Categoria> categorias = new ArrayList<>();
	private List<Exemplar> exemplares = new ArrayList<>();
	private int numExemplares = 0;
	
	private boolean atualizaAutores = false;
	private boolean atualizaCategorias = false;
	
	//Transient
	private String nomeAutores;
	
	public Livro() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public Integer getIsbn() {
		return isbn;
	}

	public void setIsbn(Integer isbn) {
		this.isbn = isbn;
	}

	public String getCutter() {
		return cutter;
	}

	public void setCutter(String cutter) {
		this.cutter = cutter;
	}

	public String getResumo() {
		return resumo;
	}

	public void setResumo(String resumo) {
		this.resumo = resumo;
	}

	public Editora getEditora() {
		return editora;
	}

	public void setEditora(Editora editora) {
		this.editora = editora;
	}

	public String getEdicao() {
		return edicao;
	}

	public void setEdicao(String edicao) {
		this.edicao = edicao;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public Integer getNumPaginas() {
		return numPaginas;
	}

	public void setNumPaginas(Integer numPaginas) {
		this.numPaginas = numPaginas;
	}

	public Assunto getAssunto() {
		return assunto;
	}

	public void setAssunto(Assunto assunto) {
		this.assunto = assunto;
	}

	public LocalDate getDataPublicacao() {
		return dataPublicacao;
	}

	public void setDataPublicacao(LocalDate dataPublicacao) {
		this.dataPublicacao = dataPublicacao;
	}

	public String getLocalPublicacao() {
		return localPublicacao;
	}

	public void setLocalPublicacao(String localPublicacao) {
		this.localPublicacao = localPublicacao;
	}

	public List<Autor> getAutores() {
		return autores;
	}

	public void setAutores(List<Autor> autores) {
		this.autores = autores;
		setNomeAutores();
	}

	public void addAutor(Autor autor){
		autores.add(autor);
		setNomeAutores();
		atualizaAutores = true;
	}
	
	public void setAutor(int index, Autor autor){
		autores.set(index, autor);
		setNomeAutores();
		atualizaAutores = true;
	}
	
	public void removeAutor(int index){
		autores.remove(index);
		setNomeAutores();
		atualizaAutores = true;
	}
	
	public boolean atualizaAutores(){
		return atualizaAutores;
	}	

	public List<Categoria> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}

	public void addCategoria(Categoria categoria){
		categorias.add(categoria);
		atualizaCategorias = true;
	}
	
	public void setCategoria(int index, Categoria categoria){
		categorias.set(index, categoria);
		atualizaCategorias = true;
	}
	
	public void removeCategoria(int index){
		categorias.remove(index);
		atualizaCategorias = true;
	}
	
	public boolean atualizaCategorias(){
		return atualizaCategorias;
	}

	public int getNumExemplares() {
		return numExemplares;
	}

	public void setNumExemplares(int numExemplares) {
		this.numExemplares = numExemplares;
	}

	public List<Exemplar> getExemplares() {
		return exemplares;
	}

	public void setExemplares(List<Exemplar> exemplares) {
		this.exemplares = exemplares;
	}
	
	public String toString(){
		return id+" - "+titulo;
	}
	
	private void setNomeAutores(){
		StringBuilder builder = new StringBuilder();
		for (Autor autor : autores) {
			if (builder.length() > 0)
				builder.append("; ");
			builder.append(autor.getNomeCompleto());
		}
		if (builder.length() > 0)
			nomeAutores = builder.toString();
	}

	public String getNomeAutores() {
		return nomeAutores;
	}

}
