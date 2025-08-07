package com.example.pnmbackend.controller;
import com.example.pnmbackend.model.entities.Image;
import com.example.pnmbackend.model.entities.NewsLetter;
import com.example.pnmbackend.model.entities.Producer;
import com.example.pnmbackend.model.entities.ProducerDetails;
import com.example.pnmbackend.request.ChangePasswordRequest;
import com.example.pnmbackend.request.LoginRequest;
import com.example.pnmbackend.request.SignupRequest;
import com.example.pnmbackend.request.UpdateProducerRequest;
import com.example.pnmbackend.response.JwtAuthenticationResponse;
import com.example.pnmbackend.service.*;
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
@RequestMapping(value = "api/producer")
public class ProducerController {

    private final String VALID_REGISTRATION = "Producer registered successfully";
    private final String VALID_NEWSLETTER = "Newsletter created successfully";
    private final String NEWSLETTER_DELETED = "Newsletter deleted successfully";
    private final String EMAIL_SENT="Email sent successfully";
    private final String PASSWORD_CHANGED="Password changed successfully";
    private final String TOKEN_VALIDATED="Token validated successfully";

    @Autowired
    private ProducerService producerService;

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private NewsLetterService newsLetterService;

    private JSONObject jsonObject;

//LOGIN AND REGISTRATION-------------------------------------------------------------------------------------------
    @PostMapping(value ="/login")
    public ResponseEntity<JwtAuthenticationResponse> getLogin(@RequestBody LoginRequest loginRequest){
        try{
            return ResponseEntity.ok(producerService.signIn(loginRequest));
        }catch(Exception e) {
            String errorMessage = e.getMessage();
            return ResponseEntity.badRequest().body(JwtAuthenticationResponse.builder().error(errorMessage).build());
        }
    }
    @PostMapping("/register")
    public ResponseEntity<String> registerProducer(@RequestBody SignupRequest registerRequest) {
        try {
            jsonObject = new JSONObject();
            producerService.registerProducer(registerRequest);
            jsonObject.put("result",VALID_REGISTRATION);
            return ResponseEntity.ok(jsonObject.toString());
        } catch (IllegalArgumentException e) {
            String errorMessage = e.getMessage().substring(e.getMessage().indexOf(":") + 2);
            jsonObject.put("error",errorMessage);
            return ResponseEntity.badRequest().body(jsonObject.toString());
        }
    }
    @GetMapping("/dashboard")
    public ResponseEntity<Producer> dashboardProducer() {
        Producer producer = producerService.getLoggedProducer(SecurityContextHolder.getContext().getAuthentication().getName());
        if(producer==null)
            return ResponseEntity.badRequest().body(null);

        return ResponseEntity.ok(producer);
    }

    @PostMapping("/passwordRecovered/checkEmail")
    public ResponseEntity<String> checkEmailExists(@RequestBody LoginRequest loginRequest) {
      try {
          producerService.createRecoverRequest(loginRequest.getEmail());
          jsonObject = new JSONObject();
          jsonObject.put("result",EMAIL_SENT);
          return ResponseEntity.ok(jsonObject.toString());
      }catch (Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
      }
    }

