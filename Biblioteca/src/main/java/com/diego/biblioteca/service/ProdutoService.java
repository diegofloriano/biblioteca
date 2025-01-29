package com.diego.biblioteca.service;

import com.diego.biblioteca.dao.ProdutoDAO;
import com.diego.biblioteca.Produto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {
    private final ProdutoDAO produtoDAO;

    public ProdutoService(ProdutoDAO produtoDAO) {
        this.produtoDAO = produtoDAO;
    }

    public Produto adicionarProduto(Produto produto) {
        if (produto == null || produto.getNome() == null || produto.getNome().isEmpty()) {
            throw new IllegalArgumentException("O nome do produto não pode ser nulo ou vazio");
        }
        
        return produtoDAO.incluir(produto);
    }


    public Produto atualizarProduto(int id, Produto produtoAtualizado) {
        return produtoDAO.alterar(id, produtoAtualizado);
    }


    public boolean excluirProduto(int id) {
        return produtoDAO.excluir(id);
    }

    public Produto buscarProduto(int id) {
        return produtoDAO.pesquisar(id);
    }

    public List<Produto> listarTodos() {
        return produtoDAO.mostrarTudo();
    }
}