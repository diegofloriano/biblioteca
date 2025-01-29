package com.diego.biblioteca.dao;

import com.diego.biblioteca.Produto;
import org.springframework.stereotype.Repository;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

@Repository
public class ProdutoDAOImpl implements ProdutoDAO {

    private final DataSource dataSource;

    public ProdutoDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Produto incluir(Produto produto) {
        String sql = "INSERT INTO produto (nome, quantidade) VALUES (?, ?)";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, produto.getNome());
            ps.setInt(2, produto.getQuantidade());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        produto.setId(rs.getInt(1)); // Captura o ID gerado pelo banco
                    }
                }
            }

            return produto;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao inserir produto", e);
        }
    }


    @Override
    public Produto alterar(int id, Produto produto) {
        String sql = "UPDATE produto SET nome = ?, quantidade = ? WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, produto.getNome());
            ps.setInt(2, produto.getQuantidade());
            ps.setInt(3, id);
            
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
            	produto.setId(id);
                return produto;
            } else {
                throw new SQLException("Erro ao atualizar produto, nenhum registro foi alterado.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao atualizar produto", e);
        }
    }


    @Override
    public boolean excluir(int id) {
        String sql = "DELETE FROM produto WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Produto pesquisar(int id) {
        String sql = "SELECT * FROM produto WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Produto produto = new Produto();
                    produto.setId(rs.getInt("id"));
                    produto.setNome(rs.getString("nome"));
                    produto.setQuantidade(rs.getInt("quantidade"));
                    return produto;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Produto> mostrarTudo() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produto";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Produto produto = new Produto();
                produto.setId(rs.getInt("id"));
                produto.setNome(rs.getString("nome"));
                produto.setQuantidade(rs.getInt("quantidade"));
                produtos.add(produto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produtos;
    }
}