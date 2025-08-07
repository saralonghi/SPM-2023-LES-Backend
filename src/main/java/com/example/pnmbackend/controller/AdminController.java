package com.example.pnmbackend.controller;


import com.example.pnmbackend.model.entities.Admin;
import com.example.pnmbackend.model.entities.NewsLetter;
import com.example.pnmbackend.model.entities.News;
import com.example.pnmbackend.model.entities.Producer;
import com.example.pnmbackend.request.LoginRequest;
import com.example.pnmbackend.request.SignupRequest;
import com.example.pnmbackend.response.JwtAuthenticationResponse;
import com.example.pnmbackend.service.AdminService;
import com.example.pnmbackend.service.NewsService;
import com.example.pnmbackend.service.ProducerService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;


@RestController
@RequestMapping(value = "api/admin")
public class AdminController {

    private final String VALID_REGISTRATION = "Admin registered successfully";
    private final String VALID_REJECTED = "Producer rejected successfully";
    private final String AUTHENTICATION_FAILURE="Authentication failed";
    private final String VALID_NEWS = "News created successfully";

    private final String NEWS_DELETED = "News deleted successfully";

    private final String NEWSLETTER_APPROVED = "Newsletter approved successfully";

    private final String NEWSLETTER_RESTORED = "Newsletter restored successfully";

    private final String NEWSLETTER_DELETED = "Newsletter deleted successfully";

    private final String NEWSLETTER_REJECTED = "Validation failed";

    private final String NEWSLETTER_SENT = "Newsletter sent successfully";

    @Autowired
    private AdminService adminService;

    @Autowired
    ProducerService producerService;

    @Autowired
    private NewsService newsService;

    private JSONObject jsonObject;


    @PostMapping("/register")
    public ResponseEntity<String> registerAdmin(@RequestBody SignupRequest signupRequest) {
        try {
            jsonObject = new JSONObject();
            adminService.createAdmin(signupRequest);
            jsonObject.put("result",VALID_REGISTRATION);
            return ResponseEntity.ok(jsonObject.toString());
        } catch (IllegalArgumentException e) {
            String errorMessage = e.getMessage().substring(e.getMessage().indexOf(":") + 2);
            jsonObject.put("result",errorMessage);
            return ResponseEntity.ok(jsonObject.toString());
        }
    }

