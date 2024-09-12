package Model;

import java.util.Date;

public class Sorteig implements Comparable<Sorteig> {
    private Date dataSorteig;


    public Sorteig(Date dataSorteig) {
        this.dataSorteig = dataSorteig;
    }


    public Date getDataSorteig() {
        return dataSorteig;
    }


    @Override
    public int compareTo(Sorteig comparacio) {

        return this.dataSorteig.compareTo(comparacio.dataSorteig);
    }
}