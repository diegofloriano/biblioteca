package com.diego.biblioteca.service;

import com.diego.biblioteca.dao.VendaDAO;
import com.diego.biblioteca.Venda;
import org.springframework.stereotype.Service;

@Service
public class VendaService {
    private final VendaDAO vendaDAO;

    public VendaService(VendaDAO vendaDAO) {
        this.vendaDAO = vendaDAO;
    }

    public boolean registrarVenda(Venda venda) {
        return vendaDAO.inserirVenda(venda);
    }

    public Venda buscarVenda(int id) {
        return vendaDAO.buscarVenda(id);
    }
}