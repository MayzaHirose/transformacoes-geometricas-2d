/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package computacaografica;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

/**
 *
 * @author Mayza Hirose
 */
public class FiguraGeometrica {
    
    private final ObjectProperty<Ponto> ponto1;
    private final ObjectProperty<Ponto> ponto2;
    private final ObjectProperty<Ponto> ponto3;
    
    private final ObjectProperty<Forma> forma;
    private final ObjectProperty<Color> cor;

    public FiguraGeometrica() {
        this.cor = new SimpleObjectProperty<>();
        this.forma = new SimpleObjectProperty<>();
        this.ponto1 = new SimpleObjectProperty<>();
        this.ponto2 = new SimpleObjectProperty<>();
        this.ponto3 = new SimpleObjectProperty<>();
    }

    public Ponto getPonto1() {
        return ponto1.get();
    }

    public void setPonto1(Ponto ponto1) {
        this.ponto1.set(ponto1);
    }
    
    public ObjectProperty<Ponto> ponto1Property() {
        return ponto1;
    }

    public Ponto getPonto2() {
        return ponto2.get();
    }

    public void setPonto2(Ponto ponto2) {
        this.ponto2.set(ponto2);
    }
    
    public ObjectProperty<Ponto> ponto2Property() {
        return ponto2;
    }

    public Ponto getPonto3() {
        return ponto3.get();
    }

    public void setPonto3(Ponto ponto3) {
        this.ponto3.set(ponto3);
    }
    
    public ObjectProperty<Ponto> ponto3Property() {
        return ponto3;
    }

    public Forma getForma() {
        return forma.get();
    }

    public void setForma(Forma forma) {
        this.forma.set(forma);
    }
    
    public ObjectProperty<Forma> formaProperty() {
        return forma;
    }

    public Color getCor() {
        return cor.get();
    }

    public void setCor(Color cor) {
        this.cor.set(cor);
    }
    
    public ObjectProperty<Color> corProperty() {
        return cor;
    }
}
