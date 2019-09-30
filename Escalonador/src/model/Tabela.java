package model;

import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class Tabela extends AbstractTableModel {

    DecimalFormat df = new DecimalFormat("00.00");

    private final ArrayList<Processo> dados;
    String[] colunas = {"Chegada", "Servico", "Prioridade Original", "Prioridade Relativa", "Restante", "Espera"};

    public Tabela(ArrayList<Processo> vetor) {
        this.dados = vetor;
    }

    @Override
    public String getColumnName(int i) {
        return colunas[i];
    }

    @Override
    public int getRowCount() {
        return dados.size();
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
    }

    @Override
    public Object getValueAt(int linha, int coluna) {
        switch (coluna) {
            case 0:
                return dados.get(linha).getTempoChegada();
            case 1:
                return dados.get(linha).getTempoServico();

            case 2:
                return dados.get(linha).getPrioridadeOriginal();
            case 3:
                return dados.get(linha).getPrioridadeRelativa();
            case 4:
                return dados.get(linha).getRestante();
            case 5:
                return dados.get(linha).getEspera();

            default:
                System.out.println("Erro table");
                break;
        }
        return null;

    }

    public ArrayList<Processo> getDados() {
        return dados;
    }

}