    @PostMapping(value ="/login")
    public ResponseEntity<JwtAuthenticationResponse> getLogin(@RequestBody LoginRequest loginRequest){
        try{
            return ResponseEntity.ok(adminService.signIn(loginRequest));
        }catch(Exception e){
            return ResponseEntity.badRequest().body(JwtAuthenticationResponse.builder().error(AUTHENTICATION_FAILURE).build());
        }
    }


// Producer -------------------------------------------------------------------------------------------
    @GetMapping("/dashboard")
    public ResponseEntity<Admin> dashboardProducer() {
    Admin admin = adminService.getLoggedAdmin(SecurityContextHolder.getContext().getAuthentication().getName());
    if(admin==null)  return ResponseEntity.badRequest().body(null);
    return ResponseEntity.ok(admin);
}
    @GetMapping("/producer/{id}")
    public ResponseEntity<Producer> getProducer(@PathVariable String id) {
        try {
            return ResponseEntity.ok(adminService.retrieveProducer(id));
        }
        catch (Exception e) {
            String message = e.getMessage();
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/approveProducer")
    public ResponseEntity<String> approveProducer(@RequestBody String id) {
        jsonObject = new JSONObject();
        adminService.activateProducer(id);
        jsonObject.put("result",VALID_REGISTRATION);
        return ResponseEntity.ok(jsonObject.toString());

    }
    @PostMapping("/rejectProducer")
    public ResponseEntity<String> rejectProducer(@RequestBody String id) {
        jsonObject = new JSONObject();
        adminService.rejectedProducer(id);
        jsonObject.put("result",VALID_REJECTED );
        return ResponseEntity.ok(jsonObject.toString());
    }
    @PostMapping("/restoreProducer")
    public ResponseEntity<String> restoreProducer(@RequestBody String id) {
        jsonObject = new JSONObject();
        adminService.restoreProducer(id);
        jsonObject.put("result","Producer restored");
        return ResponseEntity.ok(jsonObject.toString());
    }
    @DeleteMapping("/producerDelete")
    public ResponseEntity<String> deleteProducer(@RequestBody String id) {
        adminService.deleteProducer(id);
        return ResponseEntity.ok("Account deleted successfully");
    }
    @GetMapping(value="/allProducer/notActive")
    public ResponseEntity<List<Producer>> allProducersNotActive(){
        List<Producer> producers = adminService.retrieveAllProducersPending();
        return ResponseEntity.ok(producers);
    }
    @GetMapping(value="/allProducer/active")
    public ResponseEntity<List<Producer>> allProducersActive(){
        List<Producer> producers = adminService.retrieveAllProducersActive();
        return ResponseEntity.ok(producers);
    }


// News ----------------------------------------------------------------------------------------------
    @PostMapping("/createNews")
    public ResponseEntity<String> createNews(@RequestPart("news") News news,
                                                   @RequestPart("image") MultipartFile[] image){
        try {
            jsonObject = new JSONObject();
            newsService.createNews(news, image);
            jsonObject.put("result",VALID_NEWS);
            return ResponseEntity.ok(jsonObject.toString());
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/deleteNews/{id}")
    public ResponseEntity<String> deleteNews(@PathVariable("id") String id) {
        try {
            jsonObject = new JSONObject();
            newsService.deleteNews(id);
            jsonObject.put("result",NEWS_DELETED);
            return ResponseEntity.ok(jsonObject.toString());
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value="/allNews")
    public ResponseEntity<List<News>> allNews() {
        List<News> news = newsService.retrieveAllNews();
        return ResponseEntity.ok(news);
    }

    @GetMapping("/news/{id}")
    public ResponseEntity<News> getNews(@PathVariable String id) {
        try {
            return ResponseEntity.ok(newsService.retrieveNews(id));
        }
        catch (Exception e) {
            String message = e.getMessage();
            return ResponseEntity.badRequest().body(null);
        }
    }

// Newsletter -----------------------------------------------------------------------------------------

    @GetMapping(value="/allNewsletter/notActive")
    public ResponseEntity<List<NewsLetter>> allNewsletterNotActive(){
    List<NewsLetter> newsletter = adminService.retrieveAllNewsletterPending();
    return ResponseEntity.ok(newsletter);
    }

    @GetMapping(value="/allNewsletter/active")
    public ResponseEntity<List<NewsLetter>> allNewsletterActive(){
        List<NewsLetter> newsletter = adminService.retrieveAllNewsletterActive();
        return ResponseEntity.ok(newsletter);
    }
    @GetMapping("/newsletter/{id}")
    public ResponseEntity<NewsLetter> getNewsletter(@PathVariable String id) {
        try {
            return ResponseEntity.ok(adminService.retrieveNewsletter(id));
        }
        catch (Exception e) {
            String message = e.getMessage();
            return ResponseEntity.badRequest().body(null);
        }
    }
    @PostMapping("/approveNewsletter")
    public ResponseEntity<String> approveNewsletter(@RequestBody String id){
        jsonObject = new JSONObject();
        adminService.approveNewsletter(id);
        jsonObject.put("result",NEWSLETTER_APPROVED);
        return ResponseEntity.ok(jsonObject.toString());
    }

    @PostMapping("/sendNewsletter")
    public ResponseEntity<String> sendNewsletter(@RequestBody String id){
        jsonObject = new JSONObject();
        adminService.sendNewsletter(id);
        jsonObject.put("result",NEWSLETTER_SENT);
        return ResponseEntity.ok(jsonObject.toString());
    }
    @PostMapping("/rejectNewsletter")
    public ResponseEntity<String> rejectNewsletter(@RequestBody String id) {
        jsonObject = new JSONObject();
        adminService.rejectNewsletter(id);
        jsonObject.put("result",NEWSLETTER_REJECTED );
        return ResponseEntity.ok(jsonObject.toString());
    }
    @PostMapping("/restoreNewsletter")
    public ResponseEntity<String> restoreNewsletter(@RequestBody String id) {
        jsonObject = new JSONObject();
        adminService.restoreNewsletter(id);
        jsonObject.put("result",NEWSLETTER_RESTORED);
        return ResponseEntity.ok(jsonObject.toString());
    }
    @DeleteMapping("/deleteNewsletter/{id}")
    public ResponseEntity<String> deleteNewsLetter(@PathVariable("id") String id) {
        try {
            jsonObject = new JSONObject();
            adminService.deleteNewsletter(id);
            jsonObject.put("result",NEWSLETTER_DELETED);
            return ResponseEntity.ok(jsonObject.toString());
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
