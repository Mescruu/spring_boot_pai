package com.albert.pai.controllers;

import com.albert.pai.dao.ratingDao;
import com.albert.pai.dao.recipeDao;
import com.albert.pai.dao.typesDao;
import com.albert.pai.dao.userDao;

import com.albert.pai.entity.Ratings;
import com.albert.pai.entity.Recipes;
import com.albert.pai.entity.User;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

import static java.lang.Integer.parseInt;
import static java.util.Objects.isNull;


@Controller
public class MainController {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private userDao dao;

    @Autowired
    private recipeDao rDao;

    @Autowired
    private typesDao tDao;

    @Autowired
    private ratingDao ratDao;

    @GetMapping("/")
    public String indexPage(Model m) {

        m.addAttribute("types", tDao.findAll());

        //zwrócenie nazwy widoku logowania - index.html
        return "index";
    }


    @GetMapping("/login")
    public String loginPage(Model m) {
        m.addAttribute("types", tDao.findAll());

        //zwrócenie nazwy widoku logowania - login.html
        return "login";
    }
    //wyswietlenie strony register
    @GetMapping("/register")
    public String registerPage(Model m) {
        //dodanie do modelu nowego użytkownika
        m.addAttribute("types", tDao.findAll());

        m.addAttribute("user", new User());
        //zwrócenie nazwy widoku rejestracji - register.html
        return "register";
    }

    //przetworzenie danych z formularza
    @PostMapping("/register")//Obsługa błędnych danych z formularza - wstrzyknięcie do metody kontrolera obiektu BindingResult: oraz model
    public String registerPagePOST(@ModelAttribute(value = "user") @Valid User user, BindingResult binding, Model m) {

        m.addAttribute("types", tDao.findAll());


        if (binding.hasErrors()) {
            return "register";
        }
         User template_user = dao.findByLogin(user.getLogin());

        //sprawdzenie czy user o podanym loginie istnieje
        if(!isNull(template_user)){
            String login_error = new String();
            login_error = "Try another login. "+user.getLogin()+ " is taken";
            m.addAttribute("same_login_error", login_error);

            return "register";

        }else{

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            dao.save(user);
            //przekierowanie do adresu url: /login
            return "redirect:/login";
        }
    }

    @GetMapping("/profile")
    public String profilePage(Model m, Principal principal) {

        m.addAttribute("types", tDao.findAll());
        m.addAttribute("odd", false);

        //dodanie do modelu obiektu user - aktualnie zalogowanego użytkownika:
        m.addAttribute("user", dao.findByLogin(principal.getName()));

        //zwrócenie nazwy widoku profilu użytkownika - profile.html
        return "profile";

    }

    @GetMapping("/types")
    public String Types(Model m){        //dodanie do modelu obiektu user - aktualnie zalogowanego użytkownika:
        m.addAttribute("types", tDao.findAll());
        System.out.println(tDao.findAll());

        return "selecttypes";
    }

    @GetMapping("/types/{type}")
    public String Types(@PathVariable(value="type")  String type, Model m){        //dodanie do modelu obiektu user - aktualnie zalogowanego użytkownika:

        m.addAttribute("recipes", rDao.findByType(type));
        System.out.println(rDao.findByType(type));

        //zwrócenie nazwy widoku profilu użytkownika - profile.html
        return "showrecipes";
    }

    @GetMapping("/recipes/own")
    public String UserRecipes(Principal principal, Model m){        //dodanie do modelu obiektu user - aktualnie zalogowanego użytkownika:

        m.addAttribute("recipes", rDao.findByUserid(dao.findByLogin(principal.getName()).getUserid()));
        //zwrócenie nazwy widoku profilu użytkownika - profile.html
        return "showrecipes";
    }



