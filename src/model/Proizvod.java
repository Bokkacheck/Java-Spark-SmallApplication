package model;

import java.util.ArrayList;

public class Proizvod {
    private int id;
    private String grupa;
    private String tip;
    private String marka;
    private String model;
    private String opis;
    private float cena;

    public Proizvod(int id, String grupa, String tip, String marka, String model, String opis, float cena) {
        this.id = id;
        this.grupa = grupa;
        this.tip = tip;
        this.marka = marka;
        this.model = model;
        this.opis = opis;
        this.cena = cena;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGrupa() {
        return grupa;
    }

    public void setGrupa(String grupa) {
        this.grupa = grupa;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getMarka() {
        return marka;
    }

    public void setMarka(String marka) {
        this.marka = marka;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public float getCena() {
        return cena;
    }

    public void setCena(float cena) {
        this.cena = cena;
    }

    public static ArrayList<Proizvod> Filter(ArrayList<Proizvod> proizvodi,String svojstvo,String vrednost){
        if(!vrednost.equals("null")){
            for(int i = 0;i<proizvodi.size();i++){
                if(!proizvodi.get(i).VratiSvojstvo(svojstvo).equals(vrednost)){
                    proizvodi.remove(i--);
                }
            }
        }
        return proizvodi;
    }
    public static ArrayList<Proizvod> FilterCena(ArrayList<Proizvod> proizvodi,float min,float max){
        for(int i = 0;i<proizvodi.size();i++){
            if(proizvodi.get(i).cena<min || proizvodi.get(i).cena>max){
                proizvodi.remove(i--);
            }
        }
        return proizvodi;
    }
    public String VratiSvojstvo(String svojstvo){
        switch (svojstvo){
            case "grupa": return this.grupa;
            case "tip": return this.tip;
            case "marka": return this.marka;
            case "model": return this.model;
            case "opis": return this.opis;
            case "cena": return this.cena+"";
            default: return "";
        }
    }
    public static void Pretraga(ArrayList<Proizvod> proizvodi,String tekst){
        String [] svojstva = {"grupa","tip","marka","model","opis","cena"};
        for(int i = 0;i<proizvodi.size();i++){
            int j = 0;
            for(j = 0;j<svojstva.length;j++){
                if(proizvodi.get(i).VratiSvojstvo(svojstva[j]).toLowerCase().contains(tekst.toLowerCase())){
                    break;
                }
            }
            if(j==svojstva.length){
                proizvodi.remove(i--);
            }
        }
    }
    public static void Izmena(ArrayList<Proizvod> proizvodi,int id,String grupa,String tip,String marka,String model,String opis, float cena){
        for(int i = 0;i<proizvodi.size();i++){
            if(id==proizvodi.get(i).getId()){
                proizvodi.get(i).grupa = grupa;
                proizvodi.get(i).tip = tip;
                proizvodi.get(i).marka = marka;
                proizvodi.get(i).model = model;
                proizvodi.get(i).opis = opis;
                proizvodi.get(i).cena = cena;
                break;
            }
        }
    }
}
