package com.diego.biblioteca.dao;

import com.diego.biblioteca.ItemVenda;
import com.diego.biblioteca.Venda;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class VendaDAOImpl implements VendaDAO {

    private final DataSource dataSource;

    public VendaDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public boolean inserirVenda(Venda venda) {
        String sqlVenda = "INSERT INTO venda (data) VALUES (?)";
        String sqlItemVenda = "INSERT INTO item_venda (id_venda, id_produto, quantidade) VALUES (?, ?, ?)";
        String sqlUpdateProduto = "UPDATE produto SET quantidade = quantidade - ? WHERE id = ?";

        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement psVenda = conn.prepareStatement(sqlVenda, Statement.RETURN_GENERATED_KEYS)) {
                psVenda.setDate(1, new java.sql.Date(venda.getData().getTime()));
                psVenda.executeUpdate();

                try (ResultSet rs = psVenda.getGeneratedKeys()) {
                    if (rs.next()) {
                        venda.setId(rs.getInt(1));
                    }
                }
            }

            for (ItemVenda item : venda.getItens()) {
                try (PreparedStatement psItem = conn.prepareStatement(sqlItemVenda);
                     PreparedStatement psUpdateProduto = conn.prepareStatement(sqlUpdateProduto)) {
                    psItem.setInt(1, venda.getId());
                    psItem.setInt(2, item.getIdProduto());
                    psItem.setInt(3, item.getQuantidade());
                    psItem.executeUpdate();

                    psUpdateProduto.setInt(1, item.getQuantidade());
                    psUpdateProduto.setInt(2, item.getIdProduto());
                    psUpdateProduto.executeUpdate();
                }
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Venda buscarVenda(int id) {
        String sqlVenda = "SELECT * FROM venda WHERE id = ?";
        String sqlItens = "SELECT * FROM item_venda WHERE id_venda = ?";
        Venda venda = null;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement psVenda = conn.prepareStatement(sqlVenda)) {
            psVenda.setInt(1, id);

            try (ResultSet rsVenda = psVenda.executeQuery()) {
                if (rsVenda.next()) {
                    venda = new Venda();
                    venda.setId(rsVenda.getInt("id"));
                    venda.setData(rsVenda.getDate("data"));

                    List<ItemVenda> itens = new ArrayList<>();
                    try (PreparedStatement psItens = conn.prepareStatement(sqlItens)) {
                        psItens.setInt(1, id);
                        try (ResultSet rsItens = psItens.executeQuery()) {
                            while (rsItens.next()) {
                                ItemVenda item = new ItemVenda();
                                item.setId(rsItens.getInt("id"));
                                item.setIdVenda(rsItens.getInt("id_venda"));
                                item.setIdProduto(rsItens.getInt("id_produto"));
                                item.setQuantidade(rsItens.getInt("quantidade"));
                                itens.add(item);
                            }
                        }
                    }
                    venda.setItens(itens);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return venda;
    }
}