    @GetMapping("/recipe/{id}") //pobranie id receptury z linku, pobranie modelu i pobranie uzytkownika zalogowanego aktualnie
    public String Types(@PathVariable(value="id")  Integer id, Model m, Principal principal) {

        //recipe
        m.addAttribute("recipe", rDao.findByIdrecipe(id));

        //types
        m.addAttribute("types", tDao.findAll());

        //list of ingredients
        List<String> IngredientsList = Arrays.asList(rDao.findByIdrecipe(id).getIngredients().split(",", -1));
        m.addAttribute("ingredients", IngredientsList);

        /*rating*/
        //user rating

        Integer user_rating;

        if (ratDao.findByUseridAndRecipeid(dao.findByLogin(principal.getName()).getUserid(), id) == null) {
            user_rating = 0;
        } else {
            user_rating = ratDao.findByUseridAndRecipeid(dao.findByLogin(principal.getName()).getUserid(), id).getRating();
        }

        m.addAttribute("user_rating", user_rating);

        Float users_ratings = ratDao.FindAverageRatingOfRecipe(id);

        if (users_ratings == null) {
            users_ratings = 0f;
        }

        m.addAttribute("users_ratings", Math.round(users_ratings)); //wysylam zaokraglona wartosc

        m.addAttribute("userid", dao.findByLogin(principal.getName()).getUserid());

        if (rDao.findByIdrecipe(id).getUserid() != null) { //sprawdzenie czy ten przepis ma wlasciciela
            if (rDao.findByIdrecipe(id).getUserid().equals(dao.findByLogin(principal.getName()).getUserid())) {
                //zwrócenie widoku przepisu wlasciciela
                return "show_ownrecipe";
            } else {
                //zwrócenie widoku przepisu
                return "showrecipe";
            }
        } else {
            return "showrecipe";
        }
    }

    //pobranie requesta
    @RequestMapping(value="/set_rating", method=RequestMethod.POST)
    public ModelAndView Recipe(@RequestParam("id") Integer id, @RequestParam("rate") Integer rate, @RequestParam("userid") Integer userid){

        System.out.println(id + " -- " + rate  + " -- " + userid);

        //jezeli uzytkownik nie ocenial
        if(ratDao.findByUseridAndRecipeid(userid,id) == null){

            //utworzenie nowego obiektu ratings
            Ratings ratings = new Ratings(rate, userid, id);
            ratDao.save(ratings);
        }else{ //jezeli ocenil

            Ratings ratings = ratDao.findByUseridAndRecipeid(userid,id);
               ratings.setRating(rate);
               ratings.setUserid(userid);
               ratings.setRecipeid(id);

            ratDao.save(ratings);
        }

        return new ModelAndView("redirect:/recipe/"+id);
    }

    //dodawanie przepisu
    @GetMapping("/recipes/add") //definicja metody, która zwróci do widoku users.html listę użytkowników z bd
    public String addRecipes(Model m, Principal principal) {
        System.out.println("user id" +dao.findByLogin(principal.getName()).getUserid());

        m.addAttribute("recipes", new Recipes(dao.findByLogin(principal.getName()).getUserid())); //dodanie nowego obiektu przepisu
        m.addAttribute("types", tDao.findAll()); //dodanie typow przepisow

        return "addrecipe";
    }

    //przetworzenie danych z formularza
    //dodanie przepisu
    @PostMapping("/recipes/add")//Obsługa błędnych danych z formularza - wstrzyknięcie do metody kontrolera obiektu BindingResult: oraz model
    public String addRecipes(@ModelAttribute(value = "recipes") @Valid Recipes recipes, BindingResult binding, Model m, Principal principal) {

            recipes.setDifficulty("Hard");
            recipes.setIdrecipe(rDao.findLastId()+1);
            //set user id
            recipes.setUserid(dao.findByLogin(principal.getName()).getUserid());


        System.out.println("difficulty" + recipes.getName());
        System.out.println("difficulty" + recipes.getDifficulty());
        System.out.println("difficulty" + recipes.getIngredients());
        System.out.println("difficulty" + recipes.getNotes());
        System.out.println("difficulty" + recipes.getPreptime());
        System.out.println("difficulty" + recipes.getIngredients());

        m.addAttribute("types", tDao.findAll());


        System.out.println(binding.toString());

        if (binding.hasErrors()) {
            return "addrecipe";
        }

            rDao.save(recipes);

        m.addAttribute("recipes", rDao.findByUserid(dao.findByLogin(principal.getName()).getUserid()));

        //przekierowanie do adresu url: /login
        return "showrecipes";
    }



