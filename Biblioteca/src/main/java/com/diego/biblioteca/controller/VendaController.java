package com.diego.biblioteca.controller;

import com.diego.biblioteca.Venda;
import com.diego.biblioteca.service.VendaService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vendas")
public class VendaController {

    private final VendaService vendaService;

    public VendaController(VendaService vendaService) {
        this.vendaService = vendaService;
    }

    @PostMapping
    public boolean registrarVenda(@RequestBody Venda venda) {
        return vendaService.registrarVenda(venda);
    }

    @GetMapping("/{id}")
    public Venda buscarVenda(@PathVariable int id) {
        return vendaService.buscarVenda(id);
    }
}