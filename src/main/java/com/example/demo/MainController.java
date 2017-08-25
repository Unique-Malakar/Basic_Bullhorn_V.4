package com.example.demo;

import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

@Controller
public class MainController {

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    CloudinaryConfig cloudc;


    @RequestMapping("/")
    public String listMessages(Model model){
        model.addAttribute("messages", messageRepository.findAll());
        return "listOfMessage";

    }

    @GetMapping("/add")
    public String messageInfo(Model model){
        model.addAttribute("message", new Message());
        return "messageInfo";
    }

    @PostMapping("/process")
    public String processInfo(@Valid Message message, BindingResult result){

        if (result.hasErrors()){
            return "messageInfo";
        }
        messageRepository.save(message);
        return "redirect:/";
    }

    @RequestMapping("/view/{id}")
    public String viewMessage(@PathVariable("id") long id, Model model){
        model.addAttribute("message", messageRepository.findOne(id));
        return "show";


    }


    @PostMapping("/add")
    public String processActor(@ModelAttribute Message message,
                               @RequestParam("file")MultipartFile file){
        if (file.isEmpty()){
            return "redirect:/add";
        }
        try {
            Map uploadResult = cloudc.upload(file.getBytes(),
                    ObjectUtils.asMap("resourcetype", "auto"));

            message.setShot(uploadResult.get("url").toString());
            messageRepository.save(message);
        } catch (IOException e){
            e.printStackTrace();
            return "redirect:/add";
        }
        return "redirect:/";
    }
    /**@RequestMapping("/edit/{id}")
    public String editMessage(@PathVariable("id") long id, Model model){
        model.addAttribute("message", messageRepository.findOne(id));
        return "messageInfo";
    }
    **/
}