    @GetMapping("/passwordRecovered/{token}")
    public ResponseEntity<String> validateToken(@PathVariable String token) {
        try {
            producerService.validateToken(token);
            jsonObject = new JSONObject();
            jsonObject.put("result",TOKEN_VALIDATED);
            return ResponseEntity.ok(jsonObject.toString());
        }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/passwordRecovered/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        try {
            producerService.validateAndChangePassword(changePasswordRequest);
            jsonObject = new JSONObject();
            jsonObject.put("result",PASSWORD_CHANGED);
            return ResponseEntity.ok(jsonObject.toString());
        }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //IMAGES---------------------------------------------------------------------------------------------------------------------------
    @PostMapping("/uploadLogo")
    public ResponseEntity<String> updateLogo(@RequestParam("logo") MultipartFile image){
        try {
            jsonObject = new JSONObject();
            jsonObject.put("result", "success");
            producerService.uploadProducerLogo(image);
            return ResponseEntity
                    .ok(jsonObject.toString());
        } catch (IOException e) {
            String message = e.getMessage();
            return ResponseEntity.badRequest().body(message);
        }
    }
    @PostMapping("/uploadCover")
    public ResponseEntity<String> updateImage(@RequestParam("cover") MultipartFile image){
        try {
            jsonObject = new JSONObject();
            jsonObject.put("result", "success");
            producerService.uploadProducerCover(image);
            return ResponseEntity
                    .ok(jsonObject.toString());
        } catch (IOException e) {
            String message = e.getMessage();
            return ResponseEntity.badRequest().body(message);
        }
    }
    @PostMapping("/uploadImages")
    public ResponseEntity<String> uploadImage(@RequestParam("other") MultipartFile[] images){
        try {
            jsonObject = new JSONObject();
            jsonObject.put("result", "success");
            producerService.uploadProducerImages(images);
            return ResponseEntity
                    .ok(jsonObject.toString());
        } catch (IOException e) {
            String message = e.getMessage();
            return ResponseEntity.badRequest().body(message);
        }
    }
    @DeleteMapping("/deleteAllImages/{id}")
    public ResponseEntity<String> deleteAllImages(@PathVariable("id") String id) {
        imageService.deleteAllImages(id);
        try {
            return ResponseEntity.ok("Images deleted successfully");
        } catch (Exception e) {
            String message = e.getMessage();
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/deleteImage/{id}")
    public ResponseEntity<String> deleteImage( @PathVariable String id) {
        imageService.deleteImage(id);
        try {
            return ResponseEntity.ok("Image deleted successfully");
        } catch (Exception e) {
            String message = e.getMessage();
            return ResponseEntity.badRequest().body(null);
        }
    }
    @GetMapping("/retrieveLogo/{id}")
    public ResponseEntity<Image> retrieveLogo(@PathVariable String id) {
        try {
          return ResponseEntity.ok(imageService.retrieveLogo(id));

        } catch (Exception e) {
            String message = e.getMessage();
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/retrieveCover/{id}")
    public ResponseEntity<Image> retrieveCover(@PathVariable String id) {
        try {
            return ResponseEntity.ok(imageService.retrieveCover(id));

        } catch (Exception e) {
            String message = e.getMessage();
            return ResponseEntity.badRequest().body(null);
        }
    }



    @GetMapping("/retrieveAllImages/{id}")
    public ResponseEntity<List<Image>> retrieveAllImages(@PathVariable String id) {
        try {
          return ResponseEntity.ok(imageService.retrieveAllImages(id));

        } catch (Exception e) {
            String message = e.getMessage();
            return ResponseEntity.badRequest().body(null);
        }
    }

    //PRODUCERS------------------------------------------------------------------------------------------------
    @GetMapping(value="producersDetails/list")
    public ResponseEntity<List<ProducerDetails>> allDetailsProducers(){
        List<Producer> producers = adminService.retrieveAllProducersApproved();
        List<ProducerDetails> producerDetails =
                userService.createListProducerDetails(producers);
        return ResponseEntity.ok(producerDetails);
    }

    @GetMapping("producersDetails/{id}")
    public ResponseEntity<ProducerDetails> getProducer(@PathVariable String id) {
        try {
            return ResponseEntity.ok(userService.createDetails(adminService.retrieveProducer(id)));
        }
        catch (Exception e) {
            String message = e.getMessage();
            return ResponseEntity.badRequest().body(null);
        }
    }
    @PostMapping("/updateProducer")
    public ResponseEntity<JwtAuthenticationResponse> updateProducerContact(@RequestBody UpdateProducerRequest updateContactRequest) {
        try {
            return ResponseEntity
                    .ok(producerService.updateProducerContact(updateContactRequest));
        } catch (IllegalArgumentException e) {
            String errorMessage = e.getMessage().substring(e.getMessage().indexOf(":") + 2);

            return ResponseEntity.badRequest().body(JwtAuthenticationResponse.builder().error(errorMessage).build());
        }
    }


    @GetMapping("/getProducers/filtered")
    public ResponseEntity<List<ProducerDetails>> getProducers(@RequestParam String products, @RequestParam String province) {
        try {
            return ResponseEntity.ok(userService.getProducersFilter(products, province));
        } catch (IllegalArgumentException e) {
            String errorMessage = e.getMessage();
            return ResponseEntity.badRequest().body(null);
        }
    }

    //NEWSLETTER------------------------------------------------------------------------------------------------------------------
    @PostMapping("/createNewsLetter")
    public ResponseEntity<String> createNewsLetter(@RequestPart("newsletter") NewsLetter newsletter,
                                                   @RequestPart("image") MultipartFile image){
        try {
            jsonObject = new JSONObject();
            newsLetterService.createNewsLetter(newsletter, image);
            jsonObject.put("result",VALID_NEWSLETTER);
            return ResponseEntity.ok(jsonObject.toString());
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getNewsletterProducer/{id}")
    public ResponseEntity<List<NewsLetter>> getNewsletterProducer(@PathVariable String id){
    try {
        return ResponseEntity.ok(newsLetterService.retrieveNewsletterProducer(id));
        }
    catch (Exception e) {
        String message = e.getMessage();
        return ResponseEntity.badRequest().body(null);
    }
    }

    @DeleteMapping("/deleteProducerNewsletter/{id}")
    public ResponseEntity<String> deleteProducerNewsLetter(@PathVariable("id") String id) {
        try {
            jsonObject = new JSONObject();
            newsLetterService.deleteProducerNewsletter(id);
            jsonObject.put("result",NEWSLETTER_DELETED);
            return ResponseEntity.ok(jsonObject.toString());
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}