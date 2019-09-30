package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import model.Processo;

public class Comparador {

    public void comparar(ArrayList<Processo> listaDeProcessos, int variavel) {

        Collections.sort(listaDeProcessos, new Comparator() {
            public int compare(Object o1, Object o2) {
                Processo p1 = (Processo) o1;
                Processo p2 = (Processo) o2;
              
                switch (variavel) {
                    case 1:
                        return p1.getTempoServico() < p2.getTempoServico() ? -1 : (p1.getTempoServico() > p2.getTempoServico() ? +1 : 0);
                    case 2:
                        return p1.getRestante() < p2.getRestante() ? -1 : (p1.getRestante() > p2.getRestante() ? +1 : 0);
                    case 4:
                        return p1.getPrioridadeRelativa() > p2.getPrioridadeRelativa() ? -1 : (p1.getPrioridadeRelativa() < p2.getPrioridadeRelativa() ? +1 : 0);

                }
                return 0;
            }

        });

    }

    public void loteria(ArrayList<Processo> listaDeProcessos) {
        int ganhador;

        ArrayList<Integer> bilhetes = new ArrayList<>();

        for (int i = 0; i < listaDeProcessos.size(); i++) {
            for (int j = listaDeProcessos.get(i).getPrioridadeRelativa(); j > 0; j--) {
                bilhetes.add(i);
            }
        }

        Collections.shuffle(bilhetes);
        if (!bilhetes.isEmpty()) {
            ganhador = bilhetes.get(0);
            Processo win = listaDeProcessos.remove(ganhador);
            listaDeProcessos.add(0, win);
        }

    }



}
