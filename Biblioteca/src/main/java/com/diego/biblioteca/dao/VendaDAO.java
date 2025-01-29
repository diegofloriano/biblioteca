package com.diego.biblioteca.dao;

import com.diego.biblioteca.Venda;

public interface VendaDAO {
    boolean inserirVenda(Venda venda);
    Venda buscarVenda(int id);
}