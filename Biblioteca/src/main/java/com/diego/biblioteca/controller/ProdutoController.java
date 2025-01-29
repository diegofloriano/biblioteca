package com.diego.biblioteca.controller;

import com.diego.biblioteca.Produto;
import com.diego.biblioteca.service.ProdutoService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PostMapping
    public ResponseEntity<?> inserirProduto(@RequestBody Produto produto) {
        try {
            Produto produtoSalvo = produtoService.adicionarProduto(produto);
            return ResponseEntity.status(HttpStatus.CREATED).body(produtoSalvo);  
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());  
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao inserir produto");  
        }
    }



    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarProduto(@PathVariable int id, @RequestBody Produto produto) {
        try {
            Produto produtoAtualizado = produtoService.atualizarProduto(id, produto);
            return ResponseEntity.ok(produtoAtualizado);  //
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado"); 
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar produto"); 
        }
    }

    @DeleteMapping("/{id}")
    public boolean excluirProduto(@PathVariable int id) {
        return produtoService.excluirProduto(id);
    }

    @GetMapping("/{id}")
    public Produto buscarProduto(@PathVariable int id) {
        return produtoService.buscarProduto(id);
    }

    @GetMapping
    public List<Produto> listarProdutos() {
        return produtoService.listarTodos();
    }
}