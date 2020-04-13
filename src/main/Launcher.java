package main;
import com.google.gson.Gson;
import model.Proizvod;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static spark.Spark.*;
public class Launcher {
    public static void main(String[] args) {
        staticFileLocation("public");
        port(5000);
        HashMap<String,Object> polja = new HashMap<>();
        String path = "proizvodi.json";
        get("/",(request, response) -> {
            if(request.session().attribute("user")!=null){
                response.redirect("/admin");
                return null;
            }
            return new ModelAndView(null,"index.hbs");
        },new HandlebarsTemplateEngine());
        post("/proizvodi",(request, response) -> {
            return new Gson().toJson(Data.readFromJson(path));
        });
        post("/proizvod",(request, response) -> {
            return new Gson().toJson(Data.readFromJson(path).stream().filter(p->request.queryParams("id").equals(p.getId()+"")).toArray()[0]);
        });
        post("/popuniSelect",(request, response) -> {
            String param = request.queryParams("parametar");
            String val = request.queryParams("val");
            HashSet<String> set = new HashSet<>();
            ArrayList<Proizvod> proizvodi = Data.readFromJson(path);
            if(param.equals("marka")){
                for(int i = 0;i<proizvodi.size();i++){
                    if(proizvodi.get(i).getMarka().equals(val))
                    set.add(proizvodi.get(i).getModel());
                }
            }else{
                for(int i = 0;i<proizvodi.size();i++){
                    if(proizvodi.get(i).getGrupa().equals(val))
                        set.add(proizvodi.get(i).getTip());
                }
            }
            return new Gson().toJson(set.toArray());
        });
        get("/admin",(request, response) -> {
            if(request.session().attribute("user")==null) {
                response.redirect("/");
                return null;
            }
            polja.put("admin","admin");
            return new ModelAndView(polja,"index.hbs");
        },new HandlebarsTemplateEngine());
        post("/prijava",(request, response) -> {
            String name = request.queryParams("name");
            String password = request.queryParams("password");
            if(name.equals("admin")&&password.equals("admin")){
                request.session().attribute("user","admin");
                return new Gson().toJson("/admin");
            }
            return new Gson().toJson("/");
        });
        post("/odjava",(request, response) -> {
            request.session().removeAttribute("user");
            return new Gson().toJson("/");
        });
        post("/brisanje",(request, response) -> {
            ArrayList<Proizvod> proizvodi = Data.readFromJson(path);
            for(int i = 0;i<proizvodi.size();i++){
                if(request.queryParams("id").equals(proizvodi.get(i).getId()+"")){
                    proizvodi.remove(i);
                }
            }
            Data.writeToJSON(proizvodi,path);
            return new Gson().toJson("Proizvod uspesno obrisan");
        });
        post("/pretraga",(request, response) -> {
            float min = Float.parseFloat(request.queryParams("min"));
            float max = Float.parseFloat(request.queryParams("max"));
            ArrayList<Proizvod> proizvodi = Data.readFromJson(path);
            Proizvod.Filter(proizvodi,"grupa",request.queryParams("grupa"));
            Proizvod.Filter(proizvodi,"tip",request.queryParams("tip"));
            Proizvod.Filter(proizvodi,"marka",request.queryParams("marka"));
            Proizvod.Filter(proizvodi,"model",request.queryParams("model"));
            Proizvod.FilterCena(proizvodi,min,max);
            return new Gson().toJson(proizvodi);
        });
        post("/izmena",(request, response) -> {
            ArrayList<Proizvod> proizvodi = Data.readFromJson(path);
            int id = Integer.parseInt(request.queryParams("id"));
            Proizvod.Izmena(proizvodi,id, request.queryParams("grupa"),request.queryParams("tip"),request.queryParams("marka"),request.queryParams("model"),request.queryParams("opis"),Float.parseFloat(request.queryParams("cena")));
            Data.writeToJSON(proizvodi,path);
            return new Gson().toJson("Uspenso izmenjeno");
        });
        post("/dodaj",(request, response) -> {
            ArrayList<Proizvod> proizvodi = Data.readFromJson(path);
            int id = proizvodi.get(proizvodi.size()-1).getId()+1;
            proizvodi.add(new Proizvod(id,request.queryParams("grupa"),request.queryParams("tip"),request.queryParams("marka"),request.queryParams("model"),request.queryParams("opis"),Float.parseFloat(request.queryParams("cena"))));
            Data.writeToJSON(proizvodi,path);
            return new Gson().toJson("Uspesno dodat proizvod");
        });
        post("/pretraziSve",(request, response) -> {
            ArrayList<Proizvod> proizvodi = Data.readFromJson(path);
            String tekst = request.queryParams("tekst");
            Proizvod.Pretraga(proizvodi,tekst);
            return new Gson().toJson(proizvodi);
        });
        get("/izmenaSelect",(request, response) -> {
            polja.put("proizvod",Data.readFromJson(path));
            return new ModelAndView(polja,"izmena.hbs");
        },new HandlebarsTemplateEngine());
    }
}
