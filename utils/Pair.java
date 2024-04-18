package utils;

import java.io.Serializable;

public class Pair<L,R> implements Serializable {
    private static final long serialVersionUID = -645028699740444595L;
    private L l;
    private R r;
    public Pair(L l, R r){
        this.l = l;
        this.r = r;
    }
    public L getLeft(){ return l; }
    public R getRight(){ return r; }
    public void setL(L l){ this.l = l; }
    public void setR(R r){ this.r = r; }
}
