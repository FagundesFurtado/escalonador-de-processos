package model;

import java.util.ArrayList;

public class Processador {

    private int tempoTotal, ociosidadeMaxima, tempoOciosoAtual;
    private ArrayList<Integer> tempoOcioso;
    private int quantum;
    private boolean vazio;
    private Processo executando;
    private int quantumRestante;
    private int ociosoTotal;

    public void setExecutando(Processo executando) {

        this.executando = executando;
        quantumRestante = quantum;

        executando.setTotalEspera(executando.getEspera() + executando.getTotalEspera());

        if (executando.getMaximaEspera() < executando.getEspera()) {
            executando.setMaximaEspera(executando.getEspera());
        }
        executando.setEspera(0);

        if (quantum == 0) {
            quantumRestante = executando.getTempoServico();
        }

        vazio = false;
        if (tempoOciosoAtual != 0) {
            tempoOcioso.add(tempoOciosoAtual);
            tempoOciosoAtual = 0;
        }
    }

    public int getOciosoTotal() {
        return ociosoTotal;
    }

    public int getQuantumRestante() {
        return quantumRestante;
    }

    public Processo processa() {
        if (!vazio) {
            executando.setRestante(executando.getRestante() - 1);
            quantumRestante--;
            if (quantumRestante == 0 || executando.getRestante() == 0) {

                vazio = true;
                return executando;
            }
        } else {
            ociosoTotal++;
            System.out.println("Ocioso total CPU "+ociosoTotal);
            tempoOciosoAtual++;

        }

        return null;
    }

    public Processador() {
        quantum = 0;
        vazio = true;
        tempoOcioso = new ArrayList<>();
    }

    public void setQuantum(int quantum) {
        this.quantum = quantum;
    }

    public boolean isVazio() {
        return vazio;
    }

    public void setVazio(boolean vazio) {
        this.vazio = vazio;
    }

    public int getTempoTotal() {
        return tempoTotal;
    }

    public void setTempoTotal(int tempoTotal) {
        this.tempoTotal = tempoTotal;
    }

    public int getOciosidade() {
        return ociosidadeMaxima;
    }

    public void setOciosidade(int ociosidade) {
        this.ociosidadeMaxima = ociosidade;
    }

    public ArrayList<Integer> getTempoOcioso() {
        return tempoOcioso;
    }

    public void setTempoOcioso(ArrayList<Integer> tempoOcioso) {
        this.tempoOcioso = tempoOcioso;
    }

    public Processo getExecutando() {
        return executando;
    }
}
