package Persistencia;

import Modelo.UserModelo;

import java.util.List;

public interface IUserDao {
    boolean adicionarDenuncia(UserModelo usuario);

    List<UserModelo> listarDenuncia(int userId);
}
