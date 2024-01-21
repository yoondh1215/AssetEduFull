package kr.co.kfs.assetedu.controller.jnl;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.kfs.assetedu.model.Condition;
import kr.co.kfs.assetedu.model.Jnl11ReprAcnt;
import kr.co.kfs.assetedu.service.Com02CodeService;
import kr.co.kfs.assetedu.service.Jnl11ReprAcntService;
import lombok.extern.slf4j.Slf4j;

/**
 * 대표계정코드 Controller
 *
 */
@Slf4j
@Controller
@RequestMapping("/jnl/repr-acnt")
public class ReprAcntController {
	
	private final String baseUrl = "/jnl/repr-acnt";

	@Autowired
	private Jnl11ReprAcntService service;
	
	@Autowired
	private Com02CodeService codeService;

	
	@GetMapping("list")
	public String list(String searchText,   Model model) {
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		log.debug("대표계정코드");
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		model.addAttribute("pageTitle","대표계정코드");
		
		Condition condition = new Condition();
		condition.put("searchText", searchText);
		List<Jnl11ReprAcnt> list = service.selectList(condition);
		model.addAttribute("list", list);
		return baseUrl + "/list";
	}
	
	@GetMapping("insert")
	public String insert(Model model) {
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		log.debug("대표계정코드 추가 화면");
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		model.addAttribute("pageTitle","대표계정코드추가");
		model.addAttribute("jnl11ReprAcnt"     ,new Jnl11ReprAcnt());
		model.addAttribute("acntAttrCodeList", codeService.codeList("AcntAttrCode"));
		return baseUrl + "/insert_form";
	}
	
	@PostMapping("insert")
	public String insert(@Valid @ModelAttribute("jnl11ReprAcnt") Jnl11ReprAcnt reprAcnt, BindingResult bindingResult, RedirectAttributes redirectAttr, Model model)  {
		log.debug("★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆");
		log.debug("대표계정코드 저장 후 리스트로 이동");
		log.debug("★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆");
		log.debug("reprAcnt: {}", reprAcnt);

		String msg;
		
		//대표계정코드 중복체크
		Jnl11ReprAcnt checkReprAcnt = service.selectOne(reprAcnt);
		if(checkReprAcnt != null) {
			msg = String.format("\"%s\" 대표계정코드는 이미 \"%s\"으로 등록되어 있습니다", reprAcnt.getJnl11ReprAcntCd(), checkReprAcnt.getJnl11ReprAcntNm());
			model.addAttribute("jnl11ReprAcnt", reprAcnt);
			model.addAttribute("acntAttrCodeList", codeService.codeList("AcntAttrCode"));
			bindingResult.addError(new FieldError("", "", msg));
			return baseUrl + "/insert_form";
		} else {
			int affectedCount = 0;
			affectedCount = service.insert(reprAcnt);
			log.debug("DB에 적용된 갯수 : {}",affectedCount );

			//성공화면으로  redirect
			redirectAttr.addFlashAttribute("mode", "insert");
			redirectAttr.addFlashAttribute("reprAcnt", reprAcnt);
			return "redirect:" + baseUrl + "/success";
		}
	}
	
	@GetMapping("/update/{jnl11ReprAcntCd}")
	public String update(@PathVariable("jnl11ReprAcntCd") String jnl11ReprAcntCd, Model model)  {
		log.debug("★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆");
		log.debug("대표계정코드 수정 form 표시");
		log.debug("★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆");
		Jnl11ReprAcnt reprAcnt = service.selectOne(jnl11ReprAcntCd);
		model.addAttribute("jnl11ReprAcnt", reprAcnt);
		model.addAttribute("acntAttrCodeList", codeService.codeList("AcntAttrCode"));
		return baseUrl + "/update_form";
	}
	
	@PostMapping("update")
	public String update_form(@ModelAttribute("Jnl11ReprAcnt") Jnl11ReprAcnt jnl11ReprAcnt, RedirectAttributes redirectAttr) throws UnsupportedEncodingException {
		log.debug("★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆");
		log.debug("대표계정코드 수정");
		log.debug("★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆");
		log.debug("repr-acnt: {}", jnl11ReprAcnt);
		service.update(jnl11ReprAcnt);	
		
		//성공화면으로  redirect
		redirectAttr.addFlashAttribute("mode", "update");
		redirectAttr.addFlashAttribute("reprAcnt", jnl11ReprAcnt);
		log.debug("repr-acnt:{}",jnl11ReprAcnt);
		return "redirect:" + baseUrl +"/success"; 
	}

	@GetMapping("/delete/{jnl11ReprAcntCd}")
	public String delete(@PathVariable("jnl11ReprAcntCd") String jnl11ReprAcntCd) {
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		log.debug("acnt삭제할 id : {}",jnl11ReprAcntCd);
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		
		int deletedCount = service.delete(jnl11ReprAcntCd);
		if(deletedCount > 0) {
			
			log.warn("acnt id : {}가 삭제되었습니다");
		}
		return "redirect:" + baseUrl + "/list";
	}

//	@GetMapping("success")
//	public String success(String msg, String mode, String jnl11ReprAcntCd, Model model) {
//		model.addAttribute("pageTitle","대표계정코드 관리");
//		model.addAttribute("msg", msg);
//		model.addAttribute("mode", mode);
//		model.addAttribute("jnl11ReprAcntCd", jnl11ReprAcntCd);
//		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
//		log.debug("대표계정코드 추가 화면");
//		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
//		return baseUrl +"/success";
//	}
	@GetMapping("success")
	public String success(Model model) {
		return baseUrl +"/success";
	}	
	
}
