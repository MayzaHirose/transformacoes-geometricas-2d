/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package computacaografica;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 *
 * @author Mayza Hirose 
 */
public class AplicacaoController implements Initializable {
    
    @FXML
    private Canvas canvas;
    @FXML
    private Label lblInstrucoes;
    @FXML
    private TableView<FiguraGeometrica> tabelaFormas;
    @FXML
    private TableColumn<FiguraGeometrica, Forma> colunaTabela;
    @FXML
    private TextField valor1;
    @FXML
    private TextField valor2;
    @FXML
    private TextField valor3;
    @FXML
    private Button feito;
    
    private GraphicsContext contexto;
    
    private Forma formaSelecionada = null; 
    private Transformacao transformacaoSelecionada = null;
    
    private FiguraGeometrica figGeo;
    private FiguraGeometrica figSelecionada;
    
    private Boolean isPonto1 = Boolean.TRUE;
    private Boolean isPonto2 = Boolean.FALSE;
    private Double ponto1X;
    private Double ponto1Y;
    private Double ponto2X;
    private Double ponto2Y;
   
    private final ObservableList<FiguraGeometrica> figurasDesenhadas = FXCollections.observableArrayList();
    @FXML
    private ImageView imgTriangulo;
    @FXML
    private ImageView imgRetangulo;
    @FXML
    private ImageView imgCirculo;
    @FXML
    private ImageView imgRotacao;
    @FXML
    private ImageView imgTranslacao;
    @FXML
    private ImageView imgEscala;
    @FXML
    private ImageView imgLinha;
          
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        inicializaImagens();
        contexto = canvas.getGraphicsContext2D();  
        colunaTabela.setCellValueFactory(cellData -> cellData.getValue().formaProperty());
        tabelaFormas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if(figSelecionada != null){
                figSelecionada.setCor(Color.BLACK);
            }
            figSelecionada = newSelection;
            if(figSelecionada != null){
                figSelecionada.setCor(Color.RED);
            }
            valor3.setVisible(false);
            contexto.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            redesenhar();
        });
        tabelaFormas.setItems(figurasDesenhadas);
        lblInstrucoes.setText("Selecione a FIGURA que deseja desenhar nos botões à esquerda.");
    }    
    
    @FXML
    private void desenharLinha(ActionEvent event) {
        zeraFlags();
        tabelaFormas.getSelectionModel().clearSelection();
        formaSelecionada = Forma.LINHA;
        lblInstrucoes.setText("Clique em DOIS PONTOS no CANVAS BRANCO para formar uma linha.");
    }

    @FXML
    private void desenharRetangulo(ActionEvent event) {
        zeraFlags();
        tabelaFormas.getSelectionModel().clearSelection();
        formaSelecionada = Forma.RETANGULO;
        lblInstrucoes.setText("Clique em DOIS PONTOS no CANVAS BRANCO para formar um retângulo.");
    }

    @FXML
    private void desenharTriangulo(ActionEvent event) {
        zeraFlags();
        tabelaFormas.getSelectionModel().clearSelection();
        formaSelecionada = Forma.TRIANGULO;
        lblInstrucoes.setText("Clique em TRÊS PONTOS no CANVAS BRANCO para formar um triângulo.");
    }

    @FXML
    private void desenharCirculo(ActionEvent event) {
        zeraFlags();
        tabelaFormas.getSelectionModel().clearSelection();
        formaSelecionada = Forma.CIRCULO;
        lblInstrucoes.setText("Clique em DOIS PONTOS no CANVAS BRANCO para formar um círculo.");
    }

    @FXML
    private void rotacionar(ActionEvent event) {
        transformacaoSelecionada = Transformacao.ROTACAO;
        if(tabelaFormas.getSelectionModel().getSelectedItem() == null){
            if(figurasDesenhadas.isEmpty()){
                lblInstrucoes.setText("Primeiro desenhe uma FIGURA selecionando os botões à esquerda.");
            }
            lblInstrucoes.setText("Primeiro selecione na tabela a figura que deseja rotacionar e clique aqui novamente.");
        } else {
            valor3.setVisible(true);
            lblInstrucoes.setText("Digite as coordenadas do ponto de referência para rotacionar e o ângulo e clique em TRANFORMAR!");
        }
    }

    @FXML
    private void mudarEscala(ActionEvent event) {
        transformacaoSelecionada = Transformacao.MUDANCA_ESCALA;
        if(tabelaFormas.getSelectionModel().getSelectedItem() == null){
            if(figurasDesenhadas.isEmpty()){
                lblInstrucoes.setText("Primeiro desenhe uma FIGURA selecionando os botões à esquerda.");
            }
            lblInstrucoes.setText("Primeiro selecione na tabela a figura que deseja mudar a escala e clique aqui novamente.");
        } else {
            lblInstrucoes.setText("Digite o fator de escala X e Y nos campos à esquerda e clique em TRANFORMAR!");
        }
    }

    @FXML
    private void transladar(ActionEvent event) {
        transformacaoSelecionada = Transformacao.TRANSLACAO;
        if(tabelaFormas.getSelectionModel().getSelectedItem() == null){
            if(figurasDesenhadas.isEmpty()){
                lblInstrucoes.setText("Primeiro desenhe uma FIGURA selecionando os botões à esquerda.");
            }
            lblInstrucoes.setText("Primeiro selecione na tabela a figura que deseja transladar e clique aqui novamente.");
        } else {
            lblInstrucoes.setText("Digite o deslocamento X e Y nos campos à esquerda e clique em TRANFORMAR!");
        }
    }

    @FXML
    private void zoomExtend(ActionEvent event) {
        transformacaoSelecionada = Transformacao.ZOOM_EXTEND;
        lblInstrucoes.setText("Infelizmente esta opção ainda não foi implementada!");
    }

    @FXML
    private void capturarPonto(MouseEvent coord) {
        if(isPonto1){
            isPonto1 = Boolean.FALSE;
            isPonto2 = Boolean.TRUE;
            figGeo = new FiguraGeometrica();
            figGeo.setForma(formaSelecionada);
            figGeo.setCor(Color.BLACK);
            ponto1X = coord.getX();
            ponto1Y = coord.getY();
        } else if(isPonto2){
            isPonto2 = Boolean.FALSE;
            ponto2X = coord.getX();
            ponto2Y = coord.getY();
            if(!formaSelecionada.equals(Forma.TRIANGULO)){
                if(formaSelecionada.equals(Forma.LINHA)){
                    figGeo.setPonto1(new Ponto(ponto1X, ponto1Y));
                    figGeo.setPonto2(new Ponto(ponto2X, ponto2Y));
                } else if(ponto2X < ponto1X){
                    if(ponto2Y < ponto2X){
                        figGeo.setPonto1(new Ponto(ponto2X, ponto2Y)); 
                        figGeo.setPonto2(new Ponto(ponto1X, ponto1Y));
                    } else {
                        figGeo.setPonto1(new Ponto(ponto2X, ponto1Y)); 
                        figGeo.setPonto2(new Ponto(ponto1X, ponto2Y));
                    }
                } else if(ponto2Y < ponto1Y){
                    figGeo.setPonto1(new Ponto(ponto1X, ponto2Y));
                    figGeo.setPonto2(new Ponto(ponto2X, ponto1Y));
                } else {
                    figGeo.setPonto1(new Ponto(ponto1X, ponto1Y));
                    figGeo.setPonto2(new Ponto(ponto2X, ponto2Y));
                }
                desenhar();
                zeraFlags();
            }
        } else {
            figGeo.setPonto1(new Ponto(ponto1X, ponto1Y));
            figGeo.setPonto2(new Ponto(ponto2X, ponto2Y));
            figGeo.setPonto3(new Ponto(coord.getX(), coord.getY()));
            desenhar();
            zeraFlags();
        }
    }

    @FXML
    private void limparTudo(ActionEvent event) {
        formaSelecionada = null; 
        transformacaoSelecionada = null;
        figGeo = null;
        figSelecionada = null;
        figurasDesenhadas.removeAll(figurasDesenhadas);
        contexto.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
    
    private void desenhar(){
        if(figSelecionada != null){
            figSelecionada.setCor(Color.BLACK);          
            contexto.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            redesenhar();
        }
        contexto.setStroke(Color.BLACK);
        switch (formaSelecionada) {
            case LINHA:
                figurasDesenhadas.add(figGeo);
                contexto.strokeLine(figGeo.getPonto1().getX(), figGeo.getPonto1().getY(), figGeo.getPonto2().getX(), figGeo.getPonto2().getY());
                break;
            case RETANGULO:
                figurasDesenhadas.add(figGeo);               
                contexto.strokeRect(figGeo.getPonto1().getX(), figGeo.getPonto1().getY(), figGeo.getPonto2().getX()-figGeo.getPonto1().getX(), figGeo.getPonto2().getY()-figGeo.getPonto1().getY());
                break;
            case TRIANGULO:
                figurasDesenhadas.add(figGeo);
                double[] pontosX = {figGeo.getPonto1().getX(), figGeo.getPonto2().getX(), figGeo.getPonto3().getX()};
                double[] pontosY = {figGeo.getPonto1().getY(), figGeo.getPonto2().getY(), figGeo.getPonto3().getY()};
                contexto.strokePolygon(pontosX, pontosY, 3);
                break;
            case CIRCULO:
                figurasDesenhadas.add(figGeo);
                contexto.strokeOval(figGeo.getPonto1().getX(), figGeo.getPonto1().getY(), figGeo.getPonto2().getX()-figGeo.getPonto1().getX(), figGeo.getPonto2().getY()-figGeo.getPonto1().getY());
                break;
            default:
                break;
        }
    }
    
    private void redesenhar(){
        for(FiguraGeometrica f: figurasDesenhadas){
            switch (f.getForma()) {
                case LINHA:
                    contexto.setStroke(f.getCor());
                    contexto.strokeLine(f.getPonto1().getX(), f.getPonto1().getY(), f.getPonto2().getX(), f.getPonto2().getY());
                    break;
                case RETANGULO:
                    contexto.setStroke(f.getCor());
                    contexto.strokeRect(f.getPonto1().getX(), f.getPonto1().getY(), f.getPonto2().getX()-f.getPonto1().getX(), f.getPonto2().getY()-f.getPonto1().getY());
                    break;
                case TRIANGULO:
                    contexto.setStroke(f.getCor());
                    double[] pontosX = {f.getPonto1().getX(), f.getPonto2().getX(), f.getPonto3().getX()};
                    double[] pontosY = {f.getPonto1().getY(), f.getPonto2().getY(), f.getPonto3().getY()};
                    contexto.strokePolygon(pontosX, pontosY, 3);
                    break;
                case CIRCULO:
                    contexto.setStroke(f.getCor());
                    contexto.strokeOval(f.getPonto1().getX(), f.getPonto1().getY(), f.getPonto2().getX()-f.getPonto1().getX(), f.getPonto2().getY()-f.getPonto1().getY());
                    break;
                default:
                    break;
            }
        }
    }

    @FXML
    private void transformar(ActionEvent event){
        if(transformacaoSelecionada.equals(Transformacao.TRANSLACAO)){       
            translacao(Double.valueOf(valor1.getText()), Double.valueOf(valor2.getText()));
        } else if (transformacaoSelecionada.equals(Transformacao.MUDANCA_ESCALA)){
            //transladar pra origem
            Double pontoOrigemX = figSelecionada.getPonto1().getX();
            Double pontoOrigemY = figSelecionada.getPonto1().getY();
            translacao(-pontoOrigemX, -pontoOrigemY);
            mudarEscala(Double.valueOf(valor1.getText()), Double.valueOf(valor2.getText()));
            translacao(pontoOrigemX, pontoOrigemY);
        } else if (transformacaoSelecionada.equals(Transformacao.ROTACAO)){
            //Double pontoOrigemX = figSelecionada.getPonto1().getX();
            //Double pontoOrigemY = figSelecionada.getPonto1().getY();
            //translacao(-pontoOrigemX, -pontoOrigemY);
            rotacao(Double.valueOf(valor1.getText()));
            //translacao(pontoOrigemX, pontoOrigemY);
        }
        contexto.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        redesenhar();
    }

    public void translacao(Double x, Double y){
        figSelecionada.getPonto1().setX(x + figSelecionada.getPonto1().getX());
        figSelecionada.getPonto1().setY(y + figSelecionada.getPonto1().getY());
        figSelecionada.getPonto2().setX(x + figSelecionada.getPonto2().getX());
        figSelecionada.getPonto2().setY(y + figSelecionada.getPonto2().getY());
        if (figSelecionada.getForma().equals(Forma.TRIANGULO)){
            figSelecionada.getPonto3().setX(x + figSelecionada.getPonto3().getX());
            figSelecionada.getPonto3().setY(y + figSelecionada.getPonto3().getY());
        }
    }
    
    public void mudarEscala(Double sx, Double sy){
        figSelecionada.getPonto1().setX(sx*figSelecionada.getPonto1().getX());
        figSelecionada.getPonto1().setY(sy*figSelecionada.getPonto1().getY());
        figSelecionada.getPonto2().setX(sx*figSelecionada.getPonto2().getX());
        figSelecionada.getPonto2().setY(sy*figSelecionada.getPonto2().getY());
        if (figSelecionada.getForma().equals(Forma.TRIANGULO)){
            figSelecionada.getPonto3().setX(sx*figSelecionada.getPonto3().getX());
            figSelecionada.getPonto3().setY(sy*figSelecionada.getPonto3().getY());
        }
    }
    
    public void rotacao(Double angulo){
        Double ang = Math.PI*angulo / 180;
        figSelecionada.getPonto1().setX((Math.cos(ang) * figSelecionada.getPonto1().getX()) + (-Math.sin(ang) * figSelecionada.getPonto1().getY()) + ((Double.valueOf(valor3.getText())*Math.sin(angulo) - Double.valueOf(valor2.getText())*Math.cos(angulo) + Double.valueOf(valor2.getText()))));
        figSelecionada.getPonto1().setY((Math.sin(ang) * figSelecionada.getPonto1().getX()) + (Math.cos(ang) * figSelecionada.getPonto1().getY()) + ((-Double.valueOf(valor2.getText())*Math.sin(angulo) - Double.valueOf(valor3.getText())*Math.cos(angulo) + Double.valueOf(valor3.getText()))));
        figSelecionada.getPonto2().setX((Math.cos(ang) * figSelecionada.getPonto2().getX()) + (-Math.sin(ang) * figSelecionada.getPonto2().getY()) + ((Double.valueOf(valor3.getText())*Math.sin(angulo) - Double.valueOf(valor2.getText())*Math.cos(angulo) + Double.valueOf(valor2.getText()))));
        figSelecionada.getPonto2().setY((Math.sin(ang) * figSelecionada.getPonto2().getX()) + (Math.cos(ang) * figSelecionada.getPonto2().getY()) + ((-Double.valueOf(valor2.getText())*Math.sin(angulo) - Double.valueOf(valor3.getText())*Math.cos(angulo) + Double.valueOf(valor3.getText()))));
        if(figSelecionada.getForma().equals(Forma.TRIANGULO)){
            figSelecionada.getPonto3().setX((Math.cos(ang) * figSelecionada.getPonto3().getX()) + (-Math.sin(ang) * figSelecionada.getPonto3().getY()));
            figSelecionada.getPonto3().setY((Math.sin(ang) * figSelecionada.getPonto3().getX()) + (Math.cos(ang) * figSelecionada.getPonto3().getY()));
        }
    }
    
    private void zeraFlags(){
        isPonto1 = Boolean.TRUE;
        isPonto2 = Boolean.FALSE;
        valor1.clear();
        valor2.clear();
        valor3.clear();
    }
    
    private void inicializaImagens(){
        imgCirculo.setImage(new Image(getClass().getResource("imagens/circulo.png").toExternalForm()));
        imgTriangulo.setImage(new Image(getClass().getResource("imagens/triangulo.png").toExternalForm()));
        imgRetangulo.setImage(new Image(getClass().getResource("imagens/retangulo.png").toExternalForm()));
        imgLinha.setImage(new Image(getClass().getResource("imagens/linha.png").toExternalForm()));
        imgRotacao.setImage(new Image(getClass().getResource("imagens/rotacao.png").toExternalForm()));
        imgTranslacao.setImage(new Image(getClass().getResource("imagens/translacao.png").toExternalForm()));
        imgEscala.setImage(new Image(getClass().getResource("imagens/escala.png").toExternalForm()));
    }
}
