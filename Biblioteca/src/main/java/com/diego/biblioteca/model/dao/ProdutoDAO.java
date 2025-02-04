package com.diego.biblioteca.dao;

import com.diego.biblioteca.Produto;

import java.util.List;

public interface ProdutoDAO {
    Produto incluir(Produto produto);
    Produto alterar(int id, Produto produto);
    boolean excluir(int id);
    Produto pesquisar(int id);
    List<Produto> mostrarTudo();
}