    @GetMapping("/editrecipe/{id}") //pobranie id receptury z linku, pobranie modelu i pobranie uzytkownika zalogowanego aktualnie
    public String EditRecipe(@PathVariable(value="id")  Integer id, Model m, Principal principal){


        //list of ingredients
        List<String> IngredientsList = Arrays.asList(rDao.findByIdrecipe(id).getIngredients().split(",", -1));
        m.addAttribute("ingredients",IngredientsList);

        /*rating*/
        Float users_ratings = ratDao.FindAverageRatingOfRecipe(id);

        if(users_ratings == null){
            users_ratings = 0f;
        }

        //recipe
        m.addAttribute("recipe", rDao.findByIdrecipe(id));

        //types
        m.addAttribute("types", tDao.findAll());

        m.addAttribute("users_ratings", Math.round(users_ratings)); //wysylam zaokraglona wartosc

        m.addAttribute("userid", dao.findByLogin(principal.getName()).getUserid());

        //add to Model new recipe object with user id
        m.addAttribute("new_recipe", new Recipes(dao.findByLogin(principal.getName()).getUserid()));

        //zwrócenie nazwy widoku
        return "edit_own_recipe";

    }

    //przetworzenie danych z formularza
    //dodanie przepisu
    @PostMapping("/editrecipe/{id}")//Obsługa błędnych danych z formularza - wstrzyknięcie do metody kontrolera obiektu BindingResult: oraz model
    public String editRecipes(@ModelAttribute(value = "new_recipe") @Valid Recipes new_recipe, @PathVariable(value="id")  Integer id, BindingResult binding, Model m, Principal principal) {

        Recipes old_recipe = rDao.findByIdrecipe(id);

        if(new_recipe.getName()!="")
            old_recipe.setName(new_recipe.getName());

        if(new_recipe.getNotes()!="")
            old_recipe.setNotes(new_recipe.getNotes());

        if(new_recipe.getPhoto()!="")
            old_recipe.setPhoto(new_recipe.getPhoto());

        if(new_recipe.getPreptime()!="")
            old_recipe.setPreptime(new_recipe.getPreptime());

        if(new_recipe.getType()!="")
            old_recipe.setType(new_recipe.getType());

        if(new_recipe.getIngredients()!="")
            old_recipe.setIngredients(new_recipe.getIngredients());

        if (binding.hasErrors()) {
            //recipe
            m.addAttribute("recipe", rDao.findByIdrecipe(id));

            return "edit_own_recipe";
        }
    //zapisanie zmian
        rDao.save(old_recipe);

        //list of ingredients
        List<String> IngredientsList = Arrays.asList(rDao.findByIdrecipe(id).getIngredients().split(",", -1));
        m.addAttribute("ingredients", IngredientsList);


        //recipe
        m.addAttribute("recipe", rDao.findByIdrecipe(id));

        return "show_ownrecipe";
    }

    @PostMapping("/removerecipe/{id}")
    public String removeRecipe(Principal principal, @PathVariable(value="id")  Integer id) {

        Recipes recipes = rDao.findByIdrecipe(id);
        if(recipes.getUserid() != null){ //sprawdz czy ktokolwiek jest wlascicielem
            if(recipes.getUserid() == dao.findByLogin(principal.getName()).getUserid()){
                //jezeli wlascicielem jest uzytkownik

                //usuniecie usera z bazy danych
                rDao.delete(recipes);

            }
        }

        //przekierowanie do adresu url: /profile
        return "redirect:/recipes/own";
    }

    @PostMapping("/edituser")//@ModelAttribute(value = "user") User new_user - pobranie z formularza i utworzenie nowego obiektu usera z danymi z formularza
    public String edituserPagePOST(@ModelAttribute(value = "user") @Valid User user, BindingResult binding, Principal principal) {

        if (binding.hasErrors()) {
            //pobranie danych uzytkownika
            user.setName(dao.findByLogin(principal.getName()).getName());
            user.setSurname(dao.findByLogin(principal.getName()).getSurname());
            user.setPassword(dao.findByLogin(principal.getName()).getPassword());

            return "profile";
        }

        //https://www.baeldung.com/spring-data-crud-repository-save
        //edycja działa na podobnej zasadzie co tworenie nowego rekordu

        //pobranie aktualnego użytkownika
        User user_old = dao.findByLogin(user.getLogin());

        //ustawienie nowych danych
        user_old.setName(user.getName());
        user_old.setSurname(user.getSurname());
        user_old.setPassword(passwordEncoder.encode(user.getPassword()));
        dao.save(user_old);

        //przekierowanie do adresu url: /profile
        return "redirect:/profile";
    }

    @PostMapping("/removeuser")
    public String removeuserPagePOST(Principal principal) {

        //pobranie aktualnego użytkownika
        User user = dao.findByLogin(principal.getName());

        //usuniecie usera z bazy danych
        dao.delete(user);

        //przekierowanie do adresu url: /logout
        return "redirect:/logout";
    }

}
