package kr.co.kfs.assetedu.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.kfs.assetedu.model.Condition;
import kr.co.kfs.assetedu.model.Sys01User;
import kr.co.kfs.assetedu.service.Sys01UserService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/admin/user")
public class UserController {
	
	@Autowired
	private Sys01UserService service;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@GetMapping("list")
	public String list(String searchText,  Model model) {
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		log.debug("사용자리스트");
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		model.addAttribute("pageTitle","사용자리스트");
		Condition condition = new Condition();
		condition.put("searchText", searchText);
		List<Sys01User> list = service.selectList(condition);
		model.addAttribute("list", list);
		return "/admin/user/list";
	}
	@GetMapping("insert")
	public String insert(Model model) {
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		log.debug("사용자 추가 화면");
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		model.addAttribute("pageTitle","사용자추가");
		model.addAttribute("user"     ,new Sys01User());

		return "/admin/user/insert_form";
	}
	@PostMapping("insert")
	public String insert(@Valid @ModelAttribute Sys01User user, RedirectAttributes redirectAttr)  {
		log.debug("★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆");
		log.debug("사용자정보를 테이블에 저장하고 리스트로 이동");
		log.debug("★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆");
		log.debug("user: {}", user);
		// 패스워드 암호화
		String pwd = user.getSys01Pwd();
		user.setSys01Pwd(passwordEncoder.encode(pwd));
		int affectedCount = service.insert(user);
		log.debug("DB에 적용된 갯수 : {}",affectedCount);
		String msg = String.format("사용자 %s 님이 추가되었습니다", user.getSys01UserNm());
		redirectAttr.addAttribute("mode", "insert");
		redirectAttr.addAttribute("msg", msg);
		return "redirect:/admin/user/success";
	}
	
	@GetMapping("update")
	public String update(@ModelAttribute("user") Sys01User user, Model model)  {
		log.debug("★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆");
		log.debug("사용자정보 수정 form 표시");
		log.debug("★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆");
		user = service.selectOne(user);
		model.addAttribute("user", user);
		
		return "/admin/user/update_form";
	}

	@PostMapping("update")
	public String update_form(@ModelAttribute("user") Sys01User user, RedirectAttributes redirectAttr) throws UnsupportedEncodingException {
		log.debug("★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆");
		log.debug("사용자정보 수정");
		log.debug("★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆");
		log.debug("user: {}", user);
		String pwd = user.getSys01Pwd();
		user.setSys01Pwd(passwordEncoder.encode(pwd));
		service.update(user);	
		
		//성공화면을 redirect
		String msg = String.format("사용자 %s 님이 수정되었습니다.", user.getSys01UserNm());
		redirectAttr.addAttribute("mode", "update");
		redirectAttr.addAttribute("userId", user.getSys01UserId());
		redirectAttr.addAttribute("msg", msg);
		return "redirect:/admin/user/success"; 
	}
	
	@GetMapping("/delete")
	public String delete(@ModelAttribute Sys01User user) {
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		log.debug("사용자삭제 삭제할 id :" + user.getSys01UserId());
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		
		int deletedCount = service.delete(user);
		if(deletedCount > 0) {
			
			log.warn("사용자 id : {}가 삭제되었습니다");
		}
		return "redirect:/admin/user/list";
	}
	@GetMapping("success")
	public String success(String msg, String mode, String userId, Model model) {
		model.addAttribute("pageTitle","사용자추가");
		model.addAttribute("msg", msg);
		model.addAttribute("mode", mode);
		model.addAttribute("userId", userId);
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		log.debug("사용자 추가 화면");
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		return "/admin/user/success";
	}
}
