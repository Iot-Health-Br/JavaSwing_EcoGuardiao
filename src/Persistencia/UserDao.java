package Persistencia;

import Conexão.DatabaseConnection;
import Modelo.UserModelo;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserDao implements IUserDao{
    // Busca o ID do usuário
    private static final String TABELA_DENUNCIAS = "tabelaDeDenuncias";
    private static final String COLUNA_ID = "id";
    private static final String COLUNA_PROTOCOLO = "protocolo";
    private static final String COLUNA_DATA = "data";
    private static final String COLUNA_DATA_ATUALIZAÇÃO = "data_atualização";
    private static final String COLUNA_STATUS = "status";
    private static final String COLUNA_SIGILO = "sigilo";
    private static final String COLUNA_CATEGORIA = "categoria";
    private static final String COLUNA_RUA = "rua";
    private static final String COLUNA_BAIRRO = "bairro";
    private static final String COLUNA_FOTO = "foto";
    private static final String COLUNA_MUNICIPIO = "municipio";
    private static final String COLUNA_CEP = "cep";
    private static final String COLUNA_LATITUDE = "latitude";
    private static final String COLUNA_LONGITUDE = "longitude";
    private static final String COLUNA_PONTO_Referencia = "referencia";
    private static final String COLUNA_AUTOR = "autor";
    private static final String COLUNA_DESCRICAO = "descricao";
    private static final String COLUNA_ATUALIZACAO = "atualizacao";
    private static final String COLUNA_IdUsuario = "idUsuario";
    private static final String COLUNA_IdAnalista = "idAnalista";


    public UserDao(){

        criarTabela();
    }
    private void criarTabela() {
        try (Connection conexao = DatabaseConnection.getConnection();
             Statement statement = conexao.createStatement()) {
            String query = String.format("CREATE TABLE IF NOT EXISTS %s (" +
                            "%s SERIAL PRIMARY KEY, " + // Coluna ID
                            "%s VARCHAR NOT NULL, " +  // Coluna PROTOCOLO
                            "%s DATE, " +  // Coluna DATA
                            "%s DATE, " +  // Coluna DATA ATUALIZAÇÃO
                            "%s VARCHAR(15) NOT NULL, " +  // Coluna STATUS
                            "%s VARCHAR(5) NOT NULL, " +  // Coluna SIGILO
                            "%s VARCHAR(50) NOT NULL, " +  // Coluna CATEGORIA
                            "%s VARCHAR(255) NOT NULL, " + // Coluna MUNICIPIO
                            "%s INTEGER, " + // Coluna ID USUÁRIO
                            "%s INTEGER)",  // Coluna ID ANALISTA
                    TABELA_DENUNCIAS, COLUNA_ID, COLUNA_PROTOCOLO, COLUNA_DATA,COLUNA_DATA_ATUALIZAÇÃO, COLUNA_STATUS, COLUNA_SIGILO, COLUNA_CATEGORIA,
                    COLUNA_MUNICIPIO, COLUNA_IdUsuario, COLUNA_IdAnalista);
            statement.executeUpdate(query);
        }
        catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao criar tabela de Denuncia");
        }
    }

    @Override
    public int getLastDenunciaId() {
        int id = 0;
        try (Connection conexao = DatabaseConnection.getConnection();
             PreparedStatement statement = conexao.prepareStatement(
                     String.format("SELECT * FROM %s ORDER BY id DESC LIMIT 1", TABELA_DENUNCIAS));
             ResultSet rs = statement.executeQuery()) {
            if (rs.next()) {
                id = rs.getInt("id");}}
        catch (Exception e) {
            e.printStackTrace();}

        return id;
    }

    @Override
    public boolean adicionarDenuncia(UserModelo usuario) {
        try (Connection conexao = DatabaseConnection.getConnection();
             PreparedStatement insercaoStatement = conexao.prepareStatement(
                     String.format("INSERT INTO %s (%s,%s, %s, %s, %s, %s, %s) VALUES (?,?, ?, ?, ?, ?, ?)",
                             TABELA_DENUNCIAS, COLUNA_PROTOCOLO, COLUNA_DATA, COLUNA_STATUS, COLUNA_SIGILO, COLUNA_CATEGORIA,
                             COLUNA_MUNICIPIO, COLUNA_IdUsuario), Statement.RETURN_GENERATED_KEYS)) {

            // Configuração dos parâmetros
            insercaoStatement.setString(1, usuario.getProtocolo());
            insercaoStatement.setDate(2, usuario.getData());
            insercaoStatement.setString(3, usuario.getStatus());
            insercaoStatement.setString(4, usuario.getSigilo());
            insercaoStatement.setString(5, usuario.getCategoria());
            insercaoStatement.setString(6, usuario.getMunicipio());
            insercaoStatement.setInt(7, usuario.getIdUsuario());

            int affectedRows = insercaoStatement.executeUpdate();

            if (affectedRows > 0) {
                // Recuperar o ID gerado
                try (ResultSet generatedKeys = insercaoStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        // Atualiza o id da denuncia
                        usuario.setId(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<UserModelo>listarDenuncia(int idUsuario) {
        List<UserModelo> denuncias = new ArrayList<>();

        String query = "SELECT * FROM " + TABELA_DENUNCIAS + " WHERE IdUsuario = ? ORDER BY id ASC";

        try (Connection conexao = DatabaseConnection.getConnection();
             PreparedStatement ps = conexao.prepareStatement(query)) {

            ps.setInt(1, idUsuario); // Define o IdUsuario no parâmetro da query

            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                String protocolo = resultSet.getString(COLUNA_PROTOCOLO);
                Date data = resultSet.getDate(COLUNA_DATA);
                String status = resultSet.getString(COLUNA_STATUS);
                String sigilo = resultSet.getString(COLUNA_SIGILO);
                String categoria = resultSet.getString(COLUNA_CATEGORIA);
                String municipio = resultSet.getString(COLUNA_MUNICIPIO); // Ajuste na coluna aqui
                UserModelo denuncia = new UserModelo(protocolo, data, status, sigilo, categoria, municipio);
                denuncias.add(denuncia);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return denuncias;
    }
}
