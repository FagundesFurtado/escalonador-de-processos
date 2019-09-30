package main;

import controller.Escalonador;
import view.telaPrincipal;

public class main {

    public static void main(String[] args) {

        Escalonador escalonator = new Escalonador();
        telaPrincipal view = new telaPrincipal(escalonator);
        escalonator.setTp(view);
    }

}
