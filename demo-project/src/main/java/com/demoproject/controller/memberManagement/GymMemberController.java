package com.demoproject.controller.memberManagement;

import com.demoproject.entity.memberManagement.Members;
import com.demoproject.repository.attendanceManagement.ClassScheduleRepo;
import com.demoproject.repository.membershipManagement.MemberRepo;
import com.demoproject.repository.membershipManagement.MembersGoalRepo;
import com.demoproject.repository.membershipTypeManagement.MembershipTypeRepo;
import com.demoproject.util.ImageOptimizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Controller
public class GymMemberController {

    //Save the uploaded file to this folder
    private static final String UPLOADED_FOLDER = "src/main/resources/static/upload/";

    @Autowired
    private MemberRepo repo;

    @Autowired
    private MembersGoalRepo goalRepo;

    @Autowired
    private MembershipTypeRepo typeRepo;

    @Autowired
    private ClassScheduleRepo classRepo;

    @Autowired
    private ImageOptimizer imageOptimizer;


    @GetMapping(value = "/add-member")
    public String addMember(Model model) {
        model.addAttribute("members", new Members());
        model.addAttribute("goal", this.goalRepo.findAll());
        model.addAttribute("msType", this.typeRepo.findAll());
        model.addAttribute("classTime", this.classRepo.findAll());
        return "gym-member/add-member";
    }

    @PostMapping(value = "/add-member")
    public String saveMember(@Valid Members members, @RequestParam(value = "file", required = false) MultipartFile file,
                             BindingResult result, Model model) throws IOException {
        members.setPhoto("/upload/" + file.getOriginalFilename());

        model.addAttribute("members", new Members());
        model.addAttribute("goal", this.goalRepo.findAll());
        model.addAttribute("msType", this.typeRepo.findAll());
        model.addAttribute("classTime", this.classRepo.findAll());
        if (result.hasErrors()) {
            return "gym-member/add-member";
        } else {
            try {

                byte[] bytes = file.getBytes();
                Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
                Files.write(path, bytes);
                this.repo.save(members);
                model.addAttribute("succMsg", "Success !");
                model.addAttribute("list", this.repo.findAll());
                model.addAttribute("members", new Members());
                // Get the file and save it somewhere
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "redirect:/members-list";
    }


    @GetMapping(value = "/members-list")
    public String showGymMemberList(Model model) {
        model.addAttribute("mlist", repo.findAll());

        return "gym-member/members-list";
    }

    @GetMapping(value = "/edit-member/{id}")
    public String editView(Model model, @PathVariable("id") Long id) {
        model.addAttribute("memberEdit", this.repo.getOne(id));
        return "gym-member/edit-member";
    }

    @PostMapping("/edit-member/{id}")
    public String editMember(@Valid Members member, BindingResult result, @PathVariable("id") Long id) {
        if (result.hasErrors()) {
            return "gym-member/edit-member";
        } else {
            this.repo.save(member);
        }
        return "redirect:/members-list";
    }


    @GetMapping(value = "/member-profile")
    public String memberProfile(Model model) {

        model.addAttribute("profile", this.repo.findAll());

        return "gym-member/member-profile";
    }
